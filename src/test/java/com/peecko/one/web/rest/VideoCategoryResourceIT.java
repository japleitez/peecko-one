package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.VideoCategory;
import com.peecko.one.repository.VideoCategoryRepository;
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
 * Integration tests for the {@link VideoCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideoCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/video-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideoCategoryRepository videoCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoCategoryMockMvc;

    private VideoCategory videoCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoCategory createEntity(EntityManager em) {
        VideoCategory videoCategory = new VideoCategory()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .label(DEFAULT_LABEL)
            .created(DEFAULT_CREATED)
            .released(DEFAULT_RELEASED)
            .archived(DEFAULT_ARCHIVED);
        return videoCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoCategory createUpdatedEntity(EntityManager em) {
        VideoCategory videoCategory = new VideoCategory()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);
        return videoCategory;
    }

    @BeforeEach
    public void initTest() {
        videoCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createVideoCategory() throws Exception {
        int databaseSizeBeforeCreate = videoCategoryRepository.findAll().size();
        // Create the VideoCategory
        restVideoCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isCreated());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVideoCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideoCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testVideoCategory.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVideoCategory.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testVideoCategory.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void createVideoCategoryWithExistingId() throws Exception {
        // Create the VideoCategory with an existing ID
        videoCategory.setId(1L);

        int databaseSizeBeforeCreate = videoCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoCategoryRepository.findAll().size();
        // set the field null
        videoCategory.setCode(null);

        // Create the VideoCategory, which fails.

        restVideoCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoCategoryRepository.findAll().size();
        // set the field null
        videoCategory.setTitle(null);

        // Create the VideoCategory, which fails.

        restVideoCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoCategoryRepository.findAll().size();
        // set the field null
        videoCategory.setLabel(null);

        // Create the VideoCategory, which fails.

        restVideoCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideoCategories() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        // Get all the videoCategoryList
        restVideoCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].released").value(hasItem(DEFAULT_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.toString())));
    }

    @Test
    @Transactional
    void getVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        // Get the videoCategory
        restVideoCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, videoCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videoCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.released").value(DEFAULT_RELEASED.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVideoCategory() throws Exception {
        // Get the videoCategory
        restVideoCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();

        // Update the videoCategory
        VideoCategory updatedVideoCategory = videoCategoryRepository.findById(videoCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVideoCategory are not directly saved in db
        em.detach(updatedVideoCategory);
        updatedVideoCategory
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restVideoCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVideoCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVideoCategory))
            )
            .andExpect(status().isOk());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideoCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideoCategory.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testVideoCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVideoCategory.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideoCategory.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void putNonExistingVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideoCategoryWithPatch() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();

        // Update the videoCategory using partial update
        VideoCategory partialUpdatedVideoCategory = new VideoCategory();
        partialUpdatedVideoCategory.setId(videoCategory.getId());

        partialUpdatedVideoCategory
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restVideoCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoCategory))
            )
            .andExpect(status().isOk());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideoCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideoCategory.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testVideoCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVideoCategory.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideoCategory.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void fullUpdateVideoCategoryWithPatch() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();

        // Update the videoCategory using partial update
        VideoCategory partialUpdatedVideoCategory = new VideoCategory();
        partialUpdatedVideoCategory.setId(videoCategory.getId());

        partialUpdatedVideoCategory
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restVideoCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoCategory))
            )
            .andExpect(status().isOk());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideoCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideoCategory.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testVideoCategory.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVideoCategory.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideoCategory.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void patchNonExistingVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videoCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();
        videoCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(videoCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        int databaseSizeBeforeDelete = videoCategoryRepository.findAll().size();

        // Delete the videoCategory
        restVideoCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, videoCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
