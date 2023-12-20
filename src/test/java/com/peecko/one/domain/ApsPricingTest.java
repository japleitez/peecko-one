package com.peecko.one.domain;

import static com.peecko.one.domain.AgencyTestSamples.*;
import static com.peecko.one.domain.ApsPricingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApsPricingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsPricing.class);
        ApsPricing apsPricing1 = getApsPricingSample1();
        ApsPricing apsPricing2 = new ApsPricing();
        assertThat(apsPricing1).isNotEqualTo(apsPricing2);

        apsPricing2.setId(apsPricing1.getId());
        assertThat(apsPricing1).isEqualTo(apsPricing2);

        apsPricing2 = getApsPricingSample2();
        assertThat(apsPricing1).isNotEqualTo(apsPricing2);
    }

    @Test
    void agencyTest() throws Exception {
        ApsPricing apsPricing = getApsPricingRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        apsPricing.setAgency(agencyBack);
        assertThat(apsPricing.getAgency()).isEqualTo(agencyBack);

        apsPricing.agency(null);
        assertThat(apsPricing.getAgency()).isNull();
    }
}
