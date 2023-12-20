package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LabelTranslationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LabelTranslation getLabelTranslationSample1() {
        return new LabelTranslation().id(1L).label("label1").translation("translation1");
    }

    public static LabelTranslation getLabelTranslationSample2() {
        return new LabelTranslation().id(2L).label("label2").translation("translation2");
    }

    public static LabelTranslation getLabelTranslationRandomSampleGenerator() {
        return new LabelTranslation()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .translation(UUID.randomUUID().toString());
    }
}
