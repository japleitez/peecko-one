package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VideoItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VideoItem getVideoItemSample1() {
        return new VideoItem().id(1L).previous("previous1").code("code1").next("next1");
    }

    public static VideoItem getVideoItemSample2() {
        return new VideoItem().id(2L).previous("previous2").code("code2").next("next2");
    }

    public static VideoItem getVideoItemRandomSampleGenerator() {
        return new VideoItem()
            .id(longCount.incrementAndGet())
            .previous(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .next(UUID.randomUUID().toString());
    }
}
