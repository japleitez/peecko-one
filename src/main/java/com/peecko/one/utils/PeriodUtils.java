package com.peecko.one.utils;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public abstract class PeriodUtils {

    public static Integer getPeriod(YearMonth yearMonth) {
        return Integer.parseInt(yearMonth.toString().replace("-", ""));
    }

    public static YearMonth getYearMonth(Integer period) {
        return getYearMonth(String.valueOf(period));
    }

    public static YearMonth getYearMonth(String period) {
        String value = period.replace("-", "");
        int year = Integer.parseInt(value.substring(0,4));
        int month = Integer.parseInt(value.substring(4));
        return YearMonth.of(year, month);
    }

    public static Optional<YearMonth> parseYearMonth(String period) {
        try {
            YearMonth yearMonth = YearMonth.parse(period);
            return Optional.of(yearMonth);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static LocalDate parsePeriodDay(Integer period, String dd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(period + dd, formatter);
    }
}
