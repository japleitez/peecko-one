package com.peecko.one.web.rest;

import com.peecko.one.domain.Country;
import com.peecko.one.repository.CountryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/countries")
@Transactional
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    private static final String ENTITY_NAME = "country";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryRepository countryRepository;

    public CountryResource(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PostMapping("")
    public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to save Country : {}", country);
        if (country.getCode() == null) {
            throw new BadRequestAlertException("A new country must have a code", ENTITY_NAME, "idnull");
        }
        if (countryRepository.existsById(country.getCode())) {
            throw new BadRequestAlertException("A country with this code already exists", ENTITY_NAME, "idexists");
        }
        Country result = countryRepository.save(country);
        return ResponseEntity
            .created(new URI("/api/countries/" + result.getCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getCode()))
            .body(result);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Country> updateCountry(
        @PathVariable(value = "code", required = false) final String code,
        @Valid @RequestBody Country country
    ) throws URISyntaxException {
        log.debug("REST request to update Country : {}, {}", code, country);
        if (country.getCode() == null) {
            throw new BadRequestAlertException("Invalid code", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(code, country.getCode())) {
            throw new BadRequestAlertException("Invalid code", ENTITY_NAME, "idinvalid");
        }
        if (!countryRepository.existsById(code)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Country result = countryRepository.save(country);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, country.getCode()))
            .body(result);
    }

    @PatchMapping(value = "/{code}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Country> partialUpdateCountry(
        @PathVariable(value = "code", required = false) final String code,
        @NotNull @RequestBody Country country
    ) throws URISyntaxException {
        log.debug("REST request to partial update Country : {}, {}", code, country);
        if (country.getCode() == null) {
            throw new BadRequestAlertException("Invalid code", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(code, country.getCode())) {
            throw new BadRequestAlertException("Invalid code", ENTITY_NAME, "idinvalid");
        }
        if (!countryRepository.existsById(code)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Country> result = countryRepository
            .findById(country.getCode())
            .map(existingCountry -> {
                if (country.getName() != null) {
                    existingCountry.setName(country.getName());
                }
                if (country.getLocale() != null) {
                    existingCountry.setLocale(country.getLocale());
                }
                if (country.getCurrency() != null) {
                    existingCountry.setCurrency(country.getCurrency());
                }
                if (country.getLanguage() != null) {
                    existingCountry.setLanguage(country.getLanguage());
                }
                return existingCountry;
            })
            .map(countryRepository::save);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, country.getCode())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<Country>> getAllCountries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Countries");
        Page<Country> page = countryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Country> getCountry(@PathVariable("code") String code) {
        log.debug("REST request to get Country : {}", code);
        Optional<Country> country = countryRepository.findById(code);
        return ResponseUtil.wrapOrNotFound(country);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCountry(@PathVariable("code") String code) {
        log.debug("REST request to delete Country : {}", code);
        countryRepository.deleteById(code);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, code)).build();
    }
}
