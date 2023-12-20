package com.peecko.one.domain;

import static com.peecko.one.domain.ArticleSeriesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArticleSeriesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleSeries.class);
        ArticleSeries articleSeries1 = getArticleSeriesSample1();
        ArticleSeries articleSeries2 = new ArticleSeries();
        assertThat(articleSeries1).isNotEqualTo(articleSeries2);

        articleSeries2.setId(articleSeries1.getId());
        assertThat(articleSeries1).isEqualTo(articleSeries2);

        articleSeries2 = getArticleSeriesSample2();
        assertThat(articleSeries1).isNotEqualTo(articleSeries2);
    }
}
