package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.web.rest.payload.request.ApsPlanListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Specification<ApsPlan> agency = ApsPlanSpecs.agencyId(request.getAgencyId());
        Specification<ApsPlan> customerCode = ApsPlanSpecs.customerCode(request.getCustomer());
        Specification<ApsPlan> contract = ApsPlanSpecs.contract(request.getContract());
        Specification<ApsPlan> state = ApsPlanSpecs.state(request.getState());
        Specification<ApsPlan> starts = ApsPlanSpecs.starts(request.getStarts());
        Specification<ApsPlan> ends = ApsPlanSpecs.ends(request.getEnds());
        Specification<ApsPlan> spec = agency.and(state.and(customerCode.or(contract)).and(starts).and(ends));
        return apsPlanRepository.findAll(spec, pageable);
    }

}
