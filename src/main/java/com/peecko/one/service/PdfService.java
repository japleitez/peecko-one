package com.peecko.one.service;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Map;

@Service
public class PdfService {
    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public boolean generatePdf(String template, String filename, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(template, context);

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            HtmlConverter.convertToPdf(htmlContent, fos);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
        return true;
    }
}
