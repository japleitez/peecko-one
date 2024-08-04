package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.repository.*;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.service.request.ApsOrderListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.utils.PeriodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);
    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;
    private final UserService userService;

    public ApsOrderService(
            ApsPlanRepository apsPlanRepository,
            ApsOrderRepository apsOrderRepository, UserService userService) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
        this.userService = userService;
    }

    public ApsOrder create(ApsOrder apsOrder) {
        Optional<ApsPlan> optionalApsPlan = apsPlanRepository.findById(apsOrder.getApsPlan().getId());
        if (optionalApsPlan.isPresent()) {
            ApsPlan apsPlan = optionalApsPlan.get();
            apsOrder.setCustomerId(apsPlan.getCustomer().getId());
            apsOrder.setCountry(apsPlan.getCustomer().getCountry());
            apsOrder.setAgencyId(apsPlan.getCustomer().getAgency().getId());
        }
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
                return existingApsOrder;
            })
            .map(apsOrderRepository::save);
    }

    public List<ApsOrder> findAll(ApsOrderListRequest request) {
        Long agencyId = userService.getCurrentAgencyId();
        Specification<ApsOrder> spec = ApsOrderSpecs.agency(agencyId);
        if (StringUtils.hasText(request.getContract())) {
            spec = spec.and(ApsOrderSpecs.contractLike(request.getContract()));
        }
        if (StringUtils.hasText(request.getCustomer())) {
            spec = spec.and(ApsOrderSpecs.customerLike(request.getCustomer()));
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

    public List<ApsOrderInfo> batchOrders(Integer period, String contract) {
        YearMonth yearMonth = PeriodUtils.getYearMonth(period);
        LocalDate periodEnds = yearMonth.atEndOfMonth();
        LocalDate periodStarts = yearMonth.atDay(1);
        List<ApsOrder> orders;
        List<ApsPlan> plans;
        if (StringUtils.hasText(contract)) {
            orders = apsOrderRepository.getByContactAndPeriod(contract, period);
            plans = apsPlanRepository.getByContractAndDatesAndActive(contract, periodStarts, periodEnds);
        } else {
            Long agencyId = userService.getCurrentAgencyId();
            orders = apsOrderRepository.getByAgencyAndPeriod(agencyId, period);
            plans = apsPlanRepository.getByAgencyAndDatesAndActive(agencyId, periodStarts, periodEnds);
        }
        return plans.stream().map(p -> getOrCreateApsOrder(p, orders, period)).toList();
    }

    private ApsOrderInfo getOrCreateApsOrder(ApsPlan apsPlan, List<ApsOrder> apsOrders, Integer period) {
        ApsOrder apsOrder = apsOrders.stream().filter(o -> apsPlan.equals(o.getApsPlan())).findAny().orElse(new ApsOrder());
        if (apsOrder.getId() == null) {
            apsOrder.setApsPlan(apsPlan);
            apsOrder.setPeriod(period);
            apsOrder.setLicense(apsPlan.getLicense());
            apsOrder.setUnitPrice(apsPlan.getUnitPrice());
            apsOrder.setVatRate(apsPlan.getCustomer().getVatRate());
            apsOrder.setNumberOfUsers(0);
            apsOrder.setInvoiceNumber(null);
            apsOrder.setCustomerId(apsPlan.getCustomer().getId());
            apsOrder.setCountry(apsPlan.getCustomer().getCountry());
            apsOrder.setAgencyId(apsPlan.getCustomer().getId());
            apsOrder = apsOrderRepository.save(apsOrder);
        }
        return ApsOrderInfo.of(apsOrder);
    }

}
