package com.peecko.one.web.rest;

import com.peecko.one.domain.ApsMembership;
import com.peecko.one.repository.ApsMembershipRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.peecko.one.domain.ApsMembership}.
 */
@RestController
@RequestMapping("/api/aps-memberships")
@Transactional
public class ApsMembershipResource {

    private final Logger log = LoggerFactory.getLogger(ApsMembershipResource.class);

    private static final String ENTITY_NAME = "apsMembership";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApsMembershipRepository apsMembershipRepository;

    public ApsMembershipResource(ApsMembershipRepository apsMembershipRepository) {
        this.apsMembershipRepository = apsMembershipRepository;
    }

    /**
     * {@code POST  /aps-memberships} : Create a new apsMembership.
     *
     * @param apsMembership the apsMembership to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apsMembership, or with status {@code 400 (Bad Request)} if the apsMembership has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApsMembership> createApsMembership(@Valid @RequestBody ApsMembership apsMembership) throws URISyntaxException {
        log.debug("REST request to save ApsMembership : {}", apsMembership);
        if (apsMembership.getId() != null) {
            throw new BadRequestAlertException("A new apsMembership cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApsMembership result = apsMembershipRepository.save(apsMembership);
        return ResponseEntity
            .created(new URI("/api/aps-memberships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aps-memberships/:id} : Updates an existing apsMembership.
     *
     * @param id the id of the apsMembership to save.
     * @param apsMembership the apsMembership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsMembership,
     * or with status {@code 400 (Bad Request)} if the apsMembership is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apsMembership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApsMembership> updateApsMembership(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApsMembership apsMembership
    ) throws URISyntaxException {
        log.debug("REST request to update ApsMembership : {}, {}", id, apsMembership);
        if (apsMembership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsMembership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsMembershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApsMembership result = apsMembershipRepository.save(apsMembership);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsMembership.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aps-memberships/:id} : Partial updates given fields of an existing apsMembership, field will ignore if it is null
     *
     * @param id the id of the apsMembership to save.
     * @param apsMembership the apsMembership to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsMembership,
     * or with status {@code 400 (Bad Request)} if the apsMembership is not valid,
     * or with status {@code 404 (Not Found)} if the apsMembership is not found,
     * or with status {@code 500 (Internal Server Error)} if the apsMembership couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApsMembership> partialUpdateApsMembership(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApsMembership apsMembership
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApsMembership partially : {}, {}", id, apsMembership);
        if (apsMembership.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsMembership.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsMembershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApsMembership> result = apsMembershipRepository
            .findById(apsMembership.getId())
            .map(existingApsMembership -> {
                if (apsMembership.getPeriod() != null) {
                    existingApsMembership.setPeriod(apsMembership.getPeriod());
                }
                if (apsMembership.getLicense() != null) {
                    existingApsMembership.setLicense(apsMembership.getLicense());
                }
                if (apsMembership.getUsername() != null) {
                    existingApsMembership.setUsername(apsMembership.getUsername());
                }

                return existingApsMembership;
            })
            .map(apsMembershipRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsMembership.getId().toString())
        );
    }

    /**
     * {@code GET  /aps-memberships} : get all the apsMemberships.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apsMemberships in body.
     */
    @GetMapping("")
    public List<ApsMembership> getAllApsMemberships() {
        log.debug("REST request to get all ApsMemberships");
        return apsMembershipRepository.findAll();
    }

    /**
     * {@code GET  /aps-memberships/:id} : get the "id" apsMembership.
     *
     * @param id the id of the apsMembership to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apsMembership, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApsMembership> getApsMembership(@PathVariable("id") Long id) {
        log.debug("REST request to get ApsMembership : {}", id);
        Optional<ApsMembership> apsMembership = apsMembershipRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apsMembership);
    }

    /**
     * {@code DELETE  /aps-memberships/:id} : delete the "id" apsMembership.
     *
     * @param id the id of the apsMembership to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApsMembership(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApsMembership : {}", id);
        apsMembershipRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
