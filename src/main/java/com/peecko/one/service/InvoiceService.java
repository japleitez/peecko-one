package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.domain.enumeration.ProductType;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.repository.ApsPricingRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.repository.InvoiceRepository;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.utils.PeriodUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;

@Service
public class InvoiceService {
    private final ApsOrderRepository apsOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ApsPricingRepository apsPricingRepository;

    private final static Long BASE_CUSTOMER_ID = 1L;

    public InvoiceService(ApsOrderRepository apsOrderRepository, InvoiceRepository invoiceRepository, CustomerRepository customerRepository, ApsPricingRepository apsPricingRepository) {
        this.apsOrderRepository = apsOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.apsPricingRepository = apsPricingRepository;
    }

    public List<ApsOrderInfo> batchInvoice(Long agencyId, String contract, Integer period) {
        List<ApsOrder> orders;
        if (StringUtils.hasText(contract)) {
            orders = apsOrderRepository.getByContractAndPeriodAndActive(contract, period);
        } else {
            orders = apsOrderRepository.getByAgencyAndPeriodAndActive(agencyId, period);
        }
        return orders.stream()
            .filter(ApsOrder::hasSubscribers)
            .map(apsOrder -> this.getOrCreateInvoice(agencyId, apsOrder.getId()))
            .toList();
    }

    private ApsOrderInfo getOrCreateInvoice(Long agencyId, Long apsOrderId) {
        ApsOrder apsOrder = apsOrderRepository.getReferenceById(apsOrderId);
        Invoice invoice = null;
        if (apsOrder.getInvoice() == null) {
            InvoiceItem invoiceItem = generateInvoiceItem(agencyId, apsOrder);
            invoice = new Invoice();
            invoice.setNumber(generateInvoiceNumber(agencyId, apsOrder.getPeriod()));
            invoice.setDueDate(PeriodUtils.parsePeriodDay(apsOrder.getPeriod(), "09"));
            invoice.saleDate(PeriodUtils.parsePeriodDay(apsOrder.getPeriod(), "01"));
            invoice.setIssued(Instant.now());
            invoice.setAgencyId(agencyId);
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
        if (invoice != null) {
            apsOrderRepository.save(apsOrder);
        }
        return ApsOrderInfo.of(apsOrder);
    }

    private String generateInvoiceNumber(Long agencyId, Integer period) {
        Long count = invoiceRepository.countByAgencyIdAndPeriod(agencyId, period);
        return  "PCK" + period + String.format("%05d", count);
    }

    private InvoiceItem generateInvoiceItem(Long agencyId, ApsOrder apsOrder) {
        Double unitPrice = 0D;
        String description = apsOrder.getNumberOfUsers() + " peecko app license(s)";
        String country = apsOrder.getCountry();
        Long customerId = apsOrder.getCustomerId();
        Integer numberOfUsers = apsOrder.getNumberOfUsers();
        PricingType pricingType = apsOrder.getApsPlan().getPricing();
        if (PricingType.FIXED.equals(pricingType)) {
            unitPrice = apsOrder.getApsPlan().getUnitPrice();
        } else if (PricingType.BRACKET.equals(pricingType)) {
            List<ApsPricing> apsPricings = apsPricingRepository.findByCountryAndCustomerIdAndNumberOfUsers(country, customerId, numberOfUsers);
            if (apsPricings.isEmpty()) {
                apsPricings = apsPricingRepository.findByCountryAndCustomerIdAndNumberOfUsers(country, BASE_CUSTOMER_ID, numberOfUsers);
            }
            if (!apsPricings.isEmpty()) {
                unitPrice = apsPricings.get(0).getUnitPrice();
            }
        }

        final DecimalFormat df = new DecimalFormat("#.##");
        double subTotal = Double.parseDouble(df.format(numberOfUsers * unitPrice));
        double vat =   Double.parseDouble(df.format (subTotal * apsOrder.getVatRate() / 100.0));
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
