package com.peecko.one.domain;

import static com.peecko.one.domain.InvoiceItemTestSamples.*;
import static com.peecko.one.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItem.class);
        InvoiceItem invoiceItem1 = getInvoiceItemSample1();
        InvoiceItem invoiceItem2 = new InvoiceItem();
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);

        invoiceItem2.setId(invoiceItem1.getId());
        assertThat(invoiceItem1).isEqualTo(invoiceItem2);

        invoiceItem2 = getInvoiceItemSample2();
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
    }

    @Test
    void invoiceTest() throws Exception {
        InvoiceItem invoiceItem = getInvoiceItemRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        invoiceItem.setInvoice(invoiceBack);
        assertThat(invoiceItem.getInvoice()).isEqualTo(invoiceBack);

        invoiceItem.invoice(null);
        assertThat(invoiceItem.getInvoice()).isNull();
    }
}
