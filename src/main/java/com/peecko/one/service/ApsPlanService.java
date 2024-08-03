package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.service.request.ApsPlanListRequest;
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
        Instant now = Instant.now();
        apsPlan.setCreated(now);
        apsPlan.setUpdated(now);
        Customer customer = customerRepository.getReferenceById(apsPlan.getCustomer().getId());
        apsPlan.setAgencyId(customer.getAgency().getId());
        return apsPlanRepository.save(apsPlan);
    }

    public ApsPlan update(ApsPlan apsPlan) {
        apsPlan.setUpdated(Instant.now());
        return apsPlanRepository.save(apsPlan);
    }

    public void deleteById(Long id) {
        if (apsPlanRepository.existsById(id)) {
            apsPlanRepository.deleteById(id);
        }
    }
    public Optional<ApsPlan> loadById(Long id) {
        List<ApsPlan> list = apsPlanRepository.loadById(id);
        return list.isEmpty()? Optional.empty(): Optional.of(list.get(0));
    }

    public Optional<ApsPlan> partialUpdateApsPlan(ApsPlan apsPlan) {
        return apsPlanRepository
            .findById(apsPlan.getId())
            .map(existingApsPlan -> {
                if (apsPlan.getContract() != null) {
                    existingApsPlan.setContract(apsPlan.getContract());
                }
                if (apsPlan.getPricing() != null) {
                    existingApsPlan.setPricing(apsPlan.getPricing());
                }
                if (apsPlan.getState() != null) {
                    existingApsPlan.setState(apsPlan.getState());
                }
                if (apsPlan.getLicense() != null) {
                    existingApsPlan.setLicense(apsPlan.getLicense());
                }
                if (apsPlan.getStarts() != null) {
                    existingApsPlan.setStarts(apsPlan.getStarts());
                }
                if (apsPlan.getEnds() != null) {
                    existingApsPlan.setEnds(apsPlan.getEnds());
                }
                if (apsPlan.getUnitPrice() != null) {
                    existingApsPlan.setUnitPrice(apsPlan.getUnitPrice());
                }
                if (apsPlan.getNotes() != null) {
                    existingApsPlan.setNotes(apsPlan.getNotes());
                }
                existingApsPlan.setUpdated(Instant.now());
                return existingApsPlan;
            })
            .map(apsPlanRepository::save);
    }

    public List<ApsPlan> getPlansForAgencyAndStates(Long agencyId, List<PlanState> states) {
        return apsPlanRepository.getByAgencyAndStates(agencyId, states);
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
