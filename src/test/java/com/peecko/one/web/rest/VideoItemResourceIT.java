package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.VideoItem;
import com.peecko.one.repository.VideoItemRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link VideoItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideoItemResourceIT {

    private static final String DEFAULT_PREVIOUS = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT = "AAAAAAAAAA";
    private static final String UPDATED_NEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/video-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideoItemRepository videoItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoItemMockMvc;

    private VideoItem videoItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoItem createEntity(EntityManager em) {
        VideoItem videoItem = new VideoItem().previous(DEFAULT_PREVIOUS).code(DEFAULT_CODE).next(DEFAULT_NEXT);
        return videoItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoItem createUpdatedEntity(EntityManager em) {
        VideoItem videoItem = new VideoItem().previous(UPDATED_PREVIOUS).code(UPDATED_CODE).next(UPDATED_NEXT);
        return videoItem;
    }

    @BeforeEach
    public void initTest() {
        videoItem = createEntity(em);
    }

    @Test
    @Transactional
    void createVideoItem() throws Exception {
        int databaseSizeBeforeCreate = videoItemRepository.findAll().size();
        // Create the VideoItem
        restVideoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoItem)))
            .andExpect(status().isCreated());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeCreate + 1);
        VideoItem testVideoItem = videoItemList.get(videoItemList.size() - 1);
        assertThat(testVideoItem.getPrevious()).isEqualTo(DEFAULT_PREVIOUS);
        assertThat(testVideoItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVideoItem.getNext()).isEqualTo(DEFAULT_NEXT);
    }

    @Test
    @Transactional
    void createVideoItemWithExistingId() throws Exception {
        // Create the VideoItem with an existing ID
        videoItem.setId(1L);

        int databaseSizeBeforeCreate = videoItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoItem)))
            .andExpect(status().isBadRequest());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVideoItems() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        // Get all the videoItemList
        restVideoItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].previous").value(hasItem(DEFAULT_PREVIOUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].next").value(hasItem(DEFAULT_NEXT)));
    }

    @Test
    @Transactional
    void getVideoItem() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        // Get the videoItem
        restVideoItemMockMvc
            .perform(get(ENTITY_API_URL_ID, videoItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videoItem.getId().intValue()))
            .andExpect(jsonPath("$.previous").value(DEFAULT_PREVIOUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.next").value(DEFAULT_NEXT));
    }

    @Test
    @Transactional
    void getNonExistingVideoItem() throws Exception {
        // Get the videoItem
        restVideoItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVideoItem() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();

        // Update the videoItem
        VideoItem updatedVideoItem = videoItemRepository.findById(videoItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVideoItem are not directly saved in db
        em.detach(updatedVideoItem);
        updatedVideoItem.previous(UPDATED_PREVIOUS).code(UPDATED_CODE).next(UPDATED_NEXT);

        restVideoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVideoItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVideoItem))
            )
            .andExpect(status().isOk());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
        VideoItem testVideoItem = videoItemList.get(videoItemList.size() - 1);
        assertThat(testVideoItem.getPrevious()).isEqualTo(UPDATED_PREVIOUS);
        assertThat(testVideoItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideoItem.getNext()).isEqualTo(UPDATED_NEXT);
    }

    @Test
    @Transactional
    void putNonExistingVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideoItemWithPatch() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();

        // Update the videoItem using partial update
        VideoItem partialUpdatedVideoItem = new VideoItem();
        partialUpdatedVideoItem.setId(videoItem.getId());

        partialUpdatedVideoItem.previous(UPDATED_PREVIOUS).next(UPDATED_NEXT);

        restVideoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoItem))
            )
            .andExpect(status().isOk());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
        VideoItem testVideoItem = videoItemList.get(videoItemList.size() - 1);
        assertThat(testVideoItem.getPrevious()).isEqualTo(UPDATED_PREVIOUS);
        assertThat(testVideoItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVideoItem.getNext()).isEqualTo(UPDATED_NEXT);
    }

    @Test
    @Transactional
    void fullUpdateVideoItemWithPatch() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();

        // Update the videoItem using partial update
        VideoItem partialUpdatedVideoItem = new VideoItem();
        partialUpdatedVideoItem.setId(videoItem.getId());

        partialUpdatedVideoItem.previous(UPDATED_PREVIOUS).code(UPDATED_CODE).next(UPDATED_NEXT);

        restVideoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoItem))
            )
            .andExpect(status().isOk());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
        VideoItem testVideoItem = videoItemList.get(videoItemList.size() - 1);
        assertThat(testVideoItem.getPrevious()).isEqualTo(UPDATED_PREVIOUS);
        assertThat(testVideoItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideoItem.getNext()).isEqualTo(UPDATED_NEXT);
    }

    @Test
    @Transactional
    void patchNonExistingVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videoItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideoItem() throws Exception {
        int databaseSizeBeforeUpdate = videoItemRepository.findAll().size();
        videoItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(videoItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoItem in the database
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideoItem() throws Exception {
        // Initialize the database
        videoItemRepository.saveAndFlush(videoItem);

        int databaseSizeBeforeDelete = videoItemRepository.findAll().size();

        // Delete the videoItem
        restVideoItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, videoItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VideoItem> videoItemList = videoItemRepository.findAll();
        assertThat(videoItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
