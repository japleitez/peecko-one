package com.peecko.one.web.rest;

import com.peecko.one.domain.VideoCategory;
import com.peecko.one.repository.VideoCategoryRepository;
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
 * REST controller for managing {@link com.peecko.one.domain.VideoCategory}.
 */
@RestController
@RequestMapping("/api/video-categories")
@Transactional
public class VideoCategoryResource {

    private final Logger log = LoggerFactory.getLogger(VideoCategoryResource.class);

    private static final String ENTITY_NAME = "videoCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoCategoryRepository videoCategoryRepository;

    public VideoCategoryResource(VideoCategoryRepository videoCategoryRepository) {
        this.videoCategoryRepository = videoCategoryRepository;
    }

    /**
     * {@code POST  /video-categories} : Create a new videoCategory.
     *
     * @param videoCategory the videoCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoCategory, or with status {@code 400 (Bad Request)} if the videoCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VideoCategory> createVideoCategory(@Valid @RequestBody VideoCategory videoCategory) throws URISyntaxException {
        log.debug("REST request to save VideoCategory : {}", videoCategory);
        if (videoCategory.getId() != null) {
            throw new BadRequestAlertException("A new videoCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoCategory result = videoCategoryRepository.save(videoCategory);
        return ResponseEntity
            .created(new URI("/api/video-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /video-categories/:id} : Updates an existing videoCategory.
     *
     * @param id the id of the videoCategory to save.
     * @param videoCategory the videoCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoCategory,
     * or with status {@code 400 (Bad Request)} if the videoCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videoCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VideoCategory> updateVideoCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VideoCategory videoCategory
    ) throws URISyntaxException {
        log.debug("REST request to update VideoCategory : {}, {}", id, videoCategory);
        if (videoCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VideoCategory result = videoCategoryRepository.save(videoCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, videoCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /video-categories/:id} : Partial updates given fields of an existing videoCategory, field will ignore if it is null
     *
     * @param id the id of the videoCategory to save.
     * @param videoCategory the videoCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoCategory,
     * or with status {@code 400 (Bad Request)} if the videoCategory is not valid,
     * or with status {@code 404 (Not Found)} if the videoCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the videoCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VideoCategory> partialUpdateVideoCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VideoCategory videoCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update VideoCategory partially : {}, {}", id, videoCategory);
        if (videoCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VideoCategory> result = videoCategoryRepository
            .findById(videoCategory.getId())
            .map(existingVideoCategory -> {
                if (videoCategory.getCode() != null) {
                    existingVideoCategory.setCode(videoCategory.getCode());
                }
                if (videoCategory.getTitle() != null) {
                    existingVideoCategory.setTitle(videoCategory.getTitle());
                }
                if (videoCategory.getLabel() != null) {
                    existingVideoCategory.setLabel(videoCategory.getLabel());
                }
                if (videoCategory.getCreated() != null) {
                    existingVideoCategory.setCreated(videoCategory.getCreated());
                }
                if (videoCategory.getReleased() != null) {
                    existingVideoCategory.setReleased(videoCategory.getReleased());
                }
                if (videoCategory.getArchived() != null) {
                    existingVideoCategory.setArchived(videoCategory.getArchived());
                }

                return existingVideoCategory;
            })
            .map(videoCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, videoCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /video-categories} : get all the videoCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoCategories in body.
     */
    @GetMapping("")
    public List<VideoCategory> getAllVideoCategories() {
        log.debug("REST request to get all VideoCategories");
        return videoCategoryRepository.findAll();
    }

    /**
     * {@code GET  /video-categories/:id} : get the "id" videoCategory.
     *
     * @param id the id of the videoCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoCategory> getVideoCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get VideoCategory : {}", id);
        Optional<VideoCategory> videoCategory = videoCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(videoCategory);
    }

    /**
     * {@code DELETE  /video-categories/:id} : delete the "id" videoCategory.
     *
     * @param id the id of the videoCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete VideoCategory : {}", id);
        videoCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
