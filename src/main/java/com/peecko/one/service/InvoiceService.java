package com.peecko.one.service;

import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);
    private final InvoiceRepository invoiceRepository;
    private final PdfInvoiceService pdfInvoiceService;

    public InvoiceService(InvoiceRepository invoiceRepository, PdfInvoiceService pdfInvoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.pdfInvoiceService = pdfInvoiceService;
    }

    public List<Invoice> batchInvoicePDF(Long agencyId, Integer period) {
        return invoiceRepository
            .findByAgencyAndPeriod(agencyId, period)
            .stream()
            .map(this::generatePDF)
            .toList();
    }

    private Invoice generatePDF(Invoice invoice) {
        log.info("generate invoice number" + invoice.getNumber());
        pdfInvoiceService.generateInvoice(invoice.getNumber(), "src/" + invoice.getNumber() + ".pdf");
        return invoice;
    }

}
