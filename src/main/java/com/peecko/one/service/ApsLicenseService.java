package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.utils.UUIDUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApsLicenseService {

    private final ApsPlanRepository apsPlanRepository;

    private final CustomerRepository customerRepository;

    public ApsLicenseService(ApsPlanRepository apsPlanRepository, CustomerRepository customerRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<ApsPlan> activateApsPlanForTrial(Long customerId, LocalDate starts, LocalDate ends) {
        return Optional.ofNullable(customerRepository
                .findById(customerId)
                .map(customer -> {
                    customer.setState(CustomerState.TRIAL);
                    customer.setTrialed(Instant.now());
                    customer.setLicense(UUIDUtils.generateTrialLicense(customer.getCode()));
                    return customer;
                })
                .map(customerRepository::save)
                .map(customer -> createApsPlanForTrial(customer, starts, ends))
                .orElseThrow(() -> new RuntimeException("Failed to activate trial plan for customer " + customerId)));
    }

    private ApsPlan createApsPlanForTrial(Customer customer, LocalDate trialStarts, LocalDate trialEnds) {
        ApsPlan apsPlan = new ApsPlan();
        apsPlan.setCustomer(Customer.of(customer.getId()));
        apsPlan.setLicense(customer.getLicense());
        apsPlan.setPricing(PricingType.TRIAL);
        apsPlan.setState(PlanState.TRIAL);
        apsPlan.setUnitPrice(0.0);
        apsPlan.setCreated(Instant.now());
        apsPlan.setLicense(customer.getLicense());
        apsPlan.setContract("NC");
        apsPlan.setUpdated(null);
        apsPlanRepository.save(apsPlan);
        return apsPlan;
    }

}
