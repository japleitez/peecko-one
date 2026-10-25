package com.peecko.one.web.rest;

import com.peecko.one.domain.Language;
import com.peecko.one.repository.LanguageRepository;
import com.peecko.one.service.LanguageService;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.peecko.one.domain.Language}.
 */
@RestController
@RequestMapping("/api/languages")
@Transactional
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    private static final String ENTITY_NAME = "language";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguageRepository languageRepository;

    private final LanguageService languageService;

    public LanguageResource(LanguageRepository languageRepository, LanguageService languageService) {
        this.languageRepository = languageRepository;
        this.languageService = languageService;
    }

    @PostMapping("")
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            throw new BadRequestAlertException("A new language cannot already have an ID", ENTITY_NAME, "idexists");
        }
        language.setCode(LanguageService.normalize(language.getCode()));
        Language result = languageRepository.save(language);
        return ResponseEntity
            .created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to update Language : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        language.setCode(LanguageService.normalize(language.getCode()));
        Language result = languageRepository.save(language);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, language.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Language> partialUpdateLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to partial update Language : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Language> result = languageRepository
            .findById(language.getId())
            .map(existingLanguage -> {
                if (language.getCode() != null) {
                    existingLanguage.setCode(LanguageService.normalize(language.getCode()));
                }
                if (language.getName() != null) {
                    existingLanguage.setName(language.getName());
                }
                if (language.getActive() != null) {
                    existingLanguage.setActive(language.getActive());
                }
                return existingLanguage;
            })
            .map(languageRepository::save);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, language.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<Language>> getAllLanguages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Languages");
        Page<Language> page = languageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /languages/codes} : get the codes of the active languages, ordered by language id.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codes in body (empty if no language is defined),
     * or with status {@code 503 (Service Unavailable)} if the languages cannot be loaded.
     */
    @GetMapping("/codes")
    @Transactional(readOnly = true)
    public ResponseEntity<List<String>> getActiveLanguageCodes() {
        log.debug("REST request to get the active language codes");
        try {
            return ResponseEntity.ok(languageService.findActiveCodes());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The languages could not be loaded", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguage(@PathVariable("id") Long id) {
        log.debug("REST request to get Language : {}", id);
        Optional<Language> language = languageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(language);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable("id") Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
