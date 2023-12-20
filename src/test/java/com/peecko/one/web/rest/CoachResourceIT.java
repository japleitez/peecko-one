package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Coach;
import com.peecko.one.domain.enumeration.CoachType;
import com.peecko.one.repository.CoachRepository;
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
 * Integration tests for the {@link CoachResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoachResourceIT {

    private static final CoachType DEFAULT_TYPE = CoachType.FITNESS;
    private static final CoachType UPDATED_TYPE = CoachType.WELLNESS;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_SPEAKS = "AAAAAAAAAA";
    private static final String UPDATED_SPEAKS = "BBBBBBBBBB";

    private static final String DEFAULT_RESUME = "AAAAAAAAAA";
    private static final String UPDATED_RESUME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/coaches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoachMockMvc;

    private Coach coach;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coach createEntity(EntityManager em) {
        Coach coach = new Coach()
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE)
            .instagram(DEFAULT_INSTAGRAM)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .country(DEFAULT_COUNTRY)
            .speaks(DEFAULT_SPEAKS)
            .resume(DEFAULT_RESUME)
            .notes(DEFAULT_NOTES)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return coach;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coach createUpdatedEntity(EntityManager em) {
        Coach coach = new Coach()
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .instagram(UPDATED_INSTAGRAM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .country(UPDATED_COUNTRY)
            .speaks(UPDATED_SPEAKS)
            .resume(UPDATED_RESUME)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return coach;
    }

    @BeforeEach
    public void initTest() {
        coach = createEntity(em);
    }

    @Test
    @Transactional
    void createCoach() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();
        // Create the Coach
        restCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate + 1);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCoach.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoach.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCoach.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCoach.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testCoach.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCoach.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCoach.getSpeaks()).isEqualTo(DEFAULT_SPEAKS);
        assertThat(testCoach.getResume()).isEqualTo(DEFAULT_RESUME);
        assertThat(testCoach.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCoach.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCoach.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void createCoachWithExistingId() throws Exception {
        // Create the Coach with an existing ID
        coach.setId(1L);

        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isBadRequest());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = coachRepository.findAll().size();
        // set the field null
        coach.setType(null);

        // Create the Coach, which fails.

        restCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isBadRequest());

        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = coachRepository.findAll().size();
        // set the field null
        coach.setName(null);

        // Create the Coach, which fails.

        restCoachMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isBadRequest());

        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoaches() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get all the coachList
        restCoachMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coach.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].speaks").value(hasItem(DEFAULT_SPEAKS)))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(DEFAULT_RESUME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get the coach
        restCoachMockMvc
            .perform(get(ENTITY_API_URL_ID, coach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coach.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.speaks").value(DEFAULT_SPEAKS))
            .andExpect(jsonPath("$.resume").value(DEFAULT_RESUME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCoach() throws Exception {
        // Get the coach
        restCoachMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach
        Coach updatedCoach = coachRepository.findById(coach.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoach are not directly saved in db
        em.detach(updatedCoach);
        updatedCoach
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .instagram(UPDATED_INSTAGRAM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .country(UPDATED_COUNTRY)
            .speaks(UPDATED_SPEAKS)
            .resume(UPDATED_RESUME)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoach.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoach))
            )
            .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCoach.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoach.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCoach.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCoach.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testCoach.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCoach.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCoach.getSpeaks()).isEqualTo(UPDATED_SPEAKS);
        assertThat(testCoach.getResume()).isEqualTo(UPDATED_RESUME);
        assertThat(testCoach.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCoach.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCoach.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coach.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coach))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coach))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoachWithPatch() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach using partial update
        Coach partialUpdatedCoach = new Coach();
        partialUpdatedCoach.setId(coach.getId());

        partialUpdatedCoach.name(UPDATED_NAME).website(UPDATED_WEBSITE).notes(UPDATED_NOTES).created(UPDATED_CREATED);

        restCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoach))
            )
            .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCoach.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoach.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCoach.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCoach.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testCoach.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCoach.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCoach.getSpeaks()).isEqualTo(DEFAULT_SPEAKS);
        assertThat(testCoach.getResume()).isEqualTo(DEFAULT_RESUME);
        assertThat(testCoach.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCoach.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCoach.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateCoachWithPatch() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach using partial update
        Coach partialUpdatedCoach = new Coach();
        partialUpdatedCoach.setId(coach.getId());

        partialUpdatedCoach
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .instagram(UPDATED_INSTAGRAM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .country(UPDATED_COUNTRY)
            .speaks(UPDATED_SPEAKS)
            .resume(UPDATED_RESUME)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoach.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoach))
            )
            .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachList.get(coachList.size() - 1);
        assertThat(testCoach.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCoach.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoach.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCoach.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCoach.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testCoach.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCoach.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCoach.getSpeaks()).isEqualTo(UPDATED_SPEAKS);
        assertThat(testCoach.getResume()).isEqualTo(UPDATED_RESUME);
        assertThat(testCoach.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCoach.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCoach.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coach.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coach))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coach))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoach() throws Exception {
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();
        coach.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoachMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coach)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coach in the database
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        int databaseSizeBeforeDelete = coachRepository.findAll().size();

        // Delete the coach
        restCoachMockMvc
            .perform(delete(ENTITY_API_URL_ID, coach.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coach> coachList = coachRepository.findAll();
        assertThat(coachList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
