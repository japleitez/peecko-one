package com.peecko.one.service;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.utils.PeriodUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

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
        LocalDate endOfMonth = PeriodUtils.getYearMonth(period).atEndOfMonth();
        List<ApsPlan> plans = apsPlanRepository.currentPaidActivePlans(agencyId);
        List<ApsOrder> orders = apsOrderRepository.findByAgencyAndPeriod(agencyId, period);
        return plans.stream()
            .filter(p -> betweenPlanValidity(endOfMonth, p.getStarts(), p.getEnds()))
            .map(p -> getOrCreateApsOrder(p, orders, period))
            .toList();
    }

    private ApsOrderInfo getOrCreateApsOrder(ApsPlan apsPlan, List<ApsOrder> apsOrders, Integer period) {
        ApsOrder apsOrder = apsOrders.stream().filter(o -> apsPlan.equals(o.getApsPlan())).findAny().orElse(new ApsOrder());
        if (apsOrder.getId() == null) {
            apsOrder.setApsPlan(apsPlan);
            apsOrder.setPeriod(period);
            apsOrder.setLicense(apsPlan.getLicense());
            apsOrder.setUnitPrice(apsPlan.getUnitPrice());
            apsOrder.setVatRate(0d); //TODO plan should have vat rate to manage potential exceptional rates per customer
            apsOrder.setNumberOfUsers(0);
            apsOrder = apsOrderRepository.save(apsOrder);
        }
        return ApsOrderInfo.of(apsOrder);
    }

    private boolean betweenPlanValidity(LocalDate endOfMonth, LocalDate starts, LocalDate ends) {
        if (endOfMonth.isBefore(starts)) {
            return false;
        }
        return ends == null || endOfMonth.isEqual(ends) || endOfMonth.isBefore(ends);
    }

}
