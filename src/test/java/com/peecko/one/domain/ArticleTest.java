package com.peecko.one.domain;

import static com.peecko.one.domain.ArticleCategoryTestSamples.*;
import static com.peecko.one.domain.ArticleTestSamples.*;
import static com.peecko.one.domain.CoachTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = getArticleSample1();
        Article article2 = new Article();
        assertThat(article1).isNotEqualTo(article2);

        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);

        article2 = getArticleSample2();
        assertThat(article1).isNotEqualTo(article2);
    }

    @Test
    void articleCategoryTest() throws Exception {
        Article article = getArticleRandomSampleGenerator();
        ArticleCategory articleCategoryBack = getArticleCategoryRandomSampleGenerator();

        article.setArticleCategory(articleCategoryBack);
        assertThat(article.getArticleCategory()).isEqualTo(articleCategoryBack);

        article.articleCategory(null);
        assertThat(article.getArticleCategory()).isNull();
    }

    @Test
    void coachTest() throws Exception {
        Article article = getArticleRandomSampleGenerator();
        Coach coachBack = getCoachRandomSampleGenerator();

        article.setCoach(coachBack);
        assertThat(article.getCoach()).isEqualTo(coachBack);

        article.coach(null);
        assertThat(article.getCoach()).isNull();
    }
}
