package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Video;
import com.peecko.one.domain.enumeration.Intensity;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.domain.enumeration.Player;
import com.peecko.one.repository.VideoRepository;
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
 * Integration tests for the {@link VideoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideoResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Language DEFAULT_LANGUAGE = Language.EN;
    private static final Language UPDATED_LANGUAGE = Language.FR;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final Player DEFAULT_PLAYER = Player.PEECKO;
    private static final Player UPDATED_PLAYER = Player.YOUTUBE;

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_AUDIENCE = "AAAAAAAAAA";
    private static final String UPDATED_AUDIENCE = "BBBBBBBBBB";

    private static final Intensity DEFAULT_INTENSITY = Intensity.BEGINNER;
    private static final Intensity UPDATED_INTENSITY = Intensity.INTERMEDIATE;

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARCHIVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/videos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoMockMvc;

    private Video video;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createEntity(EntityManager em) {
        Video video = new Video()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .duration(DEFAULT_DURATION)
            .language(DEFAULT_LANGUAGE)
            .tags(DEFAULT_TAGS)
            .player(DEFAULT_PLAYER)
            .thumbnail(DEFAULT_THUMBNAIL)
            .url(DEFAULT_URL)
            .audience(DEFAULT_AUDIENCE)
            .intensity(DEFAULT_INTENSITY)
            .filename(DEFAULT_FILENAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .released(DEFAULT_RELEASED)
            .archived(DEFAULT_ARCHIVED);
        return video;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createUpdatedEntity(EntityManager em) {
        Video video = new Video()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .player(UPDATED_PLAYER)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL)
            .audience(UPDATED_AUDIENCE)
            .intensity(UPDATED_INTENSITY)
            .filename(UPDATED_FILENAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);
        return video;
    }

    @BeforeEach
    public void initTest() {
        video = createEntity(em);
    }

    @Test
    @Transactional
    void createVideo() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();
        // Create the Video
        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate + 1);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVideo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideo.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideo.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testVideo.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testVideo.getPlayer()).isEqualTo(DEFAULT_PLAYER);
        assertThat(testVideo.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testVideo.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testVideo.getAudience()).isEqualTo(DEFAULT_AUDIENCE);
        assertThat(testVideo.getIntensity()).isEqualTo(DEFAULT_INTENSITY);
        assertThat(testVideo.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testVideo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVideo.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testVideo.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void createVideoWithExistingId() throws Exception {
        // Create the Video with an existing ID
        video.setId(1L);

        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setCode(null);

        // Create the Video, which fails.

        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setTitle(null);

        // Create the Video, which fails.

        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setLanguage(null);

        // Create the Video, which fails.

        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlayerIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setPlayer(null);

        // Create the Video, which fails.

        restVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videoList
        restVideoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].player").value(hasItem(DEFAULT_PLAYER.toString())))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].audience").value(hasItem(DEFAULT_AUDIENCE)))
            .andExpect(jsonPath("$.[*].intensity").value(hasItem(DEFAULT_INTENSITY.toString())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].released").value(hasItem(DEFAULT_RELEASED.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.toString())));
    }

    @Test
    @Transactional
    void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc
            .perform(get(ENTITY_API_URL_ID, video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.player").value(DEFAULT_PLAYER.toString()))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.audience").value(DEFAULT_AUDIENCE))
            .andExpect(jsonPath("$.intensity").value(DEFAULT_INTENSITY.toString()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.released").value(DEFAULT_RELEASED.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video
        Video updatedVideo = videoRepository.findById(video.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVideo are not directly saved in db
        em.detach(updatedVideo);
        updatedVideo
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .player(UPDATED_PLAYER)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL)
            .audience(UPDATED_AUDIENCE)
            .intensity(UPDATED_INTENSITY)
            .filename(UPDATED_FILENAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVideo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVideo))
            )
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideo.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testVideo.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testVideo.getPlayer()).isEqualTo(UPDATED_PLAYER);
        assertThat(testVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testVideo.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVideo.getAudience()).isEqualTo(UPDATED_AUDIENCE);
        assertThat(testVideo.getIntensity()).isEqualTo(UPDATED_INTENSITY);
        assertThat(testVideo.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testVideo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVideo.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideo.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void putNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, video.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(video))
            )
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(video))
            )
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideoWithPatch() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video using partial update
        Video partialUpdatedVideo = new Video();
        partialUpdatedVideo.setId(video.getId());

        partialUpdatedVideo
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .tags(UPDATED_TAGS)
            .player(UPDATED_PLAYER)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL)
            .audience(UPDATED_AUDIENCE)
            .intensity(UPDATED_INTENSITY)
            .released(UPDATED_RELEASED);

        restVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideo))
            )
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideo.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testVideo.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testVideo.getPlayer()).isEqualTo(UPDATED_PLAYER);
        assertThat(testVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testVideo.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVideo.getAudience()).isEqualTo(UPDATED_AUDIENCE);
        assertThat(testVideo.getIntensity()).isEqualTo(UPDATED_INTENSITY);
        assertThat(testVideo.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testVideo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideo.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVideo.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideo.getArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    void fullUpdateVideoWithPatch() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video using partial update
        Video partialUpdatedVideo = new Video();
        partialUpdatedVideo.setId(video.getId());

        partialUpdatedVideo
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .language(UPDATED_LANGUAGE)
            .tags(UPDATED_TAGS)
            .player(UPDATED_PLAYER)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL)
            .audience(UPDATED_AUDIENCE)
            .intensity(UPDATED_INTENSITY)
            .filename(UPDATED_FILENAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .released(UPDATED_RELEASED)
            .archived(UPDATED_ARCHIVED);

        restVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideo))
            )
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideo.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testVideo.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testVideo.getPlayer()).isEqualTo(UPDATED_PLAYER);
        assertThat(testVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testVideo.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVideo.getAudience()).isEqualTo(UPDATED_AUDIENCE);
        assertThat(testVideo.getIntensity()).isEqualTo(UPDATED_INTENSITY);
        assertThat(testVideo.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testVideo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideo.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVideo.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testVideo.getArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    void patchNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, video.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(video))
            )
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(video))
            )
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();
        video.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeDelete = videoRepository.findAll().size();

        // Delete the video
        restVideoMockMvc
            .perform(delete(ENTITY_API_URL_ID, video.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
