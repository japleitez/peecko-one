package com.peecko.one.domain;

import static com.peecko.one.domain.ApsDeviceTestSamples.*;
import static com.peecko.one.domain.ApsUserTestSamples.*;
import static com.peecko.one.domain.PlayListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApsUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApsUser.class);
        ApsUser apsUser1 = getApsUserSample1();
        ApsUser apsUser2 = new ApsUser();
        assertThat(apsUser1).isNotEqualTo(apsUser2);

        apsUser2.setId(apsUser1.getId());
        assertThat(apsUser1).isEqualTo(apsUser2);

        apsUser2 = getApsUserSample2();
        assertThat(apsUser1).isNotEqualTo(apsUser2);
    }

    @Test
    void apsDeviceTest() throws Exception {
        ApsUser apsUser = getApsUserRandomSampleGenerator();
        ApsDevice apsDeviceBack = getApsDeviceRandomSampleGenerator();

        apsUser.addApsDevice(apsDeviceBack);
        assertThat(apsUser.getApsDevices()).containsOnly(apsDeviceBack);
        assertThat(apsDeviceBack.getApsUser()).isEqualTo(apsUser);

        apsUser.removeApsDevice(apsDeviceBack);
        assertThat(apsUser.getApsDevices()).doesNotContain(apsDeviceBack);
        assertThat(apsDeviceBack.getApsUser()).isNull();

        apsUser.apsDevices(new HashSet<>(Set.of(apsDeviceBack)));
        assertThat(apsUser.getApsDevices()).containsOnly(apsDeviceBack);
        assertThat(apsDeviceBack.getApsUser()).isEqualTo(apsUser);

        apsUser.setApsDevices(new HashSet<>());
        assertThat(apsUser.getApsDevices()).doesNotContain(apsDeviceBack);
        assertThat(apsDeviceBack.getApsUser()).isNull();
    }

    @Test
    void playListTest() throws Exception {
        ApsUser apsUser = getApsUserRandomSampleGenerator();
        PlayList playListBack = getPlayListRandomSampleGenerator();

        apsUser.addPlayList(playListBack);
        assertThat(apsUser.getPlayLists()).containsOnly(playListBack);
        assertThat(playListBack.getApsUser()).isEqualTo(apsUser);

        apsUser.removePlayList(playListBack);
        assertThat(apsUser.getPlayLists()).doesNotContain(playListBack);
        assertThat(playListBack.getApsUser()).isNull();

        apsUser.playLists(new HashSet<>(Set.of(playListBack)));
        assertThat(apsUser.getPlayLists()).containsOnly(playListBack);
        assertThat(playListBack.getApsUser()).isEqualTo(apsUser);

        apsUser.setPlayLists(new HashSet<>());
        assertThat(apsUser.getPlayLists()).doesNotContain(playListBack);
        assertThat(playListBack.getApsUser()).isNull();
    }
}
