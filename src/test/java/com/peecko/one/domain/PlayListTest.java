package com.peecko.one.domain;

import static com.peecko.one.domain.ApsUserTestSamples.*;
import static com.peecko.one.domain.PlayListTestSamples.*;
import static com.peecko.one.domain.VideoItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlayListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayList.class);
        PlayList playList1 = getPlayListSample1();
        PlayList playList2 = new PlayList();
        assertThat(playList1).isNotEqualTo(playList2);

        playList2.setId(playList1.getId());
        assertThat(playList1).isEqualTo(playList2);

        playList2 = getPlayListSample2();
        assertThat(playList1).isNotEqualTo(playList2);
    }

    @Test
    void videoItemTest() throws Exception {
        PlayList playList = getPlayListRandomSampleGenerator();
        VideoItem videoItemBack = getVideoItemRandomSampleGenerator();

        playList.addVideoItem(videoItemBack);
        assertThat(playList.getVideoItems()).containsOnly(videoItemBack);
        assertThat(videoItemBack.getPlayList()).isEqualTo(playList);

        playList.removeVideoItem(videoItemBack);
        assertThat(playList.getVideoItems()).doesNotContain(videoItemBack);
        assertThat(videoItemBack.getPlayList()).isNull();

        playList.videoItems(new HashSet<>(Set.of(videoItemBack)));
        assertThat(playList.getVideoItems()).containsOnly(videoItemBack);
        assertThat(videoItemBack.getPlayList()).isEqualTo(playList);

        playList.setVideoItems(new HashSet<>());
        assertThat(playList.getVideoItems()).doesNotContain(videoItemBack);
        assertThat(videoItemBack.getPlayList()).isNull();
    }

    @Test
    void apsUserTest() throws Exception {
        PlayList playList = getPlayListRandomSampleGenerator();
        ApsUser apsUserBack = getApsUserRandomSampleGenerator();

        playList.setApsUser(apsUserBack);
        assertThat(playList.getApsUser()).isEqualTo(apsUserBack);

        playList.apsUser(null);
        assertThat(playList.getApsUser()).isNull();
    }
}
