package com.peecko.one.domain;

import static com.peecko.one.domain.ApsMembershipTestSamples.*;
import static com.peecko.one.domain.ApsOrderTestSamples.*;
import static com.peecko.one.domain.ApsPlanTestSamples.*;
import static com.peecko.one.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApsOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsOrder.class);
        ApsOrder apsOrder1 = getApsOrderSample1();
        ApsOrder apsOrder2 = new ApsOrder();
        assertThat(apsOrder1).isNotEqualTo(apsOrder2);

        apsOrder2.setId(apsOrder1.getId());
        assertThat(apsOrder1).isEqualTo(apsOrder2);

        apsOrder2 = getApsOrderSample2();
        assertThat(apsOrder1).isNotEqualTo(apsOrder2);
    }

    @Test
    void apsMembershipTest() throws Exception {
        ApsOrder apsOrder = getApsOrderRandomSampleGenerator();
        ApsMembership apsMembershipBack = getApsMembershipRandomSampleGenerator();

        apsOrder.addApsMembership(apsMembershipBack);
        assertThat(apsOrder.getApsMemberships()).containsOnly(apsMembershipBack);
        assertThat(apsMembershipBack.getApsOrder()).isEqualTo(apsOrder);

        apsOrder.removeApsMembership(apsMembershipBack);
        assertThat(apsOrder.getApsMemberships()).doesNotContain(apsMembershipBack);
        assertThat(apsMembershipBack.getApsOrder()).isNull();

        apsOrder.apsMemberships(new HashSet<>(Set.of(apsMembershipBack)));
        assertThat(apsOrder.getApsMemberships()).containsOnly(apsMembershipBack);
        assertThat(apsMembershipBack.getApsOrder()).isEqualTo(apsOrder);

        apsOrder.setApsMemberships(new HashSet<>());
        assertThat(apsOrder.getApsMemberships()).doesNotContain(apsMembershipBack);
        assertThat(apsMembershipBack.getApsOrder()).isNull();
    }


    @Test
    void apsPlanTest() throws Exception {
        ApsOrder apsOrder = getApsOrderRandomSampleGenerator();
        ApsPlan apsPlanBack = getApsPlanRandomSampleGenerator();

        apsOrder.setApsPlan(apsPlanBack);
        assertThat(apsOrder.getApsPlan()).isEqualTo(apsPlanBack);

        apsOrder.apsPlan(null);
        assertThat(apsOrder.getApsPlan()).isNull();
    }
}
