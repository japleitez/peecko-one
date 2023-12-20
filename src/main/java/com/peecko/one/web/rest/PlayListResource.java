package com.peecko.one.web.rest;

import com.peecko.one.domain.PlayList;
import com.peecko.one.repository.PlayListRepository;
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
 * REST controller for managing {@link com.peecko.one.domain.PlayList}.
 */
@RestController
@RequestMapping("/api/play-lists")
@Transactional
public class PlayListResource {

    private final Logger log = LoggerFactory.getLogger(PlayListResource.class);

    private static final String ENTITY_NAME = "playList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayListRepository playListRepository;

    public PlayListResource(PlayListRepository playListRepository) {
        this.playListRepository = playListRepository;
    }

    /**
     * {@code POST  /play-lists} : Create a new playList.
     *
     * @param playList the playList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playList, or with status {@code 400 (Bad Request)} if the playList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlayList> createPlayList(@Valid @RequestBody PlayList playList) throws URISyntaxException {
        log.debug("REST request to save PlayList : {}", playList);
        if (playList.getId() != null) {
            throw new BadRequestAlertException("A new playList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayList result = playListRepository.save(playList);
        return ResponseEntity
            .created(new URI("/api/play-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /play-lists/:id} : Updates an existing playList.
     *
     * @param id the id of the playList to save.
     * @param playList the playList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playList,
     * or with status {@code 400 (Bad Request)} if the playList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlayList> updatePlayList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayList playList
    ) throws URISyntaxException {
        log.debug("REST request to update PlayList : {}, {}", id, playList);
        if (playList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayList result = playListRepository.save(playList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /play-lists/:id} : Partial updates given fields of an existing playList, field will ignore if it is null
     *
     * @param id the id of the playList to save.
     * @param playList the playList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playList,
     * or with status {@code 400 (Bad Request)} if the playList is not valid,
     * or with status {@code 404 (Not Found)} if the playList is not found,
     * or with status {@code 500 (Internal Server Error)} if the playList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayList> partialUpdatePlayList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayList playList
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayList partially : {}, {}", id, playList);
        if (playList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayList> result = playListRepository
            .findById(playList.getId())
            .map(existingPlayList -> {
                if (playList.getName() != null) {
                    existingPlayList.setName(playList.getName());
                }
                if (playList.getCounter() != null) {
                    existingPlayList.setCounter(playList.getCounter());
                }
                if (playList.getCreated() != null) {
                    existingPlayList.setCreated(playList.getCreated());
                }
                if (playList.getUpdated() != null) {
                    existingPlayList.setUpdated(playList.getUpdated());
                }

                return existingPlayList;
            })
            .map(playListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playList.getId().toString())
        );
    }

    /**
     * {@code GET  /play-lists} : get all the playLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playLists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlayList>> getAllPlayLists(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PlayLists");
        Page<PlayList> page = playListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /play-lists/:id} : get the "id" playList.
     *
     * @param id the id of the playList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayList> getPlayList(@PathVariable("id") Long id) {
        log.debug("REST request to get PlayList : {}", id);
        Optional<PlayList> playList = playListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playList);
    }

    /**
     * {@code DELETE  /play-lists/:id} : delete the "id" playList.
     *
     * @param id the id of the playList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayList(@PathVariable("id") Long id) {
        log.debug("REST request to delete PlayList : {}", id);
        playListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
