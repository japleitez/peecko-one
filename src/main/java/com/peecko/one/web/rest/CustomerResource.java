package com.peecko.one.web.rest;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public CustomerResource(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        log.debug("REST request to update Customer : {}, {}", id, customer);
        if (customer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Customer result = customerRepository.save(customer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString()))
            .body(result);
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
        if (customer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Customer> result = customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getCode() != null) {
                    existingCustomer.setCode(customer.getCode());
                }
                if (customer.getName() != null) {
                    existingCustomer.setName(customer.getName());
                }
                if (customer.getCountry() != null) {
                    existingCustomer.setCountry(customer.getCountry());
                }
                if (customer.getLicense() != null) {
                    existingCustomer.setLicense(customer.getLicense());
                }
                if (customer.getState() != null) {
                    existingCustomer.setState(customer.getState());
                }
                if (customer.getCloseReason() != null) {
                    existingCustomer.setCloseReason(customer.getCloseReason());
                }
                if (customer.getEmailDomains() != null) {
                    existingCustomer.setEmailDomains(customer.getEmailDomains());
                }
                if (customer.getVatId() != null) {
                    existingCustomer.setVatId(customer.getVatId());
                }
                if (customer.getBank() != null) {
                    existingCustomer.setBank(customer.getBank());
                }
                if (customer.getIban() != null) {
                    existingCustomer.setIban(customer.getIban());
                }
                if (customer.getLogo() != null) {
                    existingCustomer.setLogo(customer.getLogo());
                }
                if (customer.getNotes() != null) {
                    existingCustomer.setNotes(customer.getNotes());
                }
                if (customer.getCreated() != null) {
                    existingCustomer.setCreated(customer.getCreated());
                }
                if (customer.getUpdated() != null) {
                    existingCustomer.setUpdated(customer.getUpdated());
                }
                if (customer.getTrialed() != null) {
                    existingCustomer.setTrialed(customer.getTrialed());
                }
                if (customer.getDeclined() != null) {
                    existingCustomer.setDeclined(customer.getDeclined());
                }
                if (customer.getActivated() != null) {
                    existingCustomer.setActivated(customer.getActivated());
                }
                if (customer.getClosed() != null) {
                    existingCustomer.setClosed(customer.getClosed());
                }

                return existingCustomer;
            })
            .map(customerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customer.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Customer>> getAllCustomers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Customers");
        Agency agency = SecurityUtils.getCurrentUserAgency();
        Page<Customer> page = customerRepository.findByAgency(agency, pageable);
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
        Optional<Customer> customer = customerRepository.findById(id);
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
}
