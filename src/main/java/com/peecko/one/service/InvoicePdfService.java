package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.Invoice;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class InvoicePdfService {

    private final TemplateEngine templateEngine;
    private final InvoicePdfGeneratorService invoicePdfGeneratorService;

    public InvoicePdfService(TemplateEngine templateEngine, InvoicePdfGeneratorService invoicePdfGeneratorService) {
        this.templateEngine = templateEngine;
        this.invoicePdfGeneratorService = invoicePdfGeneratorService;
    }

    public byte[] generatePdfInvoice(Agency agency, Customer customer, Invoice invoice) {
        Map<String, Object> invoiceData = getInvoiceData(agency, customer, invoice);
        try {
            return invoicePdfGeneratorService.generate(invoiceData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getInvoiceData(Agency agency, Customer customer, Invoice invoice) {
        Map<String, Object> data = new HashMap<>();

        data.put(InvoiceField.INVOICE_NUMBER, invoice.getNumber());
        data.put(InvoiceField.INVOICE_ISSUE, invoice.getIssued());
        data.put(InvoiceField.INVOICE_DUE, invoice.getDueDate());

        data.put(InvoiceField.AGENCY_NAME, agency.getName());
        data.put(InvoiceField.AGENCY_ADDRESS_STREET, agency.getLine1());
        data.put(InvoiceField.AGENCY_ADDRESS_CITY, agency.getZip() + " " + agency.getCity());
        data.put(InvoiceField.AGENCY_ADDRESS_COUNTRY, agency.getCountry());

        data.put(InvoiceField.AGENCY_VAT_NUMBER, agency.getVatId());
        data.put(InvoiceField.AGENCY_BANK_IBAN, agency.getIban());
        data.put(InvoiceField.AGENCY_BANK_SWIFT, agency.getBank());

        data.put(InvoiceField.CLIENT_NAME, customer.getName());
        data.put(InvoiceField.CLIENT_ADDRESS_STREET, "tod: street");
        data.put(InvoiceField.CLIENT_ADDRESS_CITY, "todo: city");
        data.put(InvoiceField.CLIENT_ADDRESS_COUNTRY, "todo: country");
        data.put(InvoiceField.CLIENT_VAT_NUMBER, customer.getVatId());
        data.put(InvoiceField.CLIENT_CODE, customer.getCode());

        data.put(InvoiceField.DATE_FROM, "todo");
        data.put(InvoiceField.DATE_TO, "todo");

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

        data.put(InvoiceField.AGENCY_FOOTER_LINE1, "todo: footer 1");

        return data;
    }
}
