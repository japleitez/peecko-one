package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ApsMembership;
import com.peecko.one.repository.ApsMembershipRepository;
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
 * Integration tests for the {@link ApsMembershipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsMembershipResourceIT {

    private static final Integer DEFAULT_PERIOD = 1;
    private static final Integer UPDATED_PERIOD = 2;

    private static final String DEFAULT_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aps-memberships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsMembershipRepository apsMembershipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsMembershipMockMvc;

    private ApsMembership apsMembership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsMembership createEntity(EntityManager em) {
        ApsMembership apsMembership = new ApsMembership().period(DEFAULT_PERIOD).license(DEFAULT_LICENSE).username(DEFAULT_USERNAME);
        return apsMembership;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsMembership createUpdatedEntity(EntityManager em) {
        ApsMembership apsMembership = new ApsMembership().period(UPDATED_PERIOD).license(UPDATED_LICENSE).username(UPDATED_USERNAME);
        return apsMembership;
    }

    @BeforeEach
    public void initTest() {
        apsMembership = createEntity(em);
    }

    @Test
    @Transactional
    void createApsMembership() throws Exception {
        int databaseSizeBeforeCreate = apsMembershipRepository.findAll().size();
        // Create the ApsMembership
        restApsMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isCreated());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeCreate + 1);
        ApsMembership testApsMembership = apsMembershipList.get(apsMembershipList.size() - 1);
        assertThat(testApsMembership.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testApsMembership.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testApsMembership.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void createApsMembershipWithExistingId() throws Exception {
        // Create the ApsMembership with an existing ID
        apsMembership.setId(1L);

        int databaseSizeBeforeCreate = apsMembershipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isBadRequest());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsMembershipRepository.findAll().size();
        // set the field null
        apsMembership.setPeriod(null);

        // Create the ApsMembership, which fails.

        restApsMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isBadRequest());

        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsMembershipRepository.findAll().size();
        // set the field null
        apsMembership.setLicense(null);

        // Create the ApsMembership, which fails.

        restApsMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isBadRequest());

        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsMembershipRepository.findAll().size();
        // set the field null
        apsMembership.setUsername(null);

        // Create the ApsMembership, which fails.

        restApsMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isBadRequest());

        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsMemberships() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        // Get all the apsMembershipList
        restApsMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsMembership.getId().intValue())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)))
            .andExpect(jsonPath("$.[*].license").value(hasItem(DEFAULT_LICENSE)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getApsMembership() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        // Get the apsMembership
        restApsMembershipMockMvc
            .perform(get(ENTITY_API_URL_ID, apsMembership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsMembership.getId().intValue()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD))
            .andExpect(jsonPath("$.license").value(DEFAULT_LICENSE))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getNonExistingApsMembership() throws Exception {
        // Get the apsMembership
        restApsMembershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsMembership() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();

        // Update the apsMembership
        ApsMembership updatedApsMembership = apsMembershipRepository.findById(apsMembership.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsMembership are not directly saved in db
        em.detach(updatedApsMembership);
        updatedApsMembership.period(UPDATED_PERIOD).license(UPDATED_LICENSE).username(UPDATED_USERNAME);

        restApsMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsMembership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsMembership))
            )
            .andExpect(status().isOk());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
        ApsMembership testApsMembership = apsMembershipList.get(apsMembershipList.size() - 1);
        assertThat(testApsMembership.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsMembership.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsMembership.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void putNonExistingApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsMembership.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsMembership)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsMembershipWithPatch() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();

        // Update the apsMembership using partial update
        ApsMembership partialUpdatedApsMembership = new ApsMembership();
        partialUpdatedApsMembership.setId(apsMembership.getId());

        partialUpdatedApsMembership.period(UPDATED_PERIOD).license(UPDATED_LICENSE).username(UPDATED_USERNAME);

        restApsMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsMembership))
            )
            .andExpect(status().isOk());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
        ApsMembership testApsMembership = apsMembershipList.get(apsMembershipList.size() - 1);
        assertThat(testApsMembership.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsMembership.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsMembership.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void fullUpdateApsMembershipWithPatch() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();

        // Update the apsMembership using partial update
        ApsMembership partialUpdatedApsMembership = new ApsMembership();
        partialUpdatedApsMembership.setId(apsMembership.getId());

        partialUpdatedApsMembership.period(UPDATED_PERIOD).license(UPDATED_LICENSE).username(UPDATED_USERNAME);

        restApsMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsMembership))
            )
            .andExpect(status().isOk());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
        ApsMembership testApsMembership = apsMembershipList.get(apsMembershipList.size() - 1);
        assertThat(testApsMembership.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsMembership.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsMembership.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void patchNonExistingApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsMembership))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsMembership() throws Exception {
        int databaseSizeBeforeUpdate = apsMembershipRepository.findAll().size();
        apsMembership.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsMembership))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsMembership in the database
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsMembership() throws Exception {
        // Initialize the database
        apsMembershipRepository.saveAndFlush(apsMembership);

        int databaseSizeBeforeDelete = apsMembershipRepository.findAll().size();

        // Delete the apsMembership
        restApsMembershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsMembership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsMembership> apsMembershipList = apsMembershipRepository.findAll();
        assertThat(apsMembershipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
