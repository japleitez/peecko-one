package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ArticleCategory;
import com.peecko.one.repository.ArticleCategoryRepository;
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
 * Integration tests for the {@link ArticleCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/article-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleCategoryMockMvc;

    private ArticleCategory articleCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleCategory createEntity(EntityManager em) {
        ArticleCategory articleCategory = new ArticleCategory()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .label(DEFAULT_LABEL)
            .created(DEFAULT_CREATED)
            .release(DEFAULT_RELEASE)
            .archived(DEFAULT_ARCHIVED);
        return articleCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleCategory createUpdatedEntity(EntityManager em) {
        ArticleCategory articleCategory = new ArticleCategory()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .release(UPDATED_RELEASE)
            .archived(UPDATED_ARCHIVED);
        return articleCategory;
    }

    @BeforeEach
    public void initTest() {
        articleCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createArticleCategory() throws Exception {
        int databaseSizeBeforeCreate = articleCategoryRepository.findAll().size();
        // Create the ArticleCategory
        restArticleCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticleCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticleCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testArticleCategory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testArticleCategory.getRelease()).isEqualTo(DEFAULT_RELEASE);
        assertThat(testArticleCategory.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void createArticleCategoryWithExistingId() throws Exception {
        // Create the ArticleCategory with an existing ID
        articleCategory.setId(1L);

        int databaseSizeBeforeCreate = articleCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleCategoryRepository.findAll().size();
        // set the field null
        articleCategory.setCode(null);

        // Create the ArticleCategory, which fails.

        restArticleCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleCategoryRepository.findAll().size();
        // set the field null
        articleCategory.setTitle(null);

        // Create the ArticleCategory, which fails.

        restArticleCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleCategoryRepository.findAll().size();
        // set the field null
        articleCategory.setLabel(null);

        // Create the ArticleCategory, which fails.

        restArticleCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticleCategories() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get all the articleCategoryList
        restArticleCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].release").value(hasItem(DEFAULT_RELEASE.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.toString())));
    }

    @Test
    @Transactional
    void getArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get the articleCategory
        restArticleCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, articleCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.release").value(DEFAULT_RELEASE.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArticleCategory() throws Exception {
        // Get the articleCategory
        restArticleCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Update the articleCategory
        ArticleCategory updatedArticleCategory = articleCategoryRepository.findById(articleCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticleCategory are not directly saved in db
        em.detach(updatedArticleCategory);
        updatedArticleCategory
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .release(UPDATED_RELEASE)
            .archived(UPDATED_ARCHIVED);

        restArticleCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArticleCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArticleCategory))
            )
            .andExpect(status().isOk());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticleCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticleCategory.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testArticleCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleCategory.getRelease()).isEqualTo(UPDATED_RELEASE);
        assertThat(testArticleCategory.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void putNonExistingArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleCategoryWithPatch() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Update the articleCategory using partial update
        ArticleCategory partialUpdatedArticleCategory = new ArticleCategory();
        partialUpdatedArticleCategory.setId(articleCategory.getId());

        partialUpdatedArticleCategory.title(UPDATED_TITLE).created(UPDATED_CREATED);

        restArticleCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleCategory))
            )
            .andExpect(status().isOk());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testArticleCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticleCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testArticleCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleCategory.getRelease()).isEqualTo(DEFAULT_RELEASE);
        assertThat(testArticleCategory.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void fullUpdateArticleCategoryWithPatch() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Update the articleCategory using partial update
        ArticleCategory partialUpdatedArticleCategory = new ArticleCategory();
        partialUpdatedArticleCategory.setId(articleCategory.getId());

        partialUpdatedArticleCategory
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .release(UPDATED_RELEASE)
            .archived(UPDATED_ARCHIVED);

        restArticleCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticleCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArticleCategory))
            )
            .andExpect(status().isOk());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testArticleCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticleCategory.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testArticleCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testArticleCategory.getRelease()).isEqualTo(UPDATED_RELEASE);
        assertThat(testArticleCategory.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void patchNonExistingArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();
        articleCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(articleCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        int databaseSizeBeforeDelete = articleCategoryRepository.findAll().size();

        // Delete the articleCategory
        restArticleCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, articleCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
