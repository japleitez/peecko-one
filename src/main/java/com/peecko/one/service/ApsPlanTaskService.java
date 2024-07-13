package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.repository.ApsPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ApsPlanTaskService {
    private final Logger log = LoggerFactory.getLogger(ApsPlanTaskService.class);
    private final ApsPlanRepository apsPlanRepository;

    public ApsPlanTaskService(ApsPlanRepository apsPlanRepository) {
        this.apsPlanRepository = apsPlanRepository;
    }

    public void closeExpiredPlans() {
        List<ApsPlan> list = apsPlanRepository.getActivePlansToClose(LocalDate.now())
            .stream()
            .map(this::closePlan)
            .toList();
        System.out.println("Expired plans closed: " + list.size());
    }

    private ApsPlan closePlan(ApsPlan apsPlan) {
        apsPlan.setState(PlanState.CLOSED);
        apsPlan.setUpdated(Instant.now());
        apsPlanRepository.save(apsPlan);
        return apsPlan;
    }

}
