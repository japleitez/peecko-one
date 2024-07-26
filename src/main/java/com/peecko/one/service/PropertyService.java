package com.peecko.one.service;

import com.peecko.one.domain.Invoice;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private static final String INVOICE_TEMPLATE = "invoice.html";
    private static final String INVOICE_PATH = "/Users/jpleitez/s3";

    public String getInvoiceTemplate() {
        return INVOICE_TEMPLATE;
    }

    public String getInvoicePath() {
        return INVOICE_PATH;
    }

    public String resolveInvoicePathname(Invoice invoice) {
        return resolveInvoicePathname(invoice.getNumber());
    }

    public String resolveInvoicePathname(String invoiceNumber) {
        return getInvoicePath() + "/" + invoiceNumber + ".pdf";
    }

}
