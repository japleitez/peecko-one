package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArticleCategory getArticleCategorySample1() {
        return new ArticleCategory().id(1L).code("code1").title("title1").label("label1");
    }

    public static ArticleCategory getArticleCategorySample2() {
        return new ArticleCategory().id(2L).code("code2").title("title2").label("label2");
    }

    public static ArticleCategory getArticleCategoryRandomSampleGenerator() {
        return new ArticleCategory()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .label(UUID.randomUUID().toString());
    }
}
