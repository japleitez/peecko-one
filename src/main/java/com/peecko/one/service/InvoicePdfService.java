package com.peecko.one.service;

import com.peecko.one.domain.*;
import com.peecko.one.utils.EuroFormatter;
import com.peecko.one.utils.InstantUtils;
import com.peecko.one.utils.PeriodUtils;
import com.peecko.one.utils.PriceFormatter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class InvoicePdfService {

    private final InvoicePdfGeneratorService invoicePdfGeneratorService;
    private final CountryService countryService;
    private final MessageSource messageSource;

    public InvoicePdfService(
        InvoicePdfGeneratorService invoicePdfGeneratorService,
        CountryService countryService,
        MessageSource messageSource
    ) {
        this.invoicePdfGeneratorService = invoicePdfGeneratorService;
        this.countryService = countryService;
        this.messageSource = messageSource;
    }

    public byte[] generatePdfInvoice(Agency agency, Customer customer, Contact contact, Invoice invoice) {
        Country country = countryService.findCountryByCode(customer.getCountry());
        Locale locale = new Locale(country.getLanguage(), country.getLocale());
        Map<String, Object> invoiceData = getInvoiceData(agency, customer, contact, invoice);
        Map<String, String> labels = getInvoiceLabels(locale);
        try {
            return invoicePdfGeneratorService.generate(invoiceData, labels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getInvoiceData(Agency agency, Customer customer, Contact contact, Invoice invoice) {
        YearMonth yearMonth = PeriodUtils.parse(invoice.getPeriod());

        Country country = countryService.findCountryByCode(customer.getCountry());
        Locale locale = new Locale(country.getLanguage(), country.getLocale());
        Currency currency = Currency.getInstance(country.getCurrency());

        Map<String, Object> data = new HashMap<>();

        data.put(InvoiceField.INVOICE_NUMBER, invoice.getNumber());
        data.put(InvoiceField.INVOICE_ISSUE, InstantUtils.formatInstantToDate(invoice.getIssued()));
        data.put(InvoiceField.INVOICE_DUE, InstantUtils.formatLocalDateToDate(invoice.getDueDate()));

        data.put(InvoiceField.AGENCY_NAME, agency.getName());
        data.put(InvoiceField.AGENCY_ADDRESS_STREET, agency.getLine1());
        data.put(InvoiceField.AGENCY_ADDRESS_CITY, agency.getZip() + " " + agency.getCity());
        data.put(InvoiceField.AGENCY_ADDRESS_COUNTRY, countryService.getLocaleCountryName(agency.getCountry()));

        data.put(InvoiceField.AGENCY_VAT_NUMBER, agency.getVatId());
        data.put(InvoiceField.AGENCY_BANK_SWIFT, agency.getBankSwift());
        data.put(InvoiceField.AGENCY_BANK_ACCOUNT, agency.getBankAccount());
        data.put(InvoiceField.AGENCY_BANK_IBAN, agency.getIban());

        data.put(InvoiceField.CLIENT_NAME, customer.getName());
        data.put(InvoiceField.CLIENT_ADDRESS_STREET, contact.getLine1());
        data.put(InvoiceField.CLIENT_ADDRESS_CITY, contact.getZip() + " " + contact.getCity());
        data.put(InvoiceField.CLIENT_ADDRESS_COUNTRY, countryService.getLocaleCountryName(contact.getCountry()));
        data.put(InvoiceField.CLIENT_VAT_NUMBER, customer.getVatId());
        data.put(InvoiceField.CLIENT_CODE, customer.getCode());

        data.put(InvoiceField.DATE_FROM, PeriodUtils.getFirstDateAsString(yearMonth));
        data.put(InvoiceField.DATE_TO, PeriodUtils.getLastDateAsString(yearMonth));

        invoice
            .getInvoiceItems()
            .forEach(item -> {
                data.put(InvoiceField.ITEM_DESCRIPTION, item.getDescription());
                data.put(InvoiceField.ITEM_QUANTITY, item.getQuantity());
                data.put(InvoiceField.ITEM_UNIT_PRICE, PriceFormatter.formatPricePlain(item.getUnitPrice(), locale, currency));
                data.put(InvoiceField.ITEM_SUBTOTAL, PriceFormatter.formatPricePlain(item.getSubtotal(), locale, currency));
            });

        data.put(InvoiceField.INVOICE_VAT_RATE, invoice.getVatRate());
        data.put(InvoiceField.INVOICE_VAT, PriceFormatter.formatPricePlain(invoice.getVat(), locale, currency));
        data.put(InvoiceField.INVOICE_TOTAL, PriceFormatter.formatPrice(invoice.getTotal(), locale, currency));

        data.put(
            InvoiceField.AGENCY_FOOTER_LINE1,
            agency.getName() + " - Email: " + agency.getBillingEmail() + " - Phone: " + agency.getBillingPhone()
        );

        return data;
    }

    public Map<String, String> getInvoiceLabels(Locale locale) {
        Map<String, String> labels = new HashMap<>();
        String[] keys = {
            InvoiceLabel.TITLE,
            InvoiceLabel.FROM,
            InvoiceLabel.BILL_TO,
            InvoiceLabel.PERIOD,
            InvoiceLabel.CUSTOMER_ACCOUNT,
            InvoiceLabel.DESCRIPTION,
            InvoiceLabel.QUANTITY,
            InvoiceLabel.UNIT_PRICE,
            InvoiceLabel.AMOUNT,
            InvoiceLabel.TAX_RATE,
            InvoiceLabel.TOTAL_DUE,
            InvoiceLabel.VAT,
            InvoiceLabel.ACCOUNT,
            InvoiceLabel.ISSUE_DATE,
            InvoiceLabel.DUE_DATE,
        };
        for (String key : keys) {
            labels.put(key, messageSource.getMessage(key, null, locale));
        }
        return labels;
    }
}
