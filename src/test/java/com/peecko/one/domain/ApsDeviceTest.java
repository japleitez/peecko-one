package com.peecko.one.domain;

import static com.peecko.one.domain.ApsDeviceTestSamples.*;
import static com.peecko.one.domain.ApsUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApsDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsDevice.class);
        ApsDevice apsDevice1 = getApsDeviceSample1();
        ApsDevice apsDevice2 = new ApsDevice();
        assertThat(apsDevice1).isNotEqualTo(apsDevice2);

        apsDevice2.setId(apsDevice1.getId());
        assertThat(apsDevice1).isEqualTo(apsDevice2);

        apsDevice2 = getApsDeviceSample2();
        assertThat(apsDevice1).isNotEqualTo(apsDevice2);
    }

    @Test
    void apsUserTest() throws Exception {
        ApsDevice apsDevice = getApsDeviceRandomSampleGenerator();
        ApsUser apsUserBack = getApsUserRandomSampleGenerator();

        apsDevice.setApsUser(apsUserBack);
        assertThat(apsDevice.getApsUser()).isEqualTo(apsUserBack);

        apsDevice.apsUser(null);
        assertThat(apsDevice.getApsUser()).isNull();
    }
}
