package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agency getAgencySample1() {
        return new Agency()
            .id(1L)
            .code("code1")
            .name("name1")
            .line1("line11")
            .line2("line21")
            .zip("zip1")
            .city("city1")
            .country("country1")
            .email("email1")
            .phone("phone1")
            .billingEmail("billingEmail1")
            .billingPhone("billingPhone1")
            .bank("bank1")
            .iban("iban1")
            .rcs("rcs1")
            .vatId("vatId1")
            .notes("notes1");
    }

    public static Agency getAgencySample2() {
        return new Agency()
            .id(2L)
            .code("code2")
            .name("name2")
            .line1("line12")
            .line2("line22")
            .zip("zip2")
            .city("city2")
            .country("country2")
            .email("email2")
            .phone("phone2")
            .billingEmail("billingEmail2")
            .billingPhone("billingPhone2")
            .bank("bank2")
            .iban("iban2")
            .rcs("rcs2")
            .vatId("vatId2")
            .notes("notes2");
    }

    public static Agency getAgencyRandomSampleGenerator() {
        return new Agency()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .line1(UUID.randomUUID().toString())
            .line2(UUID.randomUUID().toString())
            .zip(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .billingEmail(UUID.randomUUID().toString())
            .billingPhone(UUID.randomUUID().toString())
            .bank(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .rcs(UUID.randomUUID().toString())
            .vatId(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
