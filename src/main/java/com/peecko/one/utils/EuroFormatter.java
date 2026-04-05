package com.peecko.one.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class EuroFormatter {

    /**
     * Converts a Double value to a Euro-formatted string
     * WITHOUT the Euro symbol (€).
     *
     * @param amount The double value to format
     * @return A Euro-formatted string without the currency symbol  (e.g., "1.234.567,89")
     */
    public static String format(Double amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null.");
        }

        // Use GERMANY locale for Euro formatting conventions
        // (dot as thousands separator, comma as decimal separator)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);

        // Pattern: grouping separator + 2 decimal places, no symbol
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);

        return formatter.format(amount);
    }

    /**
     * Converts a Double value to a Euro-formatted string
     * WITHOUT the Euro symbol, with an optional "EUR" text label.
     *
     * @param amount   The double value to format
     * @param showCode Whether to append the "EUR" currency code
     * @return A formatted string, optionally suffixed with "EUR"
     */
    public static String format(Double amount, boolean showCode) {
        String formatted = format(amount);
        return showCode ? formatted + " EUR" : formatted;
    }

    public static void main(String[] args) {
        Double[] testAmounts = { 1234567.891, 0.5, 1000.0, -450.75, 99.99 };

        System.out.println("===========================================");
        System.out.println("  Euro Formatter — No Symbol              ");
        System.out.println("===========================================");
        System.out.printf("%-20s %-20s %-20s%n", "Input Value", "Formatted", "With EUR Code");
        System.out.println("-------------------------------------------");

        for (Double amount : testAmounts) {
            System.out.printf("%-20s %-20s %-20s%n", amount, format(amount), format(amount, true));
        }

        System.out.println("===========================================");
    }
}
