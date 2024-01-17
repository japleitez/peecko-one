package com.peecko.one.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.StringBuilders;

import java.util.UUID;

public abstract class UUIDUtils {

    public static String generateTrialLicense(String customerCode) {
        String license = UUIDUtils.padCustomerCodeSize8(customerCode) + "7777" + UUIDUtils.randomUUIDSize(8);
        return license.toUpperCase();
    }

    public static String generateLicense(String customerCode) {
        String license = UUIDUtils.padCustomerCodeSize8(customerCode) + UUIDUtils.randomUUIDSize(12);
        return license.toUpperCase();
    }

    public static String padCustomerCodeSize8(String customerCode) {
        return String.format("%-8s", StringUtils.left(customerCode, 8)).replace(' ', '0');
    }

    public static String randomUUIDSize(int length) {
        return StringUtils.right(UUID.randomUUID().toString().replace("-", ""), length);
    }

}
