package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleSeriesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ArticleSeries getArticleSeriesSample1() {
        return new ArticleSeries()
            .id(1L)
            .code("code1")
            .title("title1")
            .subtitle("subtitle1")
            .summary("summary1")
            .tags("tags1")
            .thumbnail("thumbnail1")
            .counter(1);
    }

    public static ArticleSeries getArticleSeriesSample2() {
        return new ArticleSeries()
            .id(2L)
            .code("code2")
            .title("title2")
            .subtitle("subtitle2")
            .summary("summary2")
            .tags("tags2")
            .thumbnail("thumbnail2")
            .counter(2);
    }

    public static ArticleSeries getArticleSeriesRandomSampleGenerator() {
        return new ArticleSeries()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .subtitle(UUID.randomUUID().toString())
            .summary(UUID.randomUUID().toString())
            .tags(UUID.randomUUID().toString())
            .thumbnail(UUID.randomUUID().toString())
            .counter(intCount.incrementAndGet());
    }
}
