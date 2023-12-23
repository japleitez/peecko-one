package com.peecko.one.service;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.utils.PeriodUtils;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class ApsOrderService {
    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;

    public ApsOrderService(ApsPlanRepository apsPlanRepository, ApsOrderRepository apsOrderRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
    }

    public List<ApsOrderInfo> batchGenerate(Long agencyId, YearMonth yearMonth) {
        Integer period = PeriodUtils.getPeriod(yearMonth);
        List<ApsPlan> plans = apsPlanRepository.currentActivePlans(agencyId, yearMonth.atEndOfMonth());
        List<ApsOrder> orders = apsOrderRepository.findByPeriod(agencyId, period);
        return plans.stream().map(plan -> createOrderIfMissing(plan, orders, period)).toList();
    }

    private ApsOrderInfo createOrderIfMissing(ApsPlan apsPlan, List<ApsOrder> apsOrders, Integer period) {
        ApsOrder apsOrder = apsOrders.stream().filter(order -> apsPlan.equals(order.getApsPlan())).findAny().orElse(new ApsOrder());
        if (apsOrder.getId() == null) {
            apsOrder.setApsPlan(apsPlan);
            apsOrder.setPeriod(period);
            apsOrder.setLicense(apsPlan.getLicense());
            apsOrder.setUnitPrice(apsPlan.getUnitPrice());
            apsOrder.setVatRate(0d);
            apsOrder.setNumberOfUsers(0);
            apsOrder = apsOrderRepository.save(apsOrder);
        }
        return ApsOrderInfo.of(apsOrder);
    }

}
