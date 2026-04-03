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
        return pdfGeneratorService.generatePdfFromHtml(html);
    }

    private Map<String, Object> buildInvoiceData(Agency agency, Customer customer, Invoice invoice) {
        Map<String, Object> data = new HashMap<>();

        data.put("invoice.number", invoice.getNumber());
        data.put("invoice.issue", invoice.getIssued());
        data.put("invoice.due", invoice.getDueDate());

        data.put("agency.name", agency.getName());
        data.put("agency.address.street", agency.getLine1());
        data.put("agency.address.city", agency.getZip() + " " + agency.getCity());
        data.put("agency.address.country", agency.getCountry());

        data.put("agency.vat.number", agency.getVatId());
        data.put("agency.bank.iban", agency.getIban());
        data.put("agency.bank.swift", agency.getBank());

        data.put("client.name", customer.getName());
        data.put("client.address.street", "todo: street");
        data.put("client.address.city", "todo: city");
        data.put("client.address.country", "todo: country");
        data.put("client.vat.number", customer.getVatId());
        data.put("client.code", customer.getCode());

        data.put("data.from", "todo: dd/MM/yyyy");
        data.put("date.to", "todo: dd/MM/yyyy");

        invoice
            .getInvoiceItems()
            .forEach(item -> {
                data.put("item.description", item.getDescription());
                data.put("item.quantity", item.getQuantity());
                data.put("item.unit.price", item.getUnitPrice());
                data.put("item.subtotal", item.getSubtotal());

                data.put("invoice.vat.rate", item.getVatRate());
                data.put("invoice.vat", item.getVat());
                data.put("invoice.total", item.getTotal());
            });

        data.put("agency.footer.line1", "todo: footer 1");
        data.put("agency.footer.line2", "todo: footer 2");

        return data;
    }
}
