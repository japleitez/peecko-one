package com.peecko.one.service;

import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);
    private final InvoiceRepository invoiceRepository;
    private final PdfInvoiceService pdfInvoiceService;

    public InvoiceService(InvoiceRepository invoiceRepository, PdfInvoiceService pdfInvoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.pdfInvoiceService = pdfInvoiceService;
    }

    public void batchInvoicePDF(Long agencyId, String contract, Integer period) {
        if (StringUtils.hasText(contract)) {
            invoiceRepository.findByContractAndPeriod(contract, period).forEach(this::generatePDF);
        } else {
            invoiceRepository.findByAgencyAndPeriod(agencyId, period).forEach(this::generatePDF);
        }
    }

    private void generatePDF(Invoice invoice) {
        log.info("generate invoice number" + invoice.getNumber());
        String filename = generateFilename(invoice);
        boolean done = pdfInvoiceService.generateInvoice(invoice.getNumber(), filename);
        if (done) {
            invoice.setFilename(filename);
            invoiceRepository.save(invoice);
        }
    }

    private String generateFilename(Invoice invoice) {
        String filename = "src/" + invoice.getAgencyId() + "/" + invoice.getPeriod() + "/" + invoice.getNumber() + ".pdf";
        return "/Users/jpleitez/s3/" + invoice.getNumber() + ".pdf";
    }

}
