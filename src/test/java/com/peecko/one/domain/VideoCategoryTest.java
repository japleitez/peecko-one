package com.peecko.one.domain;

import static com.peecko.one.domain.VideoCategoryTestSamples.*;
import static com.peecko.one.domain.VideoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VideoCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoCategory.class);
        VideoCategory videoCategory1 = getVideoCategorySample1();
        VideoCategory videoCategory2 = new VideoCategory();
        assertThat(videoCategory1).isNotEqualTo(videoCategory2);

        videoCategory2.setId(videoCategory1.getId());
        assertThat(videoCategory1).isEqualTo(videoCategory2);

        videoCategory2 = getVideoCategorySample2();
        assertThat(videoCategory1).isNotEqualTo(videoCategory2);
    }

    @Test
    void videoTest() throws Exception {
        VideoCategory videoCategory = getVideoCategoryRandomSampleGenerator();
        Video videoBack = getVideoRandomSampleGenerator();

        videoCategory.addVideo(videoBack);
        assertThat(videoCategory.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getVideoCategory()).isEqualTo(videoCategory);

        videoCategory.removeVideo(videoBack);
        assertThat(videoCategory.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getVideoCategory()).isNull();

        videoCategory.videos(new HashSet<>(Set.of(videoBack)));
        assertThat(videoCategory.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getVideoCategory()).isEqualTo(videoCategory);

        videoCategory.setVideos(new HashSet<>());
        assertThat(videoCategory.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getVideoCategory()).isNull();
    }
}
