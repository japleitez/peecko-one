package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApsPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApsPlan getApsPlanSample1() {
        return new ApsPlan().id(1L).contract("contract1").license("license1").notes("notes1");
    }

    public static ApsPlan getApsPlanSample2() {
        return new ApsPlan().id(2L).contract("contract2").license("license2").notes("notes2");
    }

    public static ApsPlan getApsPlanRandomSampleGenerator() {
        return new ApsPlan()
            .id(longCount.incrementAndGet())
            .contract(UUID.randomUUID().toString())
            .license(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
