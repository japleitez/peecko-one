package com.peecko.one.service;

public final class InvoiceField {

    private InvoiceField() {}

    // ── Header ────────────────────────────────────────────────────────────────
    public static final String INVOICE_NUMBER = "invoiceNumber";
    public static final String INVOICE_ISSUE = "invoiceIssue";
    public static final String INVOICE_DUE = "invoiceDue";

    // ── Agency (From) ─────────────────────────────────────────────────────────
    public static final String AGENCY_NAME = "agencyName";
    public static final String AGENCY_ADDRESS_STREET = "agencyAddressStreet";
    public static final String AGENCY_ADDRESS_CITY = "agencyAddressCity";
    public static final String AGENCY_ADDRESS_COUNTRY = "agencyAddressCountry";
    public static final String AGENCY_VAT_NUMBER = "agencyVatNumber";
    public static final String AGENCY_BANK_IBAN = "agencyBankIban";
    public static final String AGENCY_BANK_SWIFT = "agencyBankSwift";
    public static final String AGENCY_BANK_ACCOUNT = "agencyBankAccount";
    public static final String AGENCY_FOOTER_LINE1 = "agencyFooterLine1";

    // ── Client (Bill To) ──────────────────────────────────────────────────────
    public static final String CLIENT_NAME = "clientName";
    public static final String CLIENT_ADDRESS_STREET = "clientAddressStreet";
    public static final String CLIENT_ADDRESS_CITY = "clientAddressCity";
    public static final String CLIENT_ADDRESS_COUNTRY = "clientAddressCountry";
    public static final String CLIENT_VAT_NUMBER = "clientVatNumber";
    public static final String CLIENT_CODE = "clientCode";

    // ── Period ────────────────────────────────────────────────────────────────
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";

    // ── Line Items ────────────────────────────────────────────────────────────
    public static final String ITEMS = "items";
    public static final String ITEM_DESCRIPTION = "itemDescription";
    public static final String ITEM_QUANTITY = "itemQuantity";
    public static final String ITEM_UNIT_PRICE = "itemUnitPrice";
    public static final String ITEM_SUBTOTAL = "itemSubtotal";

    // ── Totals ────────────────────────────────────────────────────────────────
    public static final String INVOICE_VAT_RATE = "invoiceVatRate";
    public static final String INVOICE_VAT = "invoiceVat";
    public static final String INVOICE_TOTAL = "invoiceTotal";
}
