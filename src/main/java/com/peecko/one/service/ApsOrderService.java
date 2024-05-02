package com.peecko.one.service;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsMembershipRepository;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.service.request.ApsOrderListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.utils.PeriodUtils;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
public class ApsOrderService {
    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;

    private final ApsMembershipRepository apsMembershipRepository;

    public ApsOrderService(ApsPlanRepository apsPlanRepository, ApsOrderRepository apsOrderRepository, ApsMembershipRepository apsMembershipRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
        this.apsMembershipRepository = apsMembershipRepository;
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

    public List<ApsOrder> findByListRequest(ApsOrderListRequest req) {
        Specification<ApsOrder> spec = ApsOrderSpecs.agency(req.getAgencyId());
        if (StringUtils.hasText(req.getApsPlanContract())) {
            spec = spec.and(ApsOrderSpecs.contract(req.getApsPlanContract()));
        } else if (Objects.nonNull(req.getCustomerId())) {
            spec = spec.and(ApsOrderSpecs.customer(req.getCustomerId()));
        }
        if (Objects.nonNull(req.getPeriod())) {
            spec = spec.and(ApsOrderSpecs.period(req.getPeriod()));
        } else {
            if (Objects.nonNull(req.getStartPeriod())) {
                spec.and(ApsOrderSpecs.startPeriod(req.getStartPeriod()));
            }
            if (Objects.nonNull(req.getEndPeriod())) {
                spec.and(ApsOrderSpecs.endPeriod(req.getEndPeriod()));
            }
        }
        return apsOrderRepository.findAll(spec);
    }

}
