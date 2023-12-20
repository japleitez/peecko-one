package com.peecko.one.domain;

import static com.peecko.one.domain.CoachTestSamples.*;
import static com.peecko.one.domain.VideoCategoryTestSamples.*;
import static com.peecko.one.domain.VideoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VideoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Video.class);
        Video video1 = getVideoSample1();
        Video video2 = new Video();
        assertThat(video1).isNotEqualTo(video2);

        video2.setId(video1.getId());
        assertThat(video1).isEqualTo(video2);

        video2 = getVideoSample2();
        assertThat(video1).isNotEqualTo(video2);
    }

    @Test
    void videoCategoryTest() throws Exception {
        Video video = getVideoRandomSampleGenerator();
        VideoCategory videoCategoryBack = getVideoCategoryRandomSampleGenerator();

        video.setVideoCategory(videoCategoryBack);
        assertThat(video.getVideoCategory()).isEqualTo(videoCategoryBack);

        video.videoCategory(null);
        assertThat(video.getVideoCategory()).isNull();
    }

    @Test
    void coachTest() throws Exception {
        Video video = getVideoRandomSampleGenerator();
        Coach coachBack = getCoachRandomSampleGenerator();

        video.setCoach(coachBack);
        assertThat(video.getCoach()).isEqualTo(coachBack);

        video.coach(null);
        assertThat(video.getCoach()).isNull();
    }
}
