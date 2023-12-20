package com.peecko.one.domain;

import static com.peecko.one.domain.ApsOrderTestSamples.*;
import static com.peecko.one.domain.ApsPlanTestSamples.*;
import static com.peecko.one.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApsPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsPlan.class);
        ApsPlan apsPlan1 = getApsPlanSample1();
        ApsPlan apsPlan2 = new ApsPlan();
        assertThat(apsPlan1).isNotEqualTo(apsPlan2);

        apsPlan2.setId(apsPlan1.getId());
        assertThat(apsPlan1).isEqualTo(apsPlan2);

        apsPlan2 = getApsPlanSample2();
        assertThat(apsPlan1).isNotEqualTo(apsPlan2);
    }

    @Test
    void apsOrderTest() throws Exception {
        ApsPlan apsPlan = getApsPlanRandomSampleGenerator();
        ApsOrder apsOrderBack = getApsOrderRandomSampleGenerator();

        apsPlan.addApsOrder(apsOrderBack);
        assertThat(apsPlan.getApsOrders()).containsOnly(apsOrderBack);
        assertThat(apsOrderBack.getApsPlan()).isEqualTo(apsPlan);

        apsPlan.removeApsOrder(apsOrderBack);
        assertThat(apsPlan.getApsOrders()).doesNotContain(apsOrderBack);
        assertThat(apsOrderBack.getApsPlan()).isNull();

        apsPlan.apsOrders(new HashSet<>(Set.of(apsOrderBack)));
        assertThat(apsPlan.getApsOrders()).containsOnly(apsOrderBack);
        assertThat(apsOrderBack.getApsPlan()).isEqualTo(apsPlan);

        apsPlan.setApsOrders(new HashSet<>());
        assertThat(apsPlan.getApsOrders()).doesNotContain(apsOrderBack);
        assertThat(apsOrderBack.getApsPlan()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        ApsPlan apsPlan = getApsPlanRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        apsPlan.setCustomer(customerBack);
        assertThat(apsPlan.getCustomer()).isEqualTo(customerBack);

        apsPlan.customer(null);
        assertThat(apsPlan.getCustomer()).isNull();
    }
}
