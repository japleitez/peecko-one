package com.peecko.one.service;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.service.request.ApsOrderListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.utils.PeriodUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApsOrderService {
    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;

    public ApsOrderService(ApsPlanRepository apsPlanRepository, ApsOrderRepository apsOrderRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
    }

    public ApsOrder create(ApsOrder apsOrder) {
        return apsOrderRepository.save(apsOrder);
    }

    public ApsOrder update(ApsOrder apsOrder) {
        return apsOrderRepository.save(apsOrder);
    }

    public Optional<ApsOrder> partialUpdateApsOrder(ApsOrder apsOrder) {
        return apsOrderRepository
            .findById(apsOrder.getId())
            .map(existingApsOrder -> {
                if (apsOrder.getPeriod() != null) {
                    existingApsOrder.setPeriod(apsOrder.getPeriod());
                }
                if (apsOrder.getLicense() != null) {
                    existingApsOrder.setLicense(apsOrder.getLicense());
                }
                if (apsOrder.getUnitPrice() != null) {
                    existingApsOrder.setUnitPrice(apsOrder.getUnitPrice());
                }
                if (apsOrder.getVatRate() != null) {
                    existingApsOrder.setVatRate(apsOrder.getVatRate());
                }
                if (apsOrder.getNumberOfUsers() != null) {
                    existingApsOrder.setNumberOfUsers(apsOrder.getNumberOfUsers());
                }
                if (apsOrder.getInvoiceNumber() != null) {
                    existingApsOrder.setInvoiceNumber(apsOrder.getInvoiceNumber());
                }

                return existingApsOrder;
            })
            .map(apsOrderRepository::save);
    }

    public List<ApsOrderInfo> batchGenerate(Long agencyId, Integer period) {
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

    public List<ApsOrder> findBySearchRequest(ApsOrderListRequest request) {
        Specification<ApsOrder> spec = ApsOrderSpecs.agency(request.getAgencyId());
        if (StringUtils.hasText(request.getContract())) {
            spec = spec.and(ApsOrderSpecs.contractLike(request.getContract()));
        }
        if (StringUtils.hasText(request.getCustomer())) {
            spec = spec.and(ApsOrderSpecs.customerLike(request.getContract()));
        }
        if (Objects.nonNull(request.getPeriod())) {
            spec = spec.and(ApsOrderSpecs.period(request.getPeriod()));
        } else {
            if (Objects.nonNull(request.getStarts())) {
                spec = spec.and(ApsOrderSpecs.starts(request.getStarts()));
            }
            if (Objects.nonNull(request.getEnds())) {
                spec = spec.and(ApsOrderSpecs.ends(request.getEnds()));
            }
        }
        return apsOrderRepository.findAll(spec);
    }

    public void deleteById(Long id) {
        if (apsOrderRepository.existsById(id)) {
            apsOrderRepository.deleteById(id);
        }
    }

    public boolean notFound(Long id) {
        return !apsOrderRepository.existsById(id);
    }

    public Optional<ApsOrder> findById(Long id) {
        return apsOrderRepository.findById(id);
    }

    public List<ApsOrder> findAll() {
        return apsOrderRepository.findAll();
    }

}
