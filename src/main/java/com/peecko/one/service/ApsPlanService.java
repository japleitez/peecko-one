package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.service.request.ApsPlanListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.service.specs.ApsPlanSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApsPlanService {
    private final ApsPlanRepository apsPlanRepository;

    public ApsPlanService(ApsPlanRepository apsPlanRepository) {
        this.apsPlanRepository = apsPlanRepository;
    }

    public Optional<ApsPlan> loadById(Long id) {
        List<ApsPlan> list = apsPlanRepository.loadById(id);
        return list.isEmpty()? Optional.empty(): Optional.of(list.get(0));
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
