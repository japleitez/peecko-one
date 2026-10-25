package com.peecko.one.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.peecko.one.domain.Language;
import com.peecko.one.repository.LanguageRepository;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

class LanguageServiceTest {

    private LanguageRepository languageRepository;
    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        languageRepository = mock(LanguageRepository.class);
        languageService = new LanguageService(languageRepository);
    }

    @Test
    void findActiveCodesReturnsNormalizedCodesInRepositoryOrder() {
        when(languageRepository.findByActiveTrueOrderByIdAsc())
            .thenReturn(
                List.of(new Language("en", "English", true), new Language("FR", "French", true), new Language(" de ", "German", true))
            );

        assertThat(languageService.findActiveCodes()).containsExactly("EN", "FR", "DE");
    }

    @Test
    void findActiveCodesReturnsEmptyListWhenNoLanguageIsDefined() {
        when(languageRepository.findByActiveTrueOrderByIdAsc()).thenReturn(List.of());

        assertThat(languageService.findActiveCodes()).isEmpty();
    }

    @Test
    void findActiveCodesPropagatesDatabaseFailures() {
        when(languageRepository.findByActiveTrueOrderByIdAsc()).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> languageService.findActiveCodes()).isInstanceOf(DataAccessResourceFailureException.class);
    }

    @Test
    void validateCodeNormalizesKnownCodes() {
        when(languageRepository.existsByCodeIgnoreCase("EN")).thenReturn(true);

        assertThat(languageService.validateCode(" en ", "article")).isEqualTo("EN");
    }

    @Test
    void validateCodeRejectsUnknownCodes() {
        when(languageRepository.existsByCodeIgnoreCase("ZZ")).thenReturn(false);

        assertThatThrownBy(() -> languageService.validateCode("zz", "article")).isInstanceOf(BadRequestAlertException.class);
    }

    @Test
    void validateCodeKeepsNullValues() {
        assertThat(languageService.validateCode(null, "article")).isNull();
        assertThat(languageService.validateCode("  ", "article")).isNull();
    }
}
