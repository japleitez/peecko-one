package com.peecko.one.service;

import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.InvoiceRepository;
import com.peecko.one.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class InvoicePdfService {
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final PdfService pdfService;
    private final PropertyService propertyService;
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);

    public InvoicePdfService(UserService userService, InvoiceRepository invoiceRepository, PdfService pdfService, PropertyService propertyService) {
        this.userService = userService;
        this.invoiceRepository = invoiceRepository;
        this.pdfService = pdfService;
        this.propertyService = propertyService;
    }

    public void batchInvoicePDF(String contract, Integer period) {
        if (StringUtils.hasText(contract)) {
            invoiceRepository.findByContractAndPeriod(contract, period).forEach(this::generatePDF);
        } else {
            Long agencyId = userService.getCurrentAgencyId();
            invoiceRepository.findByAgencyAndPeriod(agencyId, period).forEach(this::generatePDF);
        }
    }

    private void generatePDF(Invoice invoice) {
        log.info("generate invoice number" + invoice.getNumber());
        String template = propertyService.getInvoiceTemplate();
        String filename = propertyService.resolveInvoicePathname(invoice);
        Map<String, Object> data = buildInvoiceMapData(invoice);
        boolean generated = pdfService.generatePdf(template, filename, data);
        if (generated) {
            invoice.setFilename(filename);
            invoiceRepository.save(invoice);
        }
    }

    private Map<String, Object> buildInvoiceMapData(Invoice i) {
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
