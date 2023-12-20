package com.peecko.one.domain;

import static com.peecko.one.domain.ApsOrderTestSamples.*;
import static com.peecko.one.domain.InvoiceItemTestSamples.*;
import static com.peecko.one.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void invoiceItemTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        InvoiceItem invoiceItemBack = getInvoiceItemRandomSampleGenerator();

        invoice.addInvoiceItem(invoiceItemBack);
        assertThat(invoice.getInvoiceItems()).containsOnly(invoiceItemBack);
        assertThat(invoiceItemBack.getInvoice()).isEqualTo(invoice);

        invoice.removeInvoiceItem(invoiceItemBack);
        assertThat(invoice.getInvoiceItems()).doesNotContain(invoiceItemBack);
        assertThat(invoiceItemBack.getInvoice()).isNull();

        invoice.invoiceItems(new HashSet<>(Set.of(invoiceItemBack)));
        assertThat(invoice.getInvoiceItems()).containsOnly(invoiceItemBack);
        assertThat(invoiceItemBack.getInvoice()).isEqualTo(invoice);

        invoice.setInvoiceItems(new HashSet<>());
        assertThat(invoice.getInvoiceItems()).doesNotContain(invoiceItemBack);
        assertThat(invoiceItemBack.getInvoice()).isNull();
    }

    @Test
    void apsOrderTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        ApsOrder apsOrderBack = getApsOrderRandomSampleGenerator();

        invoice.setApsOrder(apsOrderBack);
        assertThat(invoice.getApsOrder()).isEqualTo(apsOrderBack);

        invoice.apsOrder(null);
        assertThat(invoice.getApsOrder()).isNull();
    }
}
