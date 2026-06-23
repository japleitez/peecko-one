package com.peecko.one.service;

import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private static final String INVOICE_TEMPLATE = "invoice_template.html";

    public String getInvoiceTemplate() {
        return INVOICE_TEMPLATE;
    }
}
