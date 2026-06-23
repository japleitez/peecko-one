package com.peecko.one.utils;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Utility class for dynamically formatting prices based on locale and currency.
 */
public class PriceFormatter {

    /**
     * Formats a numeric price as a string with currency symbol and localization.
     *
     * @param price the numeric price value to format
     * @param locale the locale code (e.g., "US", "DE", "FR")
     * @param currency the ISO 4217 currency code (e.g., "USD", "EUR", "HNL")
     * @return a formatted price string with currency symbol
     * @throws IllegalArgumentException if locale code or currency code is invalid
     * @throws NullPointerException if any parameter is null
     */
    public static String formatPrice(double price, Locale locale, Currency currency) {
        try {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            numberFormat.setCurrency(currency);
            return numberFormat.format(price).replaceAll("\\s+", " ");
        } catch (Exception e) {
            throw new RuntimeException(
                "Error formatting price with locale: " + locale.toString() + ", currency: " + currency.toString(),
                e
            );
        }
    }

    public static String formatPricePlain(double price, Locale locale, Currency currency) {
        String formattedPrice = formatPrice(price, locale, currency);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        String currencySymbol = String.valueOf(symbols.getCurrencySymbol());
        String result = formattedPrice.replace(currencySymbol, "");
        result = result.replaceAll("\\s+", " ");
        return result;
    }
}
