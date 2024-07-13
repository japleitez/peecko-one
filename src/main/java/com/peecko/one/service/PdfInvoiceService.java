package com.peecko.one.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;

import java.io.*;

@Service
public class PdfInvoiceService {
    private static final String HTML = "src/main/resources/sample.html";
    private final Logger log = LoggerFactory.getLogger(PdfInvoiceService.class);

    public void generate() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/html.pdf"));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML));
        document.close();
    }

    public boolean generateInvoice(String invoiceNumber, String filename) {
        Document document = new Document();

        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (DocumentException | FileNotFoundException e) {
            log.error("Cannot generate PDF Invoice " + invoiceNumber, e);
            return false;
        }
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML));
        } catch (IOException e) {
            log.error("Cannot generate PDF Invoice " + invoiceNumber, e);
        } finally {
            document.close();
        }
        return true;
    }
}
