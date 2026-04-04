package com.peecko.one.service;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.Invoice;
import com.peecko.one.domain.InvoiceItem;
import com.peecko.one.repository.InvoiceRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoicePdfService {

    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final PdfService pdfService;
    private final PropertyService propertyService;
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);
    private final PdfGeneratorService pdfGeneratorService;

    public InvoicePdfService(
        UserService userService,
        InvoiceRepository invoiceRepository,
        PdfService pdfService,
        PropertyService propertyService,
        PdfGeneratorService pdfGeneratorService
    ) {
        this.userService = userService;
        this.invoiceRepository = invoiceRepository;
        this.pdfService = pdfService;
        this.propertyService = propertyService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public byte[] generatePdfInvoice(Agency agency, Customer customer, Invoice invoice) {
        String template = propertyService.getInvoiceTemplate();
        Map<String, Object> invoiceData = buildInvoiceData(agency, customer, invoice);
        String html = pdfService.generateContent(template, invoiceData);
        // Clean the HTML for strict XML parsers
        html = html.trim().replaceAll("(?s)^\\s*<!doctype html>", "<!DOCTYPE html>");
        // Force proper doctype and remove any BOM
        if (html.startsWith("\uFEFF")) {
            html = html.substring(1);
        }
        return pdfGeneratorService.generatePdfFromHtml(html);
    }

    private Map<String, Object> buildInvoiceData(Agency agency, Customer customer, Invoice invoice) {
        Map<String, Object> data = new HashMap<>();

        data.put("invoiceNumber", invoice.getNumber());
        data.put("invoiceIssue", invoice.getIssued());
        data.put("invoiceDue", invoice.getDueDate());

        data.put("agencyName", agency.getName());
        data.put("agencyAddressStreet", agency.getLine1());
        data.put("agencyAddressCity", agency.getZip() + " " + agency.getCity());
        data.put("agencyAddressCountry", agency.getCountry());

        data.put("agencyVatNumber", agency.getVatId());
        data.put("agencyBankIban", agency.getIban());
        data.put("agencyBankSwift", agency.getBank());

        data.put("clientName", customer.getName());
        data.put("clientAddressStreet", "todo: street");
        data.put("clientAddressCity", "todo: city");
        data.put("clientAddressCountry", "todo: country");
        data.put("clientVatNumber", customer.getVatId());
        data.put("clientCode", customer.getCode());

        data.put("dataFrom", "todo");
        data.put("dateTo", "todo");

        invoice
            .getInvoiceItems()
            .forEach(item -> {
                data.put("itemDescription", item.getDescription());
                data.put("itemQuantity", item.getQuantity());
                data.put("itemUnitPrice", item.getUnitPrice());
                data.put("itemSubtotal", item.getSubtotal());

                data.put("invoiceVatRate", item.getVatRate());
                data.put("invoiceVat", item.getVat());
                data.put("invoiceTotal", item.getTotal());
            });

        data.put("agencyFooterLine1", "todo: footer 1");
        data.put("agencyFooterLine2", "todo: footer 2");

        return data;
    }
}
