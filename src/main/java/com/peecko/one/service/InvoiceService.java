package com.peecko.one.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.peecko.one.domain.Invoice;
import com.peecko.one.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;

@Service
public class InvoiceService {
    private final Logger log = LoggerFactory.getLogger(ApsOrderService.class);
    private final InvoiceRepository invoiceRepository;
    private final PropertyService propertyService;

    public InvoiceService(InvoiceRepository invoiceRepository, PropertyService propertyService) {
        this.invoiceRepository = invoiceRepository;
        this.propertyService = propertyService;
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
        String filename = propertyService.resolveInvoicePathname(invoice);
        boolean done = generateInvoicePdf(invoice.getNumber(), filename);
        if (done) {
            invoice.setFilename(filename);
            invoiceRepository.save(invoice);
        }
    }

    private boolean generateInvoicePdf(String invoiceNumber, String filename) {
        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (DocumentException | FileNotFoundException e) {
            log.error("Cannot generate PDF Invoice " + invoiceNumber, e);
            return false;
        }
        String template = propertyService.getInvoiceTemplate();
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(template));
        } catch (IOException e) {
            log.error("Cannot generate Invoice PDF " + invoiceNumber, e);
        } finally {
            document.close();
        }
        return true;
    }


}
