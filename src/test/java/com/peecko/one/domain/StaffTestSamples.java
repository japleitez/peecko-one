package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StaffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Staff getStaffSample1() {
        return new Staff().id(1L).userId(1L).role("role1");
    }

    public static Staff getStaffSample2() {
        return new Staff().id(2L).userId(2L).role("role2");
    }

    public static Staff getStaffRandomSampleGenerator() {
        return new Staff().id(longCount.incrementAndGet()).userId(longCount.incrementAndGet()).role(UUID.randomUUID().toString());
    }
}
