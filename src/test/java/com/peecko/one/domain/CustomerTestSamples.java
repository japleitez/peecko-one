package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .code("code1")
            .name("name1")
            .country("country1")
            .license("license1")
            .closeReason("closeReason1")
            .emailDomains("emailDomains1")
            .vatId("vatId1")
            .bank("bank1")
            .iban("iban1")
            .logo("logo1")
            .notes("notes1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .code("code2")
            .name("name2")
            .country("country2")
            .license("license2")
            .closeReason("closeReason2")
            .emailDomains("emailDomains2")
            .vatId("vatId2")
            .bank("bank2")
            .iban("iban2")
            .logo("logo2")
            .notes("notes2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .license(UUID.randomUUID().toString())
            .closeReason(UUID.randomUUID().toString())
            .emailDomains(UUID.randomUUID().toString())
            .vatId(UUID.randomUUID().toString())
            .bank(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
