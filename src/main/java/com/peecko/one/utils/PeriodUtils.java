package com.peecko.one.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public abstract class PeriodUtils {

    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Integer getPeriod(YearMonth yearMonth) {
        return Integer.parseInt(yearMonth.toString().replace("-", ""));
    }

    public static YearMonth parse(Integer period) {
        return parse(String.valueOf(period));
    }

    public static YearMonth parse(String period) {
        String value = period.replace("-", "");
        int year = Integer.parseInt(value.substring(0, 4));
        int month = Integer.parseInt(value.substring(4));
        return YearMonth.of(year, month);
    }

    public static LocalDate parsePeriodDay(Integer period, String dd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(period + dd, formatter);
    }

    public static String getFirstDateAsString(final YearMonth yearMonth) {
        final LocalDate firstDate = yearMonth.atDay(1);
        return firstDate.format(OUTPUT_FORMATTER);
    }

    public static String getLastDateAsString(final YearMonth yearMonth) {
        final LocalDate lastDate = yearMonth.atEndOfMonth();
        return lastDate.format(OUTPUT_FORMATTER);
    }
}
