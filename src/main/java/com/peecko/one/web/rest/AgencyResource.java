package com.peecko.one.web.rest;

import com.peecko.one.domain.Agency;
import com.peecko.one.repository.AgencyRepository;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.UserService;
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
 * REST controller for managing {@link com.peecko.one.domain.Agency}.
 */
@RestController
@RequestMapping("/api/agencies")
@Transactional
public class AgencyResource {

    private final Logger log = LoggerFactory.getLogger(AgencyResource.class);

    private static final String ENTITY_NAME = "agency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final AgencyRepository agencyRepository;

    public AgencyResource(UserService userService, AgencyRepository agencyRepository) {
        this.userService = userService;
        this.agencyRepository = agencyRepository;
    }

    /**
     * {@code POST  /agencies} : Create a new agency.
     *
     * @param agency the agency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agency, or with status {@code 400 (Bad Request)} if the agency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Agency> createAgency(@Valid @RequestBody Agency agency) throws URISyntaxException {
        log.debug("REST request to save Agency : {}", agency);
        if (agency.getId() != null) {
            throw new BadRequestAlertException("A new agency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agency result = agencyRepository.save(agency);
        return ResponseEntity
            .created(new URI("/api/agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agencies/:id} : Updates an existing agency.
     *
     * @param id the id of the agency to save.
     * @param agency the agency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agency,
     * or with status {@code 400 (Bad Request)} if the agency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Agency> updateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Agency agency
    ) throws URISyntaxException {
        log.debug("REST request to update Agency : {}, {}", id, agency);
        if (agency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Agency result = agencyRepository.save(agency);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agency.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agencies/:id} : Partial updates given fields of an existing agency, field will ignore if it is null
     *
     * @param id the id of the agency to save.
     * @param agency the agency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agency,
     * or with status {@code 400 (Bad Request)} if the agency is not valid,
     * or with status {@code 404 (Not Found)} if the agency is not found,
     * or with status {@code 500 (Internal Server Error)} if the agency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Agency> partialUpdateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Agency agency
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agency partially : {}, {}", id, agency);
        if (agency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agency> result = agencyRepository
            .findById(agency.getId())
            .map(existingAgency -> {
                if (agency.getCode() != null) {
                    existingAgency.setCode(agency.getCode());
                }
                if (agency.getName() != null) {
                    existingAgency.setName(agency.getName());
                }
                if (agency.getLine1() != null) {
                    existingAgency.setLine1(agency.getLine1());
                }
                if (agency.getLine2() != null) {
                    existingAgency.setLine2(agency.getLine2());
                }
                if (agency.getZip() != null) {
                    existingAgency.setZip(agency.getZip());
                }
                if (agency.getCity() != null) {
                    existingAgency.setCity(agency.getCity());
                }
                if (agency.getCountry() != null) {
                    existingAgency.setCountry(agency.getCountry());
                }
                if (agency.getLanguage() != null) {
                    existingAgency.setLanguage(agency.getLanguage());
                }
                if (agency.getEmail() != null) {
                    existingAgency.setEmail(agency.getEmail());
                }
                if (agency.getPhone() != null) {
                    existingAgency.setPhone(agency.getPhone());
                }
                if (agency.getBillingEmail() != null) {
                    existingAgency.setBillingEmail(agency.getBillingEmail());
                }
                if (agency.getBillingPhone() != null) {
                    existingAgency.setBillingPhone(agency.getBillingPhone());
                }
                if (agency.getBank() != null) {
                    existingAgency.setBank(agency.getBank());
                }
                if (agency.getIban() != null) {
                    existingAgency.setIban(agency.getIban());
                }
                if (agency.getRcs() != null) {
                    existingAgency.setRcs(agency.getRcs());
                }
                if (agency.getVatId() != null) {
                    existingAgency.setVatId(agency.getVatId());
                }
                if (agency.getVatRate() != null) {
                    existingAgency.setVatRate(agency.getVatRate());
                }
                if (agency.getNotes() != null) {
                    existingAgency.setNotes(agency.getNotes());
                }
                if (agency.getCreated() != null) {
                    existingAgency.setCreated(agency.getCreated());
                }
                if (agency.getUpdated() != null) {
                    existingAgency.setUpdated(agency.getUpdated());
                }

                return existingAgency;
            })
            .map(agencyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agency.getId().toString())
        );
    }

    /**
     * {@code GET  /agencies} : get all the agencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencies in body.
     */
    @GetMapping("")
    public List<Agency> getAllAgencies() {
        log.debug("REST request to get all Agencies");
        return agencyRepository.findAll();
    }

    /**
     * {@code GET  /agencies/:id} : get the "id" agency.
     *
     * @param id the id of the agency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgency(@PathVariable("id") Long id) {
        log.debug("REST request to get Agency : {}", id);
        Optional<Agency> agency = agencyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agency);
    }

    /**
     * {@code DELETE  /agencies/:id} : delete the "id" agency.
     *
     * @param id the id of the agency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable("id") Long id) {
        log.debug("REST request to delete Agency : {}", id);
        agencyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/current")
    public ResponseEntity<Agency> getAgency() {
         Long agencyId = userService.getCurrentAgencyId();
         Optional<Agency> agency = agencyRepository.findById(agencyId);
         return ResponseUtil.wrapOrNotFound(agency);
    }

}
