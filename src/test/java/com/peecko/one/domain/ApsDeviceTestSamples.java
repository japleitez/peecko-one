package com.peecko.one.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApsDeviceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApsDevice getApsDeviceSample1() {
        return new ApsDevice().id(1L).username("username1").deviceId("deviceId1").phoneModel("phoneModel1").osVersion("osVersion1");
    }

    public static ApsDevice getApsDeviceSample2() {
        return new ApsDevice().id(2L).username("username2").deviceId("deviceId2").phoneModel("phoneModel2").osVersion("osVersion2");
    }

    public static ApsDevice getApsDeviceRandomSampleGenerator() {
        return new ApsDevice()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .deviceId(UUID.randomUUID().toString())
            .phoneModel(UUID.randomUUID().toString())
            .osVersion(UUID.randomUUID().toString());
    }
}
