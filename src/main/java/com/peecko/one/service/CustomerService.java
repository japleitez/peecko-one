package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.service.request.CustomerListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(Customer customer) {
        resolveFields(customer);
        customer.setCreated(Instant.now());
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        resolveFields(customer);
        customer.setUpdated(Instant.now());
        return customerRepository.save(customer);
    }

    private void resolveFields(Customer customer) {
        boolean isDummy = customer.getCode().toUpperCase().endsWith("-ALL");
        customer.setDummy(isDummy);
        if (CustomerState.TRIAL.equals(customer.getState())) {
            customer.setTrialed(Instant.now());
        } else if (CustomerState.ACTIVE.equals(customer.getState())) {
            customer.setActivated(Instant.now());
        } else if (CustomerState.CLOSED.equals(customer.getState())) {
            customer.setClosed(Instant.now());
        }
    }

    public void delete(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        }
    }

    public boolean notFound(Long id) {
        return !customerRepository.existsById(id);
    }

    public Optional<Customer> loadById(Long id) {
        List<Customer> list = customerRepository.loadById(id);
        return list.isEmpty()?  Optional.empty(): Optional.of(list.get(0));
    }

    public List<Customer> findByAgencyAndCustomerStates(Agency agency, List<CustomerState> states) {
        return customerRepository.findByAgencyAndCustomerState(agency, states);
    }


    public Page<Customer> findAll(CustomerListRequest request, Pageable pageable) {
        Specification<Customer> spec = CustomerSpecs.agency(request.getAgencyId());
        if (StringUtils.hasText(request.getCode())) {
            spec = spec.and(CustomerSpecs.codeLike(request.getCode()));
        }
        if (StringUtils.hasText(request.getName())) {
            spec = spec.and(CustomerSpecs.nameLike(request.getName()));
        }
        if (StringUtils.hasText(request.getLicense())) {
            spec = spec.and(CustomerSpecs.licenseLike(request.getLicense()));
        }
        if (Objects.nonNull(request.getState())) {
            spec = spec.and(CustomerSpecs.stateEqual(request.getState()));
        }
        return customerRepository.findAll(spec, pageable);
    }

    public Optional<Customer> partialUpdateCustomer(Customer input) {
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
                    customer.setState(input.getState());
                }
                if (input.getCloseReason() != null) {
                    customer.setCloseReason(input.getCloseReason());
                }
                resolveFields(customer);
                boolean isDummy = customer.getCode().toUpperCase().endsWith("-ALL");
                customer.setDummy(isDummy);
                return customer;
            })
            .map(customerRepository::save);
    }

}
