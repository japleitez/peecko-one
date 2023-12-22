package com.peecko.one.service;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.utils.PeriodUtils;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApsOrderService {
    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;

    public ApsOrderService(ApsPlanRepository apsPlanRepository, ApsOrderRepository apsOrderRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
    }

    public List<ApsOrder> batchGenerate(Long agencyId, YearMonth yearMonth) {
        Integer period = PeriodUtils.getPeriod(yearMonth);
        List<ApsPlan> plans = apsPlanRepository.currentActivePlans(agencyId, yearMonth.atEndOfMonth());
        List<ApsOrder> orders = apsOrderRepository.findByPeriod(agencyId, period);
        return plans.stream().map(plan -> processApsOrder(plan, orders, period)).collect(Collectors.toList());
    }

    private ApsOrder processApsOrder(ApsPlan plan, List<ApsOrder> orders, Integer period) {
        ApsOrder apsOrder = orders.stream().filter(order -> plan.equals(order.getApsPlan())).findAny().orElse(new ApsOrder());
        if (apsOrder.getId() == null) {
            apsOrder.setApsPlan(ApsPlan.of(plan.getId()));
            apsOrder.setPeriod(period);
            apsOrder.setLicense(plan.getLicense());
            apsOrder.setUnitPrice(plan.getUnitPrice());
            apsOrder.setVatRate(0d);
            apsOrder.setNumberOfUsers(0);
            apsOrder = apsOrderRepository.save(apsOrder);
        }
        return apsOrder;
    }

}
