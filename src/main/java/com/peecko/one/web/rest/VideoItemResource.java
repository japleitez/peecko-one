package com.peecko.one.web.rest;

import com.peecko.one.domain.VideoItem;
import com.peecko.one.repository.VideoItemRepository;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.peecko.one.domain.VideoItem}.
 */
@RestController
@RequestMapping("/api/video-items")
@Transactional
public class VideoItemResource {

    private final Logger log = LoggerFactory.getLogger(VideoItemResource.class);

    private static final String ENTITY_NAME = "videoItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoItemRepository videoItemRepository;

    public VideoItemResource(VideoItemRepository videoItemRepository) {
        this.videoItemRepository = videoItemRepository;
    }

    /**
     * {@code POST  /video-items} : Create a new videoItem.
     *
     * @param videoItem the videoItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoItem, or with status {@code 400 (Bad Request)} if the videoItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VideoItem> createVideoItem(@RequestBody VideoItem videoItem) throws URISyntaxException {
        log.debug("REST request to save VideoItem : {}", videoItem);
        if (videoItem.getId() != null) {
            throw new BadRequestAlertException("A new videoItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoItem result = videoItemRepository.save(videoItem);
        return ResponseEntity
            .created(new URI("/api/video-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /video-items/:id} : Updates an existing videoItem.
     *
     * @param id the id of the videoItem to save.
     * @param videoItem the videoItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoItem,
     * or with status {@code 400 (Bad Request)} if the videoItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videoItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VideoItem> updateVideoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VideoItem videoItem
    ) throws URISyntaxException {
        log.debug("REST request to update VideoItem : {}, {}", id, videoItem);
        if (videoItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VideoItem result = videoItemRepository.save(videoItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, videoItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /video-items/:id} : Partial updates given fields of an existing videoItem, field will ignore if it is null
     *
     * @param id the id of the videoItem to save.
     * @param videoItem the videoItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoItem,
     * or with status {@code 400 (Bad Request)} if the videoItem is not valid,
     * or with status {@code 404 (Not Found)} if the videoItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the videoItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VideoItem> partialUpdateVideoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VideoItem videoItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update VideoItem partially : {}, {}", id, videoItem);
        if (videoItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VideoItem> result = videoItemRepository
            .findById(videoItem.getId())
            .map(existingVideoItem -> {
                if (videoItem.getPrevious() != null) {
                    existingVideoItem.setPrevious(videoItem.getPrevious());
                }
                if (videoItem.getCode() != null) {
                    existingVideoItem.setCode(videoItem.getCode());
                }
                if (videoItem.getNext() != null) {
                    existingVideoItem.setNext(videoItem.getNext());
                }

                return existingVideoItem;
            })
            .map(videoItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, videoItem.getId().toString())
        );
    }

    /**
     * {@code GET  /video-items} : get all the videoItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VideoItem>> getAllVideoItems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VideoItems");
        Page<VideoItem> page = videoItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /video-items/:id} : get the "id" videoItem.
     *
     * @param id the id of the videoItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoItem> getVideoItem(@PathVariable("id") Long id) {
        log.debug("REST request to get VideoItem : {}", id);
        Optional<VideoItem> videoItem = videoItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(videoItem);
    }

    /**
     * {@code DELETE  /video-items/:id} : delete the "id" videoItem.
     *
     * @param id the id of the videoItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete VideoItem : {}", id);
        videoItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
