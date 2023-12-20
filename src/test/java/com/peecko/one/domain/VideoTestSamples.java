package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VideoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Video getVideoSample1() {
        return new Video()
            .id(1L)
            .code("code1")
            .title("title1")
            .duration(1)
            .tags("tags1")
            .thumbnail("thumbnail1")
            .url("url1")
            .audience("audience1")
            .filename("filename1")
            .description("description1");
    }

    public static Video getVideoSample2() {
        return new Video()
            .id(2L)
            .code("code2")
            .title("title2")
            .duration(2)
            .tags("tags2")
            .thumbnail("thumbnail2")
            .url("url2")
            .audience("audience2")
            .filename("filename2")
            .description("description2");
    }

    public static Video getVideoRandomSampleGenerator() {
        return new Video()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .tags(UUID.randomUUID().toString())
            .thumbnail(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .audience(UUID.randomUUID().toString())
            .filename(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
