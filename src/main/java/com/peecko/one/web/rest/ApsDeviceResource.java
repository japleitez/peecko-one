package com.peecko.one.web.rest;

import com.peecko.one.domain.ApsDevice;
import com.peecko.one.repository.ApsDeviceRepository;
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
 * REST controller for managing {@link com.peecko.one.domain.ApsDevice}.
 */
@RestController
@RequestMapping("/api/aps-devices")
@Transactional
public class ApsDeviceResource {

    private final Logger log = LoggerFactory.getLogger(ApsDeviceResource.class);

    private static final String ENTITY_NAME = "apsDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApsDeviceRepository apsDeviceRepository;

    public ApsDeviceResource(ApsDeviceRepository apsDeviceRepository) {
        this.apsDeviceRepository = apsDeviceRepository;
    }

    /**
     * {@code POST  /aps-devices} : Create a new apsDevice.
     *
     * @param apsDevice the apsDevice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apsDevice, or with status {@code 400 (Bad Request)} if the apsDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApsDevice> createApsDevice(@Valid @RequestBody ApsDevice apsDevice) throws URISyntaxException {
        log.debug("REST request to save ApsDevice : {}", apsDevice);
        if (apsDevice.getId() != null) {
            throw new BadRequestAlertException("A new apsDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApsDevice result = apsDeviceRepository.save(apsDevice);
        return ResponseEntity
            .created(new URI("/api/aps-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aps-devices/:id} : Updates an existing apsDevice.
     *
     * @param id the id of the apsDevice to save.
     * @param apsDevice the apsDevice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsDevice,
     * or with status {@code 400 (Bad Request)} if the apsDevice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apsDevice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApsDevice> updateApsDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApsDevice apsDevice
    ) throws URISyntaxException {
        log.debug("REST request to update ApsDevice : {}, {}", id, apsDevice);
        if (apsDevice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsDevice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApsDevice result = apsDeviceRepository.save(apsDevice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsDevice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aps-devices/:id} : Partial updates given fields of an existing apsDevice, field will ignore if it is null
     *
     * @param id the id of the apsDevice to save.
     * @param apsDevice the apsDevice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsDevice,
     * or with status {@code 400 (Bad Request)} if the apsDevice is not valid,
     * or with status {@code 404 (Not Found)} if the apsDevice is not found,
     * or with status {@code 500 (Internal Server Error)} if the apsDevice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApsDevice> partialUpdateApsDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApsDevice apsDevice
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApsDevice partially : {}, {}", id, apsDevice);
        if (apsDevice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsDevice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApsDevice> result = apsDeviceRepository
            .findById(apsDevice.getId())
            .map(existingApsDevice -> {
                if (apsDevice.getUsername() != null) {
                    existingApsDevice.setUsername(apsDevice.getUsername());
                }
                if (apsDevice.getDeviceId() != null) {
                    existingApsDevice.setDeviceId(apsDevice.getDeviceId());
                }
                if (apsDevice.getPhoneModel() != null) {
                    existingApsDevice.setPhoneModel(apsDevice.getPhoneModel());
                }
                if (apsDevice.getOsVersion() != null) {
                    existingApsDevice.setOsVersion(apsDevice.getOsVersion());
                }
                if (apsDevice.getInstalledOn() != null) {
                    existingApsDevice.setInstalledOn(apsDevice.getInstalledOn());
                }

                return existingApsDevice;
            })
            .map(apsDeviceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsDevice.getId().toString())
        );
    }

    /**
     * {@code GET  /aps-devices} : get all the apsDevices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apsDevices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApsDevice>> getAllApsDevices(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ApsDevices");
        Page<ApsDevice> page = apsDeviceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aps-devices/:id} : get the "id" apsDevice.
     *
     * @param id the id of the apsDevice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apsDevice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApsDevice> getApsDevice(@PathVariable("id") Long id) {
        log.debug("REST request to get ApsDevice : {}", id);
        Optional<ApsDevice> apsDevice = apsDeviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apsDevice);
    }

    /**
     * {@code DELETE  /aps-devices/:id} : delete the "id" apsDevice.
     *
     * @param id the id of the apsDevice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApsDevice(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApsDevice : {}", id);
        apsDeviceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
