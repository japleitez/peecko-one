package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsPlanRepository;
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

}
