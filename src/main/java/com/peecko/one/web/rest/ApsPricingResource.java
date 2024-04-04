package com.peecko.one.web.rest;

import com.peecko.one.domain.ApsPricing;
import com.peecko.one.repository.ApsPricingRepository;
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

/**
 * REST controller for managing {@link com.peecko.one.domain.ApsPricing}.
 */
@RestController
@RequestMapping("/api/aps-pricings")
@Transactional
public class ApsPricingResource {

    private final Logger log = LoggerFactory.getLogger(ApsPricingResource.class);

    private static final String ENTITY_NAME = "apsPricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApsPricingRepository apsPricingRepository;

    public ApsPricingResource(ApsPricingRepository apsPricingRepository) {
        this.apsPricingRepository = apsPricingRepository;
    }

    /**
     * {@code POST  /aps-pricings} : Create a new apsPricing.
     *
     * @param apsPricing the apsPricing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apsPricing, or with status {@code 400 (Bad Request)} if the apsPricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApsPricing> createApsPricing(@Valid @RequestBody ApsPricing apsPricing) throws URISyntaxException {
        log.debug("REST request to save ApsPricing : {}", apsPricing);
        if (apsPricing.getId() != null) {
            throw new BadRequestAlertException("A new apsPricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApsPricing result = apsPricingRepository.save(apsPricing);
        return ResponseEntity
            .created(new URI("/api/aps-pricings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aps-pricings/:id} : Updates an existing apsPricing.
     *
     * @param id the id of the apsPricing to save.
     * @param apsPricing the apsPricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsPricing,
     * or with status {@code 400 (Bad Request)} if the apsPricing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apsPricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApsPricing> updateApsPricing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApsPricing apsPricing
    ) throws URISyntaxException {
        log.debug("REST request to update ApsPricing : {}, {}", id, apsPricing);
        if (apsPricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsPricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsPricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApsPricing result = apsPricingRepository.save(apsPricing);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsPricing.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aps-pricings/:id} : Partial updates given fields of an existing apsPricing, field will ignore if it is null
     *
     * @param id the id of the apsPricing to save.
     * @param apsPricing the apsPricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsPricing,
     * or with status {@code 400 (Bad Request)} if the apsPricing is not valid,
     * or with status {@code 404 (Not Found)} if the apsPricing is not found,
     * or with status {@code 500 (Internal Server Error)} if the apsPricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApsPricing> partialUpdateApsPricing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApsPricing apsPricing
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApsPricing partially : {}, {}", id, apsPricing);
        if (apsPricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsPricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsPricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApsPricing> result = apsPricingRepository
            .findById(apsPricing.getId())
            .map(existingApsPricing -> {
                if (apsPricing.getIndex() != null) {
                    existingApsPricing.setIndex(apsPricing.getIndex());
                }
                if (apsPricing.getMinQuantity() != null) {
                    existingApsPricing.setMinQuantity(apsPricing.getMinQuantity());
                }
                if (apsPricing.getUnitPrice() != null) {
                    existingApsPricing.setUnitPrice(apsPricing.getUnitPrice());
                }

                return existingApsPricing;
            })
            .map(apsPricingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsPricing.getId().toString())
        );
    }

    /**
     * {@code GET  /aps-pricings} : get all the apsPricings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apsPricings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApsPricing>> getAllApsPricings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ApsPricings");
        Page<ApsPricing> page = apsPricingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aps-pricings/:id} : get the "id" apsPricing.
     *
     * @param id the id of the apsPricing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apsPricing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApsPricing> getApsPricing(@PathVariable("id") Long id) {
        log.debug("REST request to get ApsPricing : {}", id);
        Optional<ApsPricing> apsPricing = apsPricingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apsPricing);
    }

    /**
     * {@code DELETE  /aps-pricings/:id} : delete the "id" apsPricing.
     *
     * @param id the id of the apsPricing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApsPricing(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApsPricing : {}", id);
        apsPricingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
