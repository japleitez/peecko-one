package com.peecko.one.web.rest;

import com.peecko.one.domain.VideoItem;
import com.peecko.one.repository.VideoItemRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

    private final VideoItemRepository videoItemRepository;

    public VideoItemResource(VideoItemRepository videoItemRepository) {
        this.videoItemRepository = videoItemRepository;
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

}
