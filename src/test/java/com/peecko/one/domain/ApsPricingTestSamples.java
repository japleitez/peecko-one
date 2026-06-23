package com.peecko.one.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApsPricingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApsPricing getApsPricingSample1() {
        return new ApsPricing().id(1L).index(1).minQuantity(1);
    }

    public static ApsPricing getApsPricingSample2() {
        return new ApsPricing().id(2L).index(2).minQuantity(2);
    }

    public static ApsPricing getApsPricingRandomSampleGenerator() {
        return new ApsPricing()
            .id(longCount.incrementAndGet())
            .index(intCount.incrementAndGet())
            .minQuantity(intCount.incrementAndGet());
    }
}
