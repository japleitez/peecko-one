package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.utils.InstantUtils;
import com.peecko.one.utils.PeriodUtils;
import java.io.IOException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class InvoicePdfService {

    private final TemplateEngine templateEngine;
    private final InvoicePdfGeneratorService invoicePdfGeneratorService;

    public InvoicePdfService(TemplateEngine templateEngine, InvoicePdfGeneratorService invoicePdfGeneratorService) {
        this.templateEngine = templateEngine;
        this.invoicePdfGeneratorService = invoicePdfGeneratorService;
    }

    public byte[] generatePdfInvoice(Agency agency, Customer customer, Contact contact, Invoice invoice) {
        Map<String, Object> invoiceData = getInvoiceData(agency, customer, contact, invoice);
        try {
            return invoicePdfGeneratorService.generate(invoiceData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getInvoiceData(Agency agency, Customer customer, Contact contact, Invoice invoice) {
        YearMonth yearMonth = PeriodUtils.parse(invoice.getPeriod());

        Map<String, Object> data = new HashMap<>();

        data.put(InvoiceField.INVOICE_NUMBER, invoice.getNumber());
        data.put(InvoiceField.INVOICE_ISSUE, InstantUtils.formatInstantToDate(invoice.getIssued()));
        data.put(InvoiceField.INVOICE_DUE, InstantUtils.formatLocalDateToDate(invoice.getDueDate()));

        data.put(InvoiceField.AGENCY_NAME, agency.getName());
        data.put(InvoiceField.AGENCY_ADDRESS_STREET, agency.getLine1());
        data.put(InvoiceField.AGENCY_ADDRESS_CITY, agency.getZip() + " " + agency.getCity());
        data.put(
            InvoiceField.AGENCY_ADDRESS_COUNTRY,
            Country.fromCode(agency.getCountry()).map(Country::toString).orElse(agency.getCountry())
        );

        data.put(InvoiceField.AGENCY_VAT_NUMBER, agency.getVatId());
        data.put(InvoiceField.AGENCY_BANK_IBAN, agency.getIban());
        data.put(InvoiceField.AGENCY_BANK_SWIFT, agency.getBank());

        data.put(InvoiceField.CLIENT_NAME, customer.getName());
        data.put(InvoiceField.CLIENT_ADDRESS_STREET, contact.getLine1());
        data.put(InvoiceField.CLIENT_ADDRESS_CITY, contact.getZip() + " " + contact.getCity());
        data.put(
            InvoiceField.CLIENT_ADDRESS_COUNTRY,
            Country.fromCode(contact.getCountry()).map(Country::toString).orElse(contact.getCountry())
        );
        data.put(InvoiceField.CLIENT_VAT_NUMBER, customer.getVatId());
        data.put(InvoiceField.CLIENT_CODE, customer.getCode());

        data.put(InvoiceField.DATE_FROM, PeriodUtils.getFirstDateAsString(yearMonth));
        data.put(InvoiceField.DATE_TO, PeriodUtils.getLastDateAsString(yearMonth));

        invoice
            .getInvoiceItems()
            .forEach(item -> {
                data.put(InvoiceField.ITEM_DESCRIPTION, item.getDescription());
                data.put(InvoiceField.ITEM_QUANTITY, item.getQuantity());
                data.put(InvoiceField.ITEM_UNIT_PRICE, item.getUnitPrice());
                data.put(InvoiceField.ITEM_SUBTOTAL, item.getSubtotal());

                data.put(InvoiceField.INVOICE_VAT_RATE, item.getVatRate());
                data.put(InvoiceField.INVOICE_VAT, item.getVat());
                data.put(InvoiceField.INVOICE_TOTAL, item.getTotal());
            });

        data.put(
            InvoiceField.AGENCY_FOOTER_LINE1,
            agency.getName() + " - Email: " + agency.getBillingEmail() + " - Phone: " + agency.getBillingPhone()
        );

        return data;
    }
}
