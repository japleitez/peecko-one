package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ArticleSeries;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.repository.ArticleSeriesRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArticleSeriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleSeriesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.EN;
    private static final Language UPDATED_LANGUAGE = Language.FR;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNTER = 1;
    private static final Integer UPDATED_COUNTER = 2;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/article-series";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleSeriesRepository articleSeriesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleSeriesMockMvc;

    private ArticleSeries articleSeries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleSeries createEntity(EntityManager em) {
        ArticleSeries articleSeries = new ArticleSeries()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .subtitle(DEFAULT_SUBTITLE)
            .summary(DEFAULT_SUMMARY)
            .language(DEFAULT_LANGUAGE)
            .tags(DEFAULT_TAGS)
            .thumbnail(DEFAULT_THUMBNAIL)
            .counter(DEFAULT_COUNTER)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .released(DEFAULT_RELEASED)
            .archived(DEFAULT_ARCHIVED);
        return articleSeries;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleSeries createUpdatedEntity(EntityManager em) {
        ArticleSeries articleSeries = new ArticleSeries()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .thumbnail(UPDATED_THUMBNAIL)
            .counter(UPDATED_COUNTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);
        return articleSeries;
    }

    @BeforeEach
    public void initTest() {
        articleSeries = createEntity(em);
    }

    @Test
    @Transactional
    void createArticleSeries() throws Exception {
        int databaseSizeBeforeCreate = articleSeriesRepository.findAll().size();
        // Create the ArticleSeries
        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isCreated());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeCreate + 1);
        ArticleSeries testArticleSeries = articleSeriesList.get(articleSeriesList.size() - 1);
        assertThat(testArticleSeries.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticleSeries.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticleSeries.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
        assertThat(testArticleSeries.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testArticleSeries.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testArticleSeries.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testArticleSeries.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testArticleSeries.getCounter()).isEqualTo(DEFAULT_COUNTER);
        assertThat(testArticleSeries.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testArticleSeries.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testArticleSeries.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testArticleSeries.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void createArticleSeriesWithExistingId() throws Exception {
        // Create the ArticleSeries with an existing ID
        articleSeries.setId(1L);

        int databaseSizeBeforeCreate = articleSeriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleSeriesRepository.findAll().size();
        // set the field null
        articleSeries.setCode(null);

        // Create the ArticleSeries, which fails.

        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isBadRequest());

        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleSeriesRepository.findAll().size();
        // set the field null
        articleSeries.setTitle(null);

        // Create the ArticleSeries, which fails.

        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isBadRequest());

        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleSeriesRepository.findAll().size();
        // set the field null
        articleSeries.setLanguage(null);

        // Create the ArticleSeries, which fails.

        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isBadRequest());

        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCounterIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleSeriesRepository.findAll().size();
        // set the field null
        articleSeries.setCounter(null);

        // Create the ArticleSeries, which fails.

        restArticleSeriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isBadRequest());

        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleSeries() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        // Get all the articleSeriesList
        restArticleSeriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleSeries.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.[*].counter").value(hasItem(DEFAULT_COUNTER)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].released").value(hasItem(DEFAULT_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.toString())));
    }

    @Test
    @Transactional
    void getArticleSeries() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        // Get the articleSeries
        restArticleSeriesMockMvc
            .perform(get(ENTITY_API_URL_ID, articleSeries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleSeries.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL))
            .andExpect(jsonPath("$.counter").value(DEFAULT_COUNTER))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.released").value(DEFAULT_RELEASED.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticleSeries() throws Exception {
        // Get the articleSeries
        restArticleSeriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleSeries() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();

        // Update the articleSeries
        ArticleSeries updatedArticleSeries = articleSeriesRepository.findById(articleSeries.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticleSeries are not directly saved in db
        em.detach(updatedArticleSeries);
        updatedArticleSeries
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .thumbnail(UPDATED_THUMBNAIL)
            .counter(UPDATED_COUNTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restArticleSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArticleSeries.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArticleSeries))
            )
            .andExpect(status().isOk());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
        ArticleSeries testArticleSeries = articleSeriesList.get(articleSeriesList.size() - 1);
        assertThat(testArticleSeries.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticleSeries.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticleSeries.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticleSeries.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticleSeries.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testArticleSeries.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArticleSeries.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testArticleSeries.getCounter()).isEqualTo(UPDATED_COUNTER);
        assertThat(testArticleSeries.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleSeries.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticleSeries.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testArticleSeries.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void putNonExistingArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleSeries.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleSeries)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleSeriesWithPatch() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();

        // Update the articleSeries using partial update
        ArticleSeries partialUpdatedArticleSeries = new ArticleSeries();
        partialUpdatedArticleSeries.setId(articleSeries.getId());

        partialUpdatedArticleSeries
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .archived(UPDATED_ARCHIVED);

        restArticleSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleSeries))
            )
            .andExpect(status().isOk());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
        ArticleSeries testArticleSeries = articleSeriesList.get(articleSeriesList.size() - 1);
        assertThat(testArticleSeries.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticleSeries.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticleSeries.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticleSeries.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticleSeries.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testArticleSeries.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testArticleSeries.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testArticleSeries.getCounter()).isEqualTo(DEFAULT_COUNTER);
        assertThat(testArticleSeries.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleSeries.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticleSeries.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testArticleSeries.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void fullUpdateArticleSeriesWithPatch() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();

        // Update the articleSeries using partial update
        ArticleSeries partialUpdatedArticleSeries = new ArticleSeries();
        partialUpdatedArticleSeries.setId(articleSeries.getId());

        partialUpdatedArticleSeries
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .thumbnail(UPDATED_THUMBNAIL)
            .counter(UPDATED_COUNTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restArticleSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleSeries))
            )
            .andExpect(status().isOk());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
        ArticleSeries testArticleSeries = articleSeriesList.get(articleSeriesList.size() - 1);
        assertThat(testArticleSeries.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticleSeries.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticleSeries.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticleSeries.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticleSeries.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testArticleSeries.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArticleSeries.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testArticleSeries.getCounter()).isEqualTo(UPDATED_COUNTER);
        assertThat(testArticleSeries.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleSeries.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticleSeries.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testArticleSeries.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void patchNonExistingArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleSeries() throws Exception {
        int databaseSizeBeforeUpdate = articleSeriesRepository.findAll().size();
        articleSeries.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(articleSeries))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleSeries in the database
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleSeries() throws Exception {
        // Initialize the database
        articleSeriesRepository.saveAndFlush(articleSeries);

        int databaseSizeBeforeDelete = articleSeriesRepository.findAll().size();

        // Delete the articleSeries
        restArticleSeriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleSeries.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArticleSeries> articleSeriesList = articleSeriesRepository.findAll();
        assertThat(articleSeriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
