package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.service.request.ApsPlanListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.service.specs.ApsPlanSpecs;
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
public class ApsPlanService {
    private final ApsPlanRepository apsPlanRepository;

    private final CustomerRepository customerRepository;

    public ApsPlanService(ApsPlanRepository apsPlanRepository, CustomerRepository customerRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.customerRepository = customerRepository;
    }

    public boolean notFound(Long id) {
        return !apsPlanRepository.existsById(id);
    }

    public ApsPlan create(ApsPlan apsPlan) {
        apsPlan.setCreated(Instant.now());
        apsPlan.setUpdated(Instant.now());
        ApsPlan result = apsPlanRepository.save(apsPlan);
        updateCustomerLicence(result);
        return result;
    }

    public ApsPlan update(ApsPlan apsPlan) {
        apsPlan.setUpdated(Instant.now());
        ApsPlan result = apsPlanRepository.save(apsPlan);
        updateCustomerLicence(result);
        return result;
    }

    public Optional<ApsPlan> loadById(Long id) {
        List<ApsPlan> list = apsPlanRepository.loadById(id);
        return list.isEmpty()? Optional.empty(): Optional.of(list.get(0));
    }

    public void updateCustomerLicence(ApsPlan apsPlan) {
        updateCustomerLicense(apsPlan.getCustomer().getId(), apsPlan.getLicense());
    }

    public void updateCustomerLicense(Long customerId, String license) {
        if (Objects.isNull(customerId) || !StringUtils.hasText(license)) {
            return;
        }
        customerRepository.findById(customerId)
            .map(customer -> {
                customer.setLicense(license);
                return customer;
            }).map(customerRepository::save);

    }

    public Page<ApsPlan> findAll(ApsPlanListRequest request, Pageable pageable) {
        Specification<ApsPlan> spec = ApsPlanSpecs.agencyId(request.getAgencyId());
        if (StringUtils.hasText(request.getContract())) {
            spec = spec.and(ApsPlanSpecs.contract(request.getContract()));
        }
        if (StringUtils.hasText(request.getCustomerCode())) {
            spec = spec.and(ApsPlanSpecs.customerCode(request.getCustomerCode()));
        }
        if (Objects.nonNull(request.getState())) {
            spec = spec.and(ApsPlanSpecs.state(request.getState()));
        }
        if (Objects.nonNull(request.getStarts())) {
            spec = spec.and(ApsPlanSpecs.starts(request.getStarts()));
        }
        if (Objects.nonNull(request.getEnds())) {
            spec = spec.and(ApsPlanSpecs.ends(request.getEnds()));
        }
        return apsPlanRepository.findAll(spec, pageable);
    }

}
