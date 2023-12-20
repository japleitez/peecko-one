package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Article;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.repository.ArticleRepository;
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
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleResourceIT {

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

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    private static final String DEFAULT_AUDIO_URL = "AAAAAAAAAA";
    private static final String UPDATED_AUDIO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_SERIES_ID = 1L;
    private static final Long UPDATED_SERIES_ID = 2L;

    private static final Integer DEFAULT_CHAPTER = 1;
    private static final Integer UPDATED_CHAPTER = 2;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .subtitle(DEFAULT_SUBTITLE)
            .summary(DEFAULT_SUMMARY)
            .language(DEFAULT_LANGUAGE)
            .tags(DEFAULT_TAGS)
            .duration(DEFAULT_DURATION)
            .thumbnail(DEFAULT_THUMBNAIL)
            .audioUrl(DEFAULT_AUDIO_URL)
            .content(DEFAULT_CONTENT)
            .seriesId(DEFAULT_SERIES_ID)
            .chapter(DEFAULT_CHAPTER)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .released(DEFAULT_RELEASED)
            .archived(DEFAULT_ARCHIVED);
        return article;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .duration(UPDATED_DURATION)
            .thumbnail(UPDATED_THUMBNAIL)
            .audioUrl(UPDATED_AUDIO_URL)
            .content(UPDATED_CONTENT)
            .seriesId(UPDATED_SERIES_ID)
            .chapter(UPDATED_CHAPTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();
        // Create the Article
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticle.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
        assertThat(testArticle.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testArticle.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testArticle.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testArticle.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testArticle.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testArticle.getAudioUrl()).isEqualTo(DEFAULT_AUDIO_URL);
        assertThat(testArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticle.getSeriesId()).isEqualTo(DEFAULT_SERIES_ID);
        assertThat(testArticle.getChapter()).isEqualTo(DEFAULT_CHAPTER);
        assertThat(testArticle.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testArticle.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testArticle.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testArticle.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void createArticleWithExistingId() throws Exception {
        // Create the Article with an existing ID
        article.setId(1L);

        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setCode(null);

        // Create the Article, which fails.

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setTitle(null);

        // Create the Article, which fails.

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setLanguage(null);

        // Create the Article, which fails.

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.[*].audioUrl").value(hasItem(DEFAULT_AUDIO_URL)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].seriesId").value(hasItem(DEFAULT_SERIES_ID.intValue())))
            .andExpect(jsonPath("$.[*].chapter").value(hasItem(DEFAULT_CHAPTER)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].released").value(hasItem(DEFAULT_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.toString())));
    }

    @Test
    @Transactional
    void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL))
            .andExpect(jsonPath("$.audioUrl").value(DEFAULT_AUDIO_URL))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.seriesId").value(DEFAULT_SERIES_ID.intValue()))
            .andExpect(jsonPath("$.chapter").value(DEFAULT_CHAPTER))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.released").value(DEFAULT_RELEASED.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .duration(UPDATED_DURATION)
            .thumbnail(UPDATED_THUMBNAIL)
            .audioUrl(UPDATED_AUDIO_URL)
            .content(UPDATED_CONTENT)
            .seriesId(UPDATED_SERIES_ID)
            .chapter(UPDATED_CHAPTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticle.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticle.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testArticle.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArticle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testArticle.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testArticle.getAudioUrl()).isEqualTo(UPDATED_AUDIO_URL);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticle.getSeriesId()).isEqualTo(UPDATED_SERIES_ID);
        assertThat(testArticle.getChapter()).isEqualTo(UPDATED_CHAPTER);
        assertThat(testArticle.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticle.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticle.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testArticle.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void putNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .tags(UPDATED_TAGS)
            .audioUrl(UPDATED_AUDIO_URL)
            .content(UPDATED_CONTENT)
            .chapter(UPDATED_CHAPTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .archived(UPDATED_ARCHIVED);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticle.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticle.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testArticle.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArticle.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testArticle.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testArticle.getAudioUrl()).isEqualTo(UPDATED_AUDIO_URL);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticle.getSeriesId()).isEqualTo(DEFAULT_SERIES_ID);
        assertThat(testArticle.getChapter()).isEqualTo(UPDATED_CHAPTER);
        assertThat(testArticle.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticle.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticle.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testArticle.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void fullUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .summary(UPDATED_SUMMARY)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .duration(UPDATED_DURATION)
            .thumbnail(UPDATED_THUMBNAIL)
            .audioUrl(UPDATED_AUDIO_URL)
            .content(UPDATED_CONTENT)
            .seriesId(UPDATED_SERIES_ID)
            .chapter(UPDATED_CHAPTER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testArticle.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testArticle.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testArticle.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testArticle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testArticle.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testArticle.getAudioUrl()).isEqualTo(UPDATED_AUDIO_URL);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticle.getSeriesId()).isEqualTo(UPDATED_SERIES_ID);
        assertThat(testArticle.getChapter()).isEqualTo(UPDATED_CHAPTER);
        assertThat(testArticle.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticle.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testArticle.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testArticle.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void patchNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, article.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, article.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
