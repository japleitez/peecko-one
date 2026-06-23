package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.domain.dto.ApsOrderInfo;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.domain.enumeration.ProductType;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPricingRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.repository.InvoiceItemRepository;
import com.peecko.one.repository.InvoiceRepository;
import com.peecko.one.service.request.InvoiceListRequest;
import com.peecko.one.service.specs.InvoiceSpecs;
import com.peecko.one.utils.PeriodUtils;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InvoiceService {

    private final UserService userService;
    private final ApsOrderRepository apsOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final CustomerRepository customerRepository;
    private final ApsPricingRepository apsPricingRepository;

    public InvoiceService(
        UserService userService,
        ApsOrderRepository apsOrderRepository,
        InvoiceRepository invoiceRepository,
        InvoiceItemRepository invoiceItemRepository,
        CustomerRepository customerRepository,
        ApsPricingRepository apsPricingRepository
    ) {
        this.userService = userService;
        this.apsOrderRepository = apsOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.customerRepository = customerRepository;
        this.apsPricingRepository = apsPricingRepository;
    }

    public List<Invoice> findAll(InvoiceListRequest request) {
        Long agencyId = userService.getCurrentAgencyId();
        Specification<Invoice> spec = InvoiceSpecs.agencyId(agencyId);
        if (Objects.nonNull(request.getCustomerId())) {
            spec = spec.and(InvoiceSpecs.customerId(request.getCustomerId()));
        }
        if (Objects.nonNull(request.getStarts())) {
            spec = spec.and(InvoiceSpecs.starts(request.getStarts()));
        }
        if (Objects.nonNull(request.getEnds())) {
            spec = spec.and(InvoiceSpecs.ends(request.getEnds()));
        }
        if (StringUtils.hasText(request.getNumber())) {
            spec = spec.and(InvoiceSpecs.numberLike(request.getNumber()));
        }
        if (request.isUnpaid()) {
            spec = spec.and(InvoiceSpecs.unpaid());
        }
        return invoiceRepository.findAll(spec);
    }

    public Invoice addInvoiceItem(Long invoiceId, InvoiceItem item) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
        invoice.addInvoiceItem(item);
        invoiceItemRepository.save(item);
        double subtotal = round(invoice.getInvoiceItems().stream().mapToDouble(InvoiceItem::getSubtotal).sum());
        double vatRate = invoice.getVatRate() != null ? invoice.getVatRate() : 0.0;
        double vat = round((subtotal * vatRate) / 100.0);
        double total = round(subtotal + vat);
        invoice.setSubtotal(subtotal);
        invoice.setVat(vat);
        invoice.setTotal(total);
        if (invoice.getPaid() != null) {
            invoice.setDiff(round(total - invoice.getPaid()));
        }
        return invoiceRepository.save(invoice);
    }

    public List<ApsOrderInfo> batchInvoiceForAgency(Long agencyId, Integer period) {
        return generateInvoices(apsOrderRepository.getByAgencyAndPeriodAndActive(agencyId, period));
    }

    public List<ApsOrderInfo> batchInvoiceForContract(String contract, Integer period) {
        return generateInvoices(apsOrderRepository.getByContractAndPeriodAndActive(contract, period));
    }

    private List<ApsOrderInfo> generateInvoices(List<ApsOrder> orders) {
        if (orders.isEmpty()) {
            return List.of();
        }
        return orders.stream().filter(ApsOrder::hasSubscribers).map(this::getOrCreateInvoice).toList();
    }

    private ApsOrderInfo getOrCreateInvoice(ApsOrder apsOrder) {
        if (apsOrder.getInvoice() == null) {
            InvoiceItem invoiceItem = generateInvoiceItem(apsOrder);
            Invoice invoice = new Invoice();
            invoice.setNumber(generateInvoiceNumber(apsOrder.getAgencyId(), apsOrder.getPeriod()));
            invoice.setDueDate(PeriodUtils.parsePeriodDay(apsOrder.getPeriod(), "09"));
            invoice.saleDate(PeriodUtils.parsePeriodDay(apsOrder.getPeriod(), "01"));
            invoice.setIssued(Instant.now());
            invoice.setAgencyId(apsOrder.getAgencyId());
            invoice.setPeriod(apsOrder.getPeriod());
            invoice.setCountry(apsOrder.getCountry());
            invoice.setCustomerId(apsOrder.getCustomerId());
            invoice.setApsPlanId(apsOrder.getApsPlan().getId());
            double subTotal = invoiceItem.getSubtotal();
            double vatRate = apsOrder.getVatRate();
            double vat = round((subTotal * vatRate) / 100.0);
            invoice.setVatRate(vatRate);
            invoice.setSubtotal(subTotal);
            invoice.setVat(vat);
            invoice.setTotal(round(subTotal + vat));
            invoice.addInvoiceItem(invoiceItem);
            Customer customer = customerRepository.getReferenceById(apsOrder.getCustomerId());
            invoice.setCustomerId(customer.getId());
            invoice.setCustomerVatId(customer.getVatId());
            apsOrder.setInvoice(invoice);
            invoiceRepository.save(invoice);
        }
        return ApsOrderInfo.of(apsOrder);
    }

    private String generateInvoiceNumber(Long agencyId, Integer period) {
        Long count = invoiceRepository.countByAgencyIdAndPeriod(agencyId, period) + 1;
        String yymm = String.valueOf(period).substring(2);
        return String.format("%d.%s%04d", agencyId, yymm, count);
    }

    private InvoiceItem generateInvoiceItem(ApsOrder apsOrder) {
        Double unitPrice = 0D;
        final ApsPlan apsPlan = apsOrder.getApsPlan();
        String description = "peecko app monthly subscription - plan " + apsPlan.getPricing().name().toLowerCase();
        final String country = apsOrder.getCountry();
        final Long customerId = apsOrder.getCustomerId();
        final Integer numberOfUsers = apsOrder.getNumberOfUsers();
        final PricingType pricingType = apsPlan.getPricing();
        if (PricingType.FIXED.equals(pricingType)) {
            unitPrice = apsPlan.getUnitPrice();
        } else {
            List<ApsPricing> apsPricings = apsPricingRepository.findByCountryAndCustomerIdAndNumberOfUsers(
                country,
                customerId,
                numberOfUsers
            );
            if (apsPricings.isEmpty()) {
                apsPricings = apsPricingRepository.findByCountryAndNumberOfUsers(country, numberOfUsers);
            }
            if (apsPricings.isEmpty()) {
                apsPricings = apsPricingRepository.findByCountryAndNumberOfUsers("LU", numberOfUsers);
            }
            if (!apsPricings.isEmpty()) {
                if (PricingType.WELLNESS.equals(pricingType)) {
                    unitPrice = apsPricings.get(0).getWellnessPrice();
                } else {
                    unitPrice = apsPricings.get(0).getFitnessPrice();
                }
            }
        }

        final DecimalFormat df = new DecimalFormat("#.##");
        double subTotal = Double.parseDouble(df.format(numberOfUsers * unitPrice));

        InvoiceItem item = new InvoiceItem();
        item.type(ProductType.APP);
        item.description(description);
        item.quantity(numberOfUsers);
        item.unitPrice(unitPrice);
        item.subtotal(subTotal);
        return item;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
