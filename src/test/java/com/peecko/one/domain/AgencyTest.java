package com.peecko.one.domain;

import static com.peecko.one.domain.AgencyTestSamples.*;
import static com.peecko.one.domain.ApsPricingTestSamples.*;
import static com.peecko.one.domain.CustomerTestSamples.*;
import static com.peecko.one.domain.StaffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agency.class);
        Agency agency1 = getAgencySample1();
        Agency agency2 = new Agency();
        assertThat(agency1).isNotEqualTo(agency2);

        agency2.setId(agency1.getId());
        assertThat(agency1).isEqualTo(agency2);

        agency2 = getAgencySample2();
        assertThat(agency1).isNotEqualTo(agency2);
    }

    @Test
    void staffTest() throws Exception {
        Agency agency = getAgencyRandomSampleGenerator();
        Staff staffBack = getStaffRandomSampleGenerator();

        agency.addStaff(staffBack);
        assertThat(agency.getStaff()).containsOnly(staffBack);
        assertThat(staffBack.getAgency()).isEqualTo(agency);

        agency.removeStaff(staffBack);
        assertThat(agency.getStaff()).doesNotContain(staffBack);
        assertThat(staffBack.getAgency()).isNull();

        agency.staff(new HashSet<>(Set.of(staffBack)));
        assertThat(agency.getStaff()).containsOnly(staffBack);
        assertThat(staffBack.getAgency()).isEqualTo(agency);

        agency.setStaff(new HashSet<>());
        assertThat(agency.getStaff()).doesNotContain(staffBack);
        assertThat(staffBack.getAgency()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        Agency agency = getAgencyRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        agency.addCustomer(customerBack);
        assertThat(agency.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getAgency()).isEqualTo(agency);

        agency.removeCustomer(customerBack);
        assertThat(agency.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getAgency()).isNull();

        agency.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(agency.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getAgency()).isEqualTo(agency);

        agency.setCustomers(new HashSet<>());
        assertThat(agency.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getAgency()).isNull();
    }

}
