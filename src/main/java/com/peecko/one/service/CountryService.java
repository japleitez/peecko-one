package com.peecko.one.service;

import com.peecko.one.domain.Country;
import com.peecko.one.repository.CountryRepository;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public String findCountryNameByCode(String code) {
        Optional<Country> country = countryRepository.findByCode(code);
        return country.map(Country::getName).orElse(this.getLocaleCountryName(code));
    }

    public Country findCountryByCode(String code) {
        return countryRepository.findByCode(code).orElse(buildDefaultCountry());
    }

    public static Country buildDefaultCountry() {
        Country country = new Country();
        country.setCode("LU");
        country.setName("Luxembourg");
        country.setLocale("LU");
        country.setCurrency("EUR");
        return country;
    }

    public String getLocaleCountryName(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            return countryCode;
        }
        if (countryCode.length() != 2) {
            return countryCode;
        }
        try {
            Locale locale = new Locale("", countryCode.toUpperCase());
            String countryName = locale.getDisplayCountry(Locale.ENGLISH);
            if (countryName.isEmpty() || countryName.equals(countryCode.toUpperCase())) {
                throw new IllegalArgumentException("Invalid country code: " + countryCode);
            }

            return countryName;
        } catch (Exception e) {
            return countryCode;
        }
    }
}
