package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApsMembershipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApsMembership getApsMembershipSample1() {
        return new ApsMembership().id(1L).period(1).license("license1").username("username1");
    }

    public static ApsMembership getApsMembershipSample2() {
        return new ApsMembership().id(2L).period(2).license("license2").username("username2");
    }

    public static ApsMembership getApsMembershipRandomSampleGenerator() {
        return new ApsMembership()
            .id(longCount.incrementAndGet())
            .period(intCount.incrementAndGet())
            .license(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString());
    }
}
