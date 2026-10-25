package com.peecko.one.service;

import com.peecko.one.domain.Language;
import com.peecko.one.repository.LanguageRepository;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides the language codes stored in the {@link Language} table.
 * Language codes are stored on the other entities as upper-case strings (EN, FR, ...).
 */
@Service
@Transactional(readOnly = true)
public class LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageService.class);

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * Returns the codes of the active languages, ordered by language id.
     * Returns an empty list when no language is defined.
     *
     * @throws DataAccessException if the languages cannot be read from the database (the failure is logged).
     */
    public List<String> findActiveCodes() {
        try {
            return languageRepository
                .findByActiveTrueOrderByIdAsc()
                .stream()
                .map(Language::getCode)
                .map(LanguageService::normalize)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        } catch (DataAccessException e) {
            log.error("Failed to load the language codes from the database", e);
            throw e;
        }
    }

    /**
     * Normalizes the given language code and checks that it exists in the language table.
     *
     * @param code the language code to validate, may be null.
     * @param entityName the name of the entity being saved, used in the error alert.
     * @return the normalized (trimmed, upper-case) code, or null if the given code is null or blank.
     * @throws BadRequestAlertException if the code is not defined in the language table.
     */
    public String validateCode(String code, String entityName) {
        String normalized = normalize(code);
        if (normalized == null) {
            return null;
        }
        boolean exists;
        try {
            exists = languageRepository.existsByCodeIgnoreCase(normalized);
        } catch (DataAccessException e) {
            log.error("Failed to validate the language code {} against the database", normalized, e);
            throw e;
        }
        if (!exists) {
            throw new BadRequestAlertException("Unknown language code: " + normalized, entityName, "languageinvalid");
        }
        return normalized;
    }

    /**
     * @return the trimmed, upper-case code, or null if the given code is null or blank.
     */
    public static String normalize(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return code.trim().toUpperCase(Locale.ROOT);
    }
}
