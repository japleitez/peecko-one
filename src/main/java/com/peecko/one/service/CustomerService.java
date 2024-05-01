package com.peecko.one.service;

import com.peecko.one.domain.Customer;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.service.request.CustomerListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> loadById(Long id) {
        List<Customer> list = customerRepository.loadById(id);
        return list.isEmpty()?  Optional.empty(): Optional.of(list.get(0));
    }

    public Page<Customer> findAll(CustomerListRequest request, Pageable pageable) {
        Specification<Customer> spc1 = CustomerSpecs.agency(request.getAgencyId());
        Specification<Customer> spc2 = CustomerSpecs.codeLike(request.getCode());
        Specification<Customer> spc3 = CustomerSpecs.nameLike(request.getName());
        Specification<Customer> spc4 = CustomerSpecs.licenseLike(request.getLicense());
        Specification<Customer> spc5 = CustomerSpecs.stateEqual(request.getState());
        Specification<Customer>  spec = spc1.and(spc2.and(spc3).and(spc4).and(spc5));
        return customerRepository.findAll(spec, pageable);
    }

}
