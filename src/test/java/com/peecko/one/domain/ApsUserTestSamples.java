package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApsUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApsUser getApsUserSample1() {
        return new ApsUser()
            .id(1L)
            .name("name1")
            .username("username1")
            .privateEmail("privateEmail1")
            .license("license1")
            .password("password1");
    }

    public static ApsUser getApsUserSample2() {
        return new ApsUser()
            .id(2L)
            .name("name2")
            .username("username2")
            .privateEmail("privateEmail2")
            .license("license2")
            .password("password2");
    }

    public static ApsUser getApsUserRandomSampleGenerator() {
        return new ApsUser()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .username(UUID.randomUUID().toString())
            .privateEmail(UUID.randomUUID().toString())
            .license(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
