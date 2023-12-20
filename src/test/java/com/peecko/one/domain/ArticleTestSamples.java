package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Article getArticleSample1() {
        return new Article()
            .id(1L)
            .code("code1")
            .title("title1")
            .subtitle("subtitle1")
            .summary("summary1")
            .tags("tags1")
            .duration(1)
            .thumbnail("thumbnail1")
            .audioUrl("audioUrl1")
            .content("content1")
            .seriesId(1L)
            .chapter(1);
    }

    public static Article getArticleSample2() {
        return new Article()
            .id(2L)
            .code("code2")
            .title("title2")
            .subtitle("subtitle2")
            .summary("summary2")
            .tags("tags2")
            .duration(2)
            .thumbnail("thumbnail2")
            .audioUrl("audioUrl2")
            .content("content2")
            .seriesId(2L)
            .chapter(2);
    }

    public static Article getArticleRandomSampleGenerator() {
        return new Article()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .subtitle(UUID.randomUUID().toString())
            .summary(UUID.randomUUID().toString())
            .tags(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .thumbnail(UUID.randomUUID().toString())
            .audioUrl(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .seriesId(longCount.incrementAndGet())
            .chapter(intCount.incrementAndGet());
    }
}
