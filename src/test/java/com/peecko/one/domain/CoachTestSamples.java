package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CoachTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Coach getCoachSample1() {
        return new Coach()
            .id(1L)
            .name("name1")
            .email("email1")
            .website("website1")
            .instagram("instagram1")
            .phoneNumber("phoneNumber1")
            .country("country1")
            .speaks("speaks1")
            .resume("resume1")
            .notes("notes1");
    }

    public static Coach getCoachSample2() {
        return new Coach()
            .id(2L)
            .name("name2")
            .email("email2")
            .website("website2")
            .instagram("instagram2")
            .phoneNumber("phoneNumber2")
            .country("country2")
            .speaks("speaks2")
            .resume("resume2")
            .notes("notes2");
    }

    public static Coach getCoachRandomSampleGenerator() {
        return new Coach()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .instagram(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .speaks(UUID.randomUUID().toString())
            .resume(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
