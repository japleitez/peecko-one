package com.peecko.one.web.rest;

import com.peecko.one.domain.TrialLicense;
import com.peecko.one.repository.TrialLicenseRepository;
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
@RequestMapping("/api/trial-licenses")
@Transactional
public class TrialLicenseResource {

    private final Logger log = LoggerFactory.getLogger(TrialLicenseResource.class);

    private static final String ENTITY_NAME = "trialLicense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrialLicenseRepository trialLicenseRepository;

    public TrialLicenseResource(TrialLicenseRepository trialLicenseRepository) {
        this.trialLicenseRepository = trialLicenseRepository;
    }

    @PostMapping("")
    public ResponseEntity<TrialLicense> createTrialLicense(@Valid @RequestBody TrialLicense trialLicense) throws URISyntaxException {
        log.debug("REST request to save TrialLicense : {}", trialLicense);
        if (trialLicense.getLicense() == null) {
            throw new BadRequestAlertException("A new trial license must have a license key", ENTITY_NAME, "idnull");
        }
        if (trialLicenseRepository.existsById(trialLicense.getLicense())) {
            throw new BadRequestAlertException("A trial license with this key already exists", ENTITY_NAME, "idexists");
        }
        TrialLicense result = trialLicenseRepository.save(trialLicense);
        return ResponseEntity
            .created(new URI("/api/trial-licenses/" + result.getLicense()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getLicense()))
            .body(result);
    }

    @PutMapping("/{license}")
    public ResponseEntity<TrialLicense> updateTrialLicense(
        @PathVariable(value = "license", required = false) final String license,
        @Valid @RequestBody TrialLicense trialLicense
    ) throws URISyntaxException {
        log.debug("REST request to update TrialLicense : {}, {}", license, trialLicense);
        if (trialLicense.getLicense() == null) {
            throw new BadRequestAlertException("Invalid license", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(license, trialLicense.getLicense())) {
            throw new BadRequestAlertException("Invalid license", ENTITY_NAME, "idinvalid");
        }
        if (!trialLicenseRepository.existsById(license)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        TrialLicense result = trialLicenseRepository.save(trialLicense);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trialLicense.getLicense()))
            .body(result);
    }

    @PatchMapping(value = "/{license}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrialLicense> partialUpdateTrialLicense(
        @PathVariable(value = "license", required = false) final String license,
        @NotNull @RequestBody TrialLicense trialLicense
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrialLicense : {}, {}", license, trialLicense);
        if (trialLicense.getLicense() == null) {
            throw new BadRequestAlertException("Invalid license", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(license, trialLicense.getLicense())) {
            throw new BadRequestAlertException("Invalid license", ENTITY_NAME, "idinvalid");
        }
        if (!trialLicenseRepository.existsById(license)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<TrialLicense> result = trialLicenseRepository
            .findById(trialLicense.getLicense())
            .map(existingTrialLicense -> {
                if (trialLicense.getExpirationDate() != null) {
                    existingTrialLicense.setExpirationDate(trialLicense.getExpirationDate());
                }
                return existingTrialLicense;
            })
            .map(trialLicenseRepository::save);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trialLicense.getLicense())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<TrialLicense>> getAllTrialLicenses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TrialLicenses");
        Page<TrialLicense> page = trialLicenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{license}")
    public ResponseEntity<TrialLicense> getTrialLicense(@PathVariable("license") String license) {
        log.debug("REST request to get TrialLicense : {}", license);
        Optional<TrialLicense> trialLicense = trialLicenseRepository.findById(license);
        return ResponseUtil.wrapOrNotFound(trialLicense);
    }

    @DeleteMapping("/{license}")
    public ResponseEntity<Void> deleteTrialLicense(@PathVariable("license") String license) {
        log.debug("REST request to delete TrialLicense : {}", license);
        trialLicenseRepository.deleteById(license);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, license))
            .build();
    }
}
