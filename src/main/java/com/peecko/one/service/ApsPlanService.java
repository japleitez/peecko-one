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
        Specification<ApsPlan> sp1 = ApsPlanSpecs.agencyId(request.getAgencyId());
        Specification<ApsPlan> sp2 = ApsPlanSpecs.customerCode(request.getCustomer());
        Specification<ApsPlan> sp3 = ApsPlanSpecs.contract(request.getContract());
        Specification<ApsPlan> sp4 = ApsPlanSpecs.state(request.getState());
        Specification<ApsPlan> spec = sp1.and(sp2.and(sp3).and(sp4));
        return apsPlanRepository.findAll(spec, pageable);
    }

}
