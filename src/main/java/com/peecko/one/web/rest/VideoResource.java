package com.peecko.one.web.rest;

import com.peecko.one.domain.Video;
import com.peecko.one.repository.VideoRepository;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
 * REST controller for managing {@link com.peecko.one.domain.Video}.
 */
@RestController
@RequestMapping("/api/videos")
@Transactional
public class VideoResource {

    private final Logger log = LoggerFactory.getLogger(VideoResource.class);

    private static final String ENTITY_NAME = "video";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoRepository videoRepository;

    public VideoResource(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /**
     * {@code POST  /videos} : Create a new video.
     *
     * @param video the video to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new video, or with status {@code 400 (Bad Request)} if the video has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to save Video : {}", video);
        if (video.getId() != null) {
            throw new BadRequestAlertException("A new video cannot already have an ID", ENTITY_NAME, "idexists");
        }
        video.setCreated(Instant.now());
        Video result = videoRepository.save(video);
        return ResponseEntity
            .created(new URI("/api/videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /videos/:id} : Updates an existing video.
     *
     * @param id the id of the video to save.
     * @param video the video to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated video,
     * or with status {@code 400 (Bad Request)} if the video is not valid,
     * or with status {@code 500 (Internal Server Error)} if the video couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Video video)
        throws URISyntaxException {
        log.debug("REST request to update Video : {}, {}", id, video);
        if (video.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, video.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Video result = videoRepository.save(video);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, video.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /videos/:id} : Partial updates given fields of an existing video, field will ignore if it is null
     *
     * @param id the id of the video to save.
     * @param video the video to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated video,
     * or with status {@code 400 (Bad Request)} if the video is not valid,
     * or with status {@code 404 (Not Found)} if the video is not found,
     * or with status {@code 500 (Internal Server Error)} if the video couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Video> partialUpdateVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Video video
    ) throws URISyntaxException {
        log.debug("REST request to partial update Video partially : {}, {}", id, video);
        if (video.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, video.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Video> result = videoRepository
            .findById(video.getId())
            .map(existingVideo -> {
                if (video.getCode() != null) {
                    existingVideo.setCode(video.getCode());
                }
                if (video.getTitle() != null) {
                    existingVideo.setTitle(video.getTitle());
                }
                if (video.getDuration() != null) {
                    existingVideo.setDuration(video.getDuration());
                }
                if (video.getLanguage() != null) {
                    existingVideo.setLanguage(video.getLanguage());
                }
                if (video.getTags() != null) {
                    existingVideo.setTags(video.getTags());
                }
                if (video.getPlayer() != null) {
                    existingVideo.setPlayer(video.getPlayer());
                }
                if (video.getThumbnail() != null) {
                    existingVideo.setThumbnail(video.getThumbnail());
                }
                if (video.getUrl() != null) {
                    existingVideo.setUrl(video.getUrl());
                }
                if (video.getAudience() != null) {
                    existingVideo.setAudience(video.getAudience());
                }
                if (video.getIntensity() != null) {
                    existingVideo.setIntensity(video.getIntensity());
                }
                if (video.getFilename() != null) {
                    existingVideo.setFilename(video.getFilename());
                }
                if (video.getDescription() != null) {
                    existingVideo.setDescription(video.getDescription());
                }
                if (video.getCreated() != null) {
                    existingVideo.setCreated(video.getCreated());
                }
                if (video.getReleased() != null) {
                    existingVideo.setReleased(video.getReleased());
                }
                if (video.getArchived() != null) {
                    existingVideo.setArchived(video.getArchived());
                }

                return existingVideo;
            })
            .map(videoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, video.getId().toString())
        );
    }

    /**
     * {@code GET  /videos} : get all the videos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Video>> getAllVideos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Videos");
        Page<Video> page = videoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /videos/:id} : get the "id" video.
     *
     * @param id the id of the video to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the video, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable("id") Long id) {
        log.debug("REST request to get Video : {}", id);
        Optional<Video> video = videoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(video);
    }

    /**
     * {@code DELETE  /videos/:id} : delete the "id" video.
     *
     * @param id the id of the video to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable("id") Long id) {
        log.debug("REST request to delete Video : {}", id);
        videoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
