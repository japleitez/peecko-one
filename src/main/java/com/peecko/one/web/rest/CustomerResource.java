package com.peecko.one.web.rest;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.CustomerService;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import com.peecko.one.web.rest.payload.request.CustomerListRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.peecko.one.domain.Customer}.
 */
@RestController
@RequestMapping("/api/customers")
@Transactional
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerRepository customerRepository;

    private final CustomerService customerService;

    public CustomerResource(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customer the customer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customer, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);
        if (customer.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customer.setDummy(false);
        Customer result = customerRepository.save(customer);
        return ResponseEntity
            .created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customer to save.
     * @param customer the customer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customer,
     * or with status {@code 400 (Bad Request)} if the customer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Customer customer
    ) throws URISyntaxException {
        Optional<Customer> result = updateCustomer(customer, id);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString())
        );
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param id the id of the customer to save.
     * @param customer the customer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customer,
     * or with status {@code 400 (Bad Request)} if the customer is not valid,
     * or with status {@code 404 (Not Found)} if the customer is not found,
     * or with status {@code 500 (Internal Server Error)} if the customer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Customer> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Customer customer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customer partially : {}, {}", id, customer);
        Optional<Customer> result = updateCustomer(customer, id);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get the customers matching the criteria
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Customer>> getAllCustomers(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String license,
        @RequestParam(required = false) CustomerState state,
        @ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Customers");
        Long agencyId = SecurityUtils.getCurrentAgencyId();
        CustomerListRequest request = new CustomerListRequest(agencyId, code, name, license, state);
        Page<Customer> page = customerService.findAll(request, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Customer>> findActiveCustomers() {
        log.debug("REST request to get active Customers");
        Agency agency = SecurityUtils.getCurrentUserAgency();
        List<Customer> customers = customerRepository.findByAgencyAndCustomerState(agency, CustomerState.ACTIVE);
        List<Customer> list = customers.stream().map(Customer::cloneForSelection).toList();
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
        log.debug("REST request to get Customer : {}", id);
        Optional<Customer> customer = customerService.loadById(id);
        return ResponseUtil.wrapOrNotFound(customer);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private Optional<Customer> updateCustomer(Customer input, Long id) {
        if (input.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, input.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return customerRepository
            .findById(input.getId())
            .map(customer -> {
                if (input.getCode() != null) {
                    customer.setCode(input.getCode());
                }
                if (input.getName() != null) {
                    customer.setName(input.getName());
                }
                if (input.getCountry() != null) {
                    customer.setCountry(input.getCountry());
                }
                if (input.getLicense() != null) {
                    customer.setLicense(input.getLicense());
                }
                if (input.getEmailDomains() != null) {
                    customer.setEmailDomains(input.getEmailDomains());
                }
                if (input.getVatId() != null) {
                    customer.setVatId(input.getVatId());
                }
                if (input.getBank() != null) {
                    customer.setBank(input.getBank());
                }
                if (input.getIban() != null) {
                    customer.setIban(input.getIban());
                }
                if (input.getLogo() != null) {
                    customer.setLogo(input.getLogo());
                }
                if (input.getNotes() != null) {
                    customer.setNotes(input.getNotes());
                }
                if (!customer.getState().equals(input.getState())) {
                    switch (input.getState()) {
                        case TRIAL -> {
                            customer.setTrialed(Instant.now());
                        }
                        case ACTIVE -> {
                            customer.setActivated(Instant.now());
                        }
                        case CLOSED -> {
                            customer.setClosed(Instant.now());
                        }
                    }
                    customer.setState(input.getState());
                }
                if (input.getCloseReason() != null) {
                    customer.setCloseReason(input.getCloseReason());
                }
                customer.setDummy(false);
                return customer;
            })
            .map(customerRepository::save);
    }
}
