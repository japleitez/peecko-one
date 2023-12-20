package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contact getContactSample1() {
        return new Contact()
            .id(1L)
            .name("name1")
            .line1("line11")
            .line2("line21")
            .zip("zip1")
            .city("city1")
            .country("country1")
            .email("email1")
            .phone("phone1")
            .notes("notes1");
    }

    public static Contact getContactSample2() {
        return new Contact()
            .id(2L)
            .name("name2")
            .line1("line12")
            .line2("line22")
            .zip("zip2")
            .city("city2")
            .country("country2")
            .email("email2")
            .phone("phone2")
            .notes("notes2");
    }

    public static Contact getContactRandomSampleGenerator() {
        return new Contact()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .line1(UUID.randomUUID().toString())
            .line2(UUID.randomUUID().toString())
            .zip(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
