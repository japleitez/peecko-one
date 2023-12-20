package com.peecko.one.web.rest;

import com.peecko.one.domain.ArticleSeries;
import com.peecko.one.repository.ArticleSeriesRepository;
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
 * REST controller for managing {@link com.peecko.one.domain.ArticleSeries}.
 */
@RestController
@RequestMapping("/api/article-series")
@Transactional
public class ArticleSeriesResource {

    private final Logger log = LoggerFactory.getLogger(ArticleSeriesResource.class);

    private static final String ENTITY_NAME = "articleSeries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleSeriesRepository articleSeriesRepository;

    public ArticleSeriesResource(ArticleSeriesRepository articleSeriesRepository) {
        this.articleSeriesRepository = articleSeriesRepository;
    }

    /**
     * {@code POST  /article-series} : Create a new articleSeries.
     *
     * @param articleSeries the articleSeries to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleSeries, or with status {@code 400 (Bad Request)} if the articleSeries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArticleSeries> createArticleSeries(@Valid @RequestBody ArticleSeries articleSeries) throws URISyntaxException {
        log.debug("REST request to save ArticleSeries : {}", articleSeries);
        if (articleSeries.getId() != null) {
            throw new BadRequestAlertException("A new articleSeries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArticleSeries result = articleSeriesRepository.save(articleSeries);
        return ResponseEntity
            .created(new URI("/api/article-series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /article-series/:id} : Updates an existing articleSeries.
     *
     * @param id the id of the articleSeries to save.
     * @param articleSeries the articleSeries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleSeries,
     * or with status {@code 400 (Bad Request)} if the articleSeries is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleSeries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleSeries> updateArticleSeries(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleSeries articleSeries
    ) throws URISyntaxException {
        log.debug("REST request to update ArticleSeries : {}, {}", id, articleSeries);
        if (articleSeries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleSeries.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleSeriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArticleSeries result = articleSeriesRepository.save(articleSeries);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, articleSeries.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /article-series/:id} : Partial updates given fields of an existing articleSeries, field will ignore if it is null
     *
     * @param id the id of the articleSeries to save.
     * @param articleSeries the articleSeries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleSeries,
     * or with status {@code 400 (Bad Request)} if the articleSeries is not valid,
     * or with status {@code 404 (Not Found)} if the articleSeries is not found,
     * or with status {@code 500 (Internal Server Error)} if the articleSeries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArticleSeries> partialUpdateArticleSeries(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArticleSeries articleSeries
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArticleSeries partially : {}, {}", id, articleSeries);
        if (articleSeries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, articleSeries.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!articleSeriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArticleSeries> result = articleSeriesRepository
            .findById(articleSeries.getId())
            .map(existingArticleSeries -> {
                if (articleSeries.getCode() != null) {
                    existingArticleSeries.setCode(articleSeries.getCode());
                }
                if (articleSeries.getTitle() != null) {
                    existingArticleSeries.setTitle(articleSeries.getTitle());
                }
                if (articleSeries.getSubtitle() != null) {
                    existingArticleSeries.setSubtitle(articleSeries.getSubtitle());
                }
                if (articleSeries.getSummary() != null) {
                    existingArticleSeries.setSummary(articleSeries.getSummary());
                }
                if (articleSeries.getLanguage() != null) {
                    existingArticleSeries.setLanguage(articleSeries.getLanguage());
                }
                if (articleSeries.getTags() != null) {
                    existingArticleSeries.setTags(articleSeries.getTags());
                }
                if (articleSeries.getThumbnail() != null) {
                    existingArticleSeries.setThumbnail(articleSeries.getThumbnail());
                }
                if (articleSeries.getCounter() != null) {
                    existingArticleSeries.setCounter(articleSeries.getCounter());
                }
                if (articleSeries.getCreated() != null) {
                    existingArticleSeries.setCreated(articleSeries.getCreated());
                }
                if (articleSeries.getUpdated() != null) {
                    existingArticleSeries.setUpdated(articleSeries.getUpdated());
                }
                if (articleSeries.getReleased() != null) {
                    existingArticleSeries.setReleased(articleSeries.getReleased());
                }
                if (articleSeries.getArchived() != null) {
                    existingArticleSeries.setArchived(articleSeries.getArchived());
                }

                return existingArticleSeries;
            })
            .map(articleSeriesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, articleSeries.getId().toString())
        );
    }

    /**
     * {@code GET  /article-series} : get all the articleSeries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleSeries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleSeries>> getAllArticleSeries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ArticleSeries");
        Page<ArticleSeries> page = articleSeriesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-series/:id} : get the "id" articleSeries.
     *
     * @param id the id of the articleSeries to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleSeries, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleSeries> getArticleSeries(@PathVariable("id") Long id) {
        log.debug("REST request to get ArticleSeries : {}", id);
        Optional<ArticleSeries> articleSeries = articleSeriesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(articleSeries);
    }

    /**
     * {@code DELETE  /article-series/:id} : delete the "id" articleSeries.
     *
     * @param id the id of the articleSeries to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticleSeries(@PathVariable("id") Long id) {
        log.debug("REST request to delete ArticleSeries : {}", id);
        articleSeriesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
