package com.peecko.one.utils;

import java.time.YearMonth;

public abstract class PeriodUtils {

    public static Integer getPeriod(YearMonth yearMonth) {
        return Integer.parseInt(yearMonth.toString().replace("-", ""));
    }

    public static YearMonth getYearMonth(String period) {
        String value = period.replace("-", "");
        int year = Integer.parseInt(value.substring(0,4));
        int month = Integer.parseInt(value.substring(4));
        return YearMonth.of(year, month);
    }

}
