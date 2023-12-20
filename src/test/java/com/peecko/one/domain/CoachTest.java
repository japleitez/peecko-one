package com.peecko.one.domain;

import static com.peecko.one.domain.ArticleTestSamples.*;
import static com.peecko.one.domain.CoachTestSamples.*;
import static com.peecko.one.domain.VideoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CoachTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coach.class);
        Coach coach1 = getCoachSample1();
        Coach coach2 = new Coach();
        assertThat(coach1).isNotEqualTo(coach2);

        coach2.setId(coach1.getId());
        assertThat(coach1).isEqualTo(coach2);

        coach2 = getCoachSample2();
        assertThat(coach1).isNotEqualTo(coach2);
    }

    @Test
    void videoTest() throws Exception {
        Coach coach = getCoachRandomSampleGenerator();
        Video videoBack = getVideoRandomSampleGenerator();

        coach.addVideo(videoBack);
        assertThat(coach.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getCoach()).isEqualTo(coach);

        coach.removeVideo(videoBack);
        assertThat(coach.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getCoach()).isNull();

        coach.videos(new HashSet<>(Set.of(videoBack)));
        assertThat(coach.getVideos()).containsOnly(videoBack);
        assertThat(videoBack.getCoach()).isEqualTo(coach);

        coach.setVideos(new HashSet<>());
        assertThat(coach.getVideos()).doesNotContain(videoBack);
        assertThat(videoBack.getCoach()).isNull();
    }

    @Test
    void articleTest() throws Exception {
        Coach coach = getCoachRandomSampleGenerator();
        Article articleBack = getArticleRandomSampleGenerator();

        coach.addArticle(articleBack);
        assertThat(coach.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getCoach()).isEqualTo(coach);

        coach.removeArticle(articleBack);
        assertThat(coach.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getCoach()).isNull();

        coach.articles(new HashSet<>(Set.of(articleBack)));
        assertThat(coach.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getCoach()).isEqualTo(coach);

        coach.setArticles(new HashSet<>());
        assertThat(coach.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getCoach()).isNull();
    }
}
