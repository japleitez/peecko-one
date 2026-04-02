package com.peecko.one.service;

import com.peecko.one.domain.Invoice;
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

    public byte[] generatePdfInvoice(Invoice invoice) {
        String template = propertyService.getInvoiceTemplate();
        Map<String, Object> invoiceData = buildInvoiceData(invoice);
        String html = pdfService.generateContent(template, invoiceData);
        return pdfGeneratorService.generatePdfFromHtml(html);
    }

    private Map<String, Object> buildInvoiceData(Invoice i) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", i.getId());
        data.put("number", i.getNumber());
        data.put("dueDate", i.getDueDate());
        data.put("issued", i.getIssued());
        data.put("subtotal", i.getSubtotal());
        data.put("total", i.getTotal());
        data.put("vat", i.getVat());
        data.put("notes", i.getNotes());
        return data;
    }
}
