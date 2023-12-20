package com.peecko.one.domain;

import static com.peecko.one.domain.ApsMembershipTestSamples.*;
import static com.peecko.one.domain.ApsOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApsMembershipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsMembership.class);
        ApsMembership apsMembership1 = getApsMembershipSample1();
        ApsMembership apsMembership2 = new ApsMembership();
        assertThat(apsMembership1).isNotEqualTo(apsMembership2);

        apsMembership2.setId(apsMembership1.getId());
        assertThat(apsMembership1).isEqualTo(apsMembership2);

        apsMembership2 = getApsMembershipSample2();
        assertThat(apsMembership1).isNotEqualTo(apsMembership2);
    }

    @Test
    void apsOrderTest() throws Exception {
        ApsMembership apsMembership = getApsMembershipRandomSampleGenerator();
        ApsOrder apsOrderBack = getApsOrderRandomSampleGenerator();

        apsMembership.setApsOrder(apsOrderBack);
        assertThat(apsMembership.getApsOrder()).isEqualTo(apsOrderBack);

        apsMembership.apsOrder(null);
        assertThat(apsMembership.getApsOrder()).isNull();
    }
}
