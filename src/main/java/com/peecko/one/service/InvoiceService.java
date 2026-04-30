package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.domain.dto.ApsOrderInfo;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.domain.enumeration.ProductType;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPricingRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.repository.InvoiceRepository;
import com.peecko.one.utils.PeriodUtils;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final UserService userService;
    private final ApsOrderRepository apsOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ApsPricingRepository apsPricingRepository;

    public InvoiceService(
        UserService userService,
        ApsOrderRepository apsOrderRepository,
        InvoiceRepository invoiceRepository,
        CustomerRepository customerRepository,
        ApsPricingRepository apsPricingRepository
    ) {
        this.userService = userService;
        this.apsOrderRepository = apsOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.apsPricingRepository = apsPricingRepository;
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
            invoice.setSubtotal(invoiceItem.getSubtotal());
            invoice.setVat(invoiceItem.getVat());
            invoice.setTotal(invoiceItem.getTotal());
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
        Long count = invoiceRepository.countByAgencyIdAndPeriod(agencyId, period);
        return "PCK" + period + String.format("%03d", count);
    }

    private InvoiceItem generateInvoiceItem(ApsOrder apsOrder) {
        Double unitPrice = 0D;
        final ApsPlan apsPlan = apsOrder.getApsPlan();
        String description = "Monthly b2b subscription to peecko app - plan " + apsPlan.getPricing().name().toLowerCase();
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
        double vat = Double.parseDouble(df.format((subTotal * apsOrder.getVatRate()) / 100.0));
        double total = subTotal + vat;

        InvoiceItem item = new InvoiceItem();
        item.type(ProductType.APP);
        item.quantity(numberOfUsers);
        item.unitPrice(unitPrice);
        item.subtotal(subTotal);
        item.vatRate(apsOrder.getVatRate());
        item.vat(vat);
        item.total(total);
        item.description(description);
        return item;
    }
}
