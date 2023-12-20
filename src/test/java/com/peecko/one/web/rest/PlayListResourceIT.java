package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.PlayList;
import com.peecko.one.repository.PlayListRepository;
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
 * Integration tests for the {@link PlayListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNTER = 1;
    private static final Integer UPDATED_COUNTER = 2;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/play-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayListRepository playListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayListMockMvc;

    private PlayList playList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayList createEntity(EntityManager em) {
        PlayList playList = new PlayList().name(DEFAULT_NAME).counter(DEFAULT_COUNTER).created(DEFAULT_CREATED).updated(DEFAULT_UPDATED);
        return playList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayList createUpdatedEntity(EntityManager em) {
        PlayList playList = new PlayList().name(UPDATED_NAME).counter(UPDATED_COUNTER).created(UPDATED_CREATED).updated(UPDATED_UPDATED);
        return playList;
    }

    @BeforeEach
    public void initTest() {
        playList = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayList() throws Exception {
        int databaseSizeBeforeCreate = playListRepository.findAll().size();
        // Create the PlayList
        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isCreated());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeCreate + 1);
        PlayList testPlayList = playListList.get(playListList.size() - 1);
        assertThat(testPlayList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlayList.getCounter()).isEqualTo(DEFAULT_COUNTER);
        assertThat(testPlayList.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPlayList.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void createPlayListWithExistingId() throws Exception {
        // Create the PlayList with an existing ID
        playList.setId(1L);

        int databaseSizeBeforeCreate = playListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isBadRequest());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playListRepository.findAll().size();
        // set the field null
        playList.setName(null);

        // Create the PlayList, which fails.

        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isBadRequest());

        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCounterIsRequired() throws Exception {
        int databaseSizeBeforeTest = playListRepository.findAll().size();
        // set the field null
        playList.setCounter(null);

        // Create the PlayList, which fails.

        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isBadRequest());

        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = playListRepository.findAll().size();
        // set the field null
        playList.setCreated(null);

        // Create the PlayList, which fails.

        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isBadRequest());

        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = playListRepository.findAll().size();
        // set the field null
        playList.setUpdated(null);

        // Create the PlayList, which fails.

        restPlayListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isBadRequest());

        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayLists() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        // Get all the playListList
        restPlayListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].counter").value(hasItem(DEFAULT_COUNTER)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getPlayList() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        // Get the playList
        restPlayListMockMvc
            .perform(get(ENTITY_API_URL_ID, playList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.counter").value(DEFAULT_COUNTER))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayList() throws Exception {
        // Get the playList
        restPlayListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayList() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        int databaseSizeBeforeUpdate = playListRepository.findAll().size();

        // Update the playList
        PlayList updatedPlayList = playListRepository.findById(playList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlayList are not directly saved in db
        em.detach(updatedPlayList);
        updatedPlayList.name(UPDATED_NAME).counter(UPDATED_COUNTER).created(UPDATED_CREATED).updated(UPDATED_UPDATED);

        restPlayListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayList))
            )
            .andExpect(status().isOk());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
        PlayList testPlayList = playListList.get(playListList.size() - 1);
        assertThat(testPlayList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlayList.getCounter()).isEqualTo(UPDATED_COUNTER);
        assertThat(testPlayList.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPlayList.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayListWithPatch() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        int databaseSizeBeforeUpdate = playListRepository.findAll().size();

        // Update the playList using partial update
        PlayList partialUpdatedPlayList = new PlayList();
        partialUpdatedPlayList.setId(playList.getId());

        partialUpdatedPlayList.counter(UPDATED_COUNTER).created(UPDATED_CREATED);

        restPlayListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayList))
            )
            .andExpect(status().isOk());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
        PlayList testPlayList = playListList.get(playListList.size() - 1);
        assertThat(testPlayList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlayList.getCounter()).isEqualTo(UPDATED_COUNTER);
        assertThat(testPlayList.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPlayList.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdatePlayListWithPatch() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        int databaseSizeBeforeUpdate = playListRepository.findAll().size();

        // Update the playList using partial update
        PlayList partialUpdatedPlayList = new PlayList();
        partialUpdatedPlayList.setId(playList.getId());

        partialUpdatedPlayList.name(UPDATED_NAME).counter(UPDATED_COUNTER).created(UPDATED_CREATED).updated(UPDATED_UPDATED);

        restPlayListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayList))
            )
            .andExpect(status().isOk());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
        PlayList testPlayList = playListList.get(playListList.size() - 1);
        assertThat(testPlayList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlayList.getCounter()).isEqualTo(UPDATED_COUNTER);
        assertThat(testPlayList.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPlayList.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayList() throws Exception {
        int databaseSizeBeforeUpdate = playListRepository.findAll().size();
        playList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayList in the database
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayList() throws Exception {
        // Initialize the database
        playListRepository.saveAndFlush(playList);

        int databaseSizeBeforeDelete = playListRepository.findAll().size();

        // Delete the playList
        restPlayListMockMvc
            .perform(delete(ENTITY_API_URL_ID, playList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayList> playListList = playListRepository.findAll();
        assertThat(playListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
