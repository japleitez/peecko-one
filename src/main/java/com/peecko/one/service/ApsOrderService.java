package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.repository.*;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.service.request.ApsOrderListRequest;
import com.peecko.one.service.specs.ApsOrderSpecs;
import com.peecko.one.utils.PeriodUtils;
import com.peecko.one.web.rest.ApsOrderResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ApsOrderService {

    private final static Long BASE_CUSTOMER_ID = 1L;
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);

    private final ApsPlanRepository apsPlanRepository;
    private final ApsOrderRepository apsOrderRepository;

    private final InvoiceItemRepository invoiceItemRepository;

    private final InvoiceRepository invoiceRepository;

    private final ApsPricingRepository apsPricingRepository;

    public ApsOrderService(ApsPlanRepository apsPlanRepository, ApsOrderRepository apsOrderRepository, InvoiceItemRepository invoiceItemRepository, InvoiceRepository invoiceRepository, ApsPricingRepository apsPricingRepository) {
        this.apsPlanRepository = apsPlanRepository;
        this.apsOrderRepository = apsOrderRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.apsPricingRepository = apsPricingRepository;
    }

    public ApsOrder create(ApsOrder apsOrder) {
        Optional<ApsPlan> optionalApsPlan = apsPlanRepository.findById(apsOrder.getApsPlan().getId());
        if (optionalApsPlan.isPresent()) {
            ApsPlan apsPlan = optionalApsPlan.get();
            apsOrder.setCustomerId(apsPlan.getCustomer().getId());
            apsOrder.setCountry(apsPlan.getCustomer().getCountry());
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

    public List<ApsOrder> findBySearchRequest(ApsOrderListRequest request) {
        Specification<ApsOrder> spec = ApsOrderSpecs.agency(request.getAgencyId());
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


    public List<ApsOrderInfo> batchOrders(Long agencyId, Integer period) {
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
            apsOrder.setVatRate(apsPlan.getCustomer().getVatRate());
            apsOrder.setNumberOfUsers(0);
            apsOrder.setInvoiceNumber(null);
            apsOrder.setCustomerId(apsPlan.getCustomer().getId());
            apsOrder.setCountry(apsPlan.getCustomer().getCountry());
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

    public List<ApsOrderInfo> batchInvoice(Long agencyId, Integer period) {
        return apsOrderRepository.findByAgencyAndPeriod(agencyId, period)
            .stream()
            .map(apsOrder -> this.getOrCreateInvoice(agencyId, apsOrder))
            .toList();
    }

    private ApsOrderInfo getOrCreateInvoice(Long agencyId, ApsOrder apsOrder) {
        if (apsOrder.getInvoices().isEmpty()) {
            Long count = invoiceRepository.countByAgencyIdAndPeriod(agencyId, apsOrder.getPeriod());
            String invoiceNumber = "PCK" + apsOrder.getPeriod() + String.format("%05d", count);
            Invoice invoice = new Invoice();
            invoice.setApsOrder(apsOrder);
            invoice.setNumber(invoiceNumber);
            invoice.setIssued(Instant.now());
            invoice.setDueDate(LocalDate.now().plusDays(7));
            invoice.saleDate(LocalDate.now());
            invoice.setSubtotal(0D);
            invoice.setVat(0D);
            invoice.setVatRate(apsOrder.getVatRate());
            invoice.setTotal(0D);
            invoice.setAgencyId(agencyId);
            invoice.setCountry(apsOrder.getCountry());
            invoice.setCustomerId(apsOrder.getCustomerId());
            invoice.setApsPlanId(apsOrder.getApsPlan().getId());
            invoiceRepository.save(invoice);
            invoiceRepository.flush();
            apsOrder.addInvoice(invoice);
            apsOrder.setInvoiceNumber(invoiceNumber);
            apsOrder = apsOrderRepository.save(apsOrder);
        }
        return ApsOrderInfo.of(apsOrder);
    }

    private Double resolveUnitPrice(ApsOrder apsOrder) {
        Double result = 0D;
        PricingType pricingType = apsOrder.getApsPlan().getPricing();
        if (PricingType.FIXED.equals(pricingType)) {
            result = apsOrder.getApsPlan().getUnitPrice();
        } else if (PricingType.BRACKET.equals(pricingType)) {
            result = findUnitPrice(apsOrder.getCustomerId(), apsOrder.getNumberOfUsers());
        }
        return result;
    }

    private Double findUnitPrice(Long customerId, Integer numberOfUsers) {
        //TODO need to add country in filter
        Double unitPrice = 0D;
        List<ApsPricing> apsPricings = apsPricingRepository.findByCustomerIdAndNumberOfUsers(customerId, numberOfUsers);
        if (apsPricings.isEmpty()) {
            apsPricings = apsPricingRepository.findByCustomerIdAndNumberOfUsers(BASE_CUSTOMER_ID, numberOfUsers);
        }
        if (!apsPricings.isEmpty()) {
            unitPrice = apsPricings.get(0).getUnitPrice();
        }
        return unitPrice;
    }

}
