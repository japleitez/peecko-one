package com.peecko.one.domain.enumeration;

/**
 * ISO 3166-1 alpha-2 codes for all 27 EU member states,
 * with each country's name in its own official language.
 */
public enum CountryName {
    // A
    AT("Österreich"),
    BE("Belgique"),
    BG("България"),

    // C
    CY("Κύπρος"),
    CZ("Česká republika"),

    // D
    DE("Deutschland"),
    DK("Danmark"),

    // E
    EE("Eesti"),
    ES("España"),

    // F
    FI("Finland"),
    FR("France"),

    // G
    GR("Ελλάδα"),

    // H
    HR("Hrvatska"),
    HU("Magyarország"),

    // I
    IE("Ireland"),
    IT("Italia"),

    // L
    LT("Lietuva"),
    LU("Luxembourg"),
    LV("Latvija"),

    // M
    MT("Malta"),

    // N
    NL("Nederland"),

    // P
    PL("Polska"),
    PT("Portugal"),

    // R
    RO("România"),

    // S
    SE("Sverige"),
    SI("Slovenija"),
    SK("Slovensko");

    private final String nativeName;

    CountryName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getNativeName() {
        return nativeName;
    }

    /**
     * Look up a Country by its ISO 3166-1 alpha-2 code (case-insensitive).
     *
     * @param code the two-letter country code
     * @return the matching Country, or empty if not found
     */
    public static java.util.Optional<CountryName> fromCode(String code) {
        if (code == null) return java.util.Optional.empty();
        try {
            return java.util.Optional.of(valueOf(code.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }

    @Override
    public String toString() {
        return nativeName;
    }
}
