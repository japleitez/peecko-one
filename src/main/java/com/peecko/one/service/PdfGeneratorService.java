package com.peecko.one.service;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

@Service
public class PdfGeneratorService {

    public byte[] generatePdfFromHtml(String htmlContent) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Parse the HTML string into a DOM Document
            // Flying Saucer requires well-formed XHTML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(htmlContent)));

            // Render the DOM document to PDF
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(document, null);
            renderer.layout();
            renderer.createPDF(baos);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF from HTML", e);
        }
    }
}
