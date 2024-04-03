package com.peecko.one.service;

import com.peecko.one.domain.Customer;
import com.peecko.one.repository.CustomerRepository;
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
}
