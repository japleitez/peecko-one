package com.peecko.one.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateContent(String template, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(template, context);
    }
}
