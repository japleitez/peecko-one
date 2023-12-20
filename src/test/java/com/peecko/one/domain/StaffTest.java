package com.peecko.one.domain;

import static com.peecko.one.domain.AgencyTestSamples.*;
import static com.peecko.one.domain.StaffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Staff.class);
        Staff staff1 = getStaffSample1();
        Staff staff2 = new Staff();
        assertThat(staff1).isNotEqualTo(staff2);

        staff2.setId(staff1.getId());
        assertThat(staff1).isEqualTo(staff2);

        staff2 = getStaffSample2();
        assertThat(staff1).isNotEqualTo(staff2);
    }

    @Test
    void agencyTest() throws Exception {
        Staff staff = getStaffRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        staff.setAgency(agencyBack);
        assertThat(staff.getAgency()).isEqualTo(agencyBack);

        staff.agency(null);
        assertThat(staff.getAgency()).isNull();
    }
}
