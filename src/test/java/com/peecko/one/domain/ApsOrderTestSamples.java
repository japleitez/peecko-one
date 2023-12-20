package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApsOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApsOrder getApsOrderSample1() {
        return new ApsOrder().id(1L).period(1).license("license1").numberOfUsers(1).invoiceNumber("invoiceNumber1");
    }

    public static ApsOrder getApsOrderSample2() {
        return new ApsOrder().id(2L).period(2).license("license2").numberOfUsers(2).invoiceNumber("invoiceNumber2");
    }

    public static ApsOrder getApsOrderRandomSampleGenerator() {
        return new ApsOrder()
            .id(longCount.incrementAndGet())
            .period(intCount.incrementAndGet())
            .license(UUID.randomUUID().toString())
            .numberOfUsers(intCount.incrementAndGet())
            .invoiceNumber(UUID.randomUUID().toString());
    }
}
