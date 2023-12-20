package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ApsUser;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.repository.ApsUserRepository;
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
 * Integration tests for the {@link ApsUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USERNAME_VERIFIED = false;
    private static final Boolean UPDATED_USERNAME_VERIFIED = true;

    private static final String DEFAULT_PRIVATE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PRIVATE_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRIVATE_VERIFIED = false;
    private static final Boolean UPDATED_PRIVATE_VERIFIED = true;

    private static final Language DEFAULT_LANGUAGE = Language.EN;
    private static final Language UPDATED_LANGUAGE = Language.FR;

    private static final String DEFAULT_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/aps-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsUserRepository apsUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsUserMockMvc;

    private ApsUser apsUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsUser createEntity(EntityManager em) {
        ApsUser apsUser = new ApsUser()
            .name(DEFAULT_NAME)
            .username(DEFAULT_USERNAME)
            .usernameVerified(DEFAULT_USERNAME_VERIFIED)
            .privateEmail(DEFAULT_PRIVATE_EMAIL)
            .privateVerified(DEFAULT_PRIVATE_VERIFIED)
            .language(DEFAULT_LANGUAGE)
            .license(DEFAULT_LICENSE)
            .active(DEFAULT_ACTIVE)
            .password(DEFAULT_PASSWORD)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return apsUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsUser createUpdatedEntity(EntityManager em) {
        ApsUser apsUser = new ApsUser()
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .usernameVerified(UPDATED_USERNAME_VERIFIED)
            .privateEmail(UPDATED_PRIVATE_EMAIL)
            .privateVerified(UPDATED_PRIVATE_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .license(UPDATED_LICENSE)
            .active(UPDATED_ACTIVE)
            .password(UPDATED_PASSWORD)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return apsUser;
    }

    @BeforeEach
    public void initTest() {
        apsUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApsUser() throws Exception {
        int databaseSizeBeforeCreate = apsUserRepository.findAll().size();
        // Create the ApsUser
        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isCreated());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApsUser testApsUser = apsUserList.get(apsUserList.size() - 1);
        assertThat(testApsUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApsUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testApsUser.getUsernameVerified()).isEqualTo(DEFAULT_USERNAME_VERIFIED);
        assertThat(testApsUser.getPrivateEmail()).isEqualTo(DEFAULT_PRIVATE_EMAIL);
        assertThat(testApsUser.getPrivateVerified()).isEqualTo(DEFAULT_PRIVATE_VERIFIED);
        assertThat(testApsUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testApsUser.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testApsUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testApsUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testApsUser.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testApsUser.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void createApsUserWithExistingId() throws Exception {
        // Create the ApsUser with an existing ID
        apsUser.setId(1L);

        int databaseSizeBeforeCreate = apsUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setName(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setUsername(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameVerifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setUsernameVerified(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrivateEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setPrivateEmail(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrivateVerifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setPrivateVerified(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setLanguage(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsUserRepository.findAll().size();
        // set the field null
        apsUser.setActive(null);

        // Create the ApsUser, which fails.

        restApsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isBadRequest());

        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsUsers() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        // Get all the apsUserList
        restApsUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].usernameVerified").value(hasItem(DEFAULT_USERNAME_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].privateEmail").value(hasItem(DEFAULT_PRIVATE_EMAIL)))
            .andExpect(jsonPath("$.[*].privateVerified").value(hasItem(DEFAULT_PRIVATE_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].license").value(hasItem(DEFAULT_LICENSE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getApsUser() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        // Get the apsUser
        restApsUserMockMvc
            .perform(get(ENTITY_API_URL_ID, apsUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.usernameVerified").value(DEFAULT_USERNAME_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.privateEmail").value(DEFAULT_PRIVATE_EMAIL))
            .andExpect(jsonPath("$.privateVerified").value(DEFAULT_PRIVATE_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.license").value(DEFAULT_LICENSE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApsUser() throws Exception {
        // Get the apsUser
        restApsUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsUser() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();

        // Update the apsUser
        ApsUser updatedApsUser = apsUserRepository.findById(apsUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsUser are not directly saved in db
        em.detach(updatedApsUser);
        updatedApsUser
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .usernameVerified(UPDATED_USERNAME_VERIFIED)
            .privateEmail(UPDATED_PRIVATE_EMAIL)
            .privateVerified(UPDATED_PRIVATE_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .license(UPDATED_LICENSE)
            .active(UPDATED_ACTIVE)
            .password(UPDATED_PASSWORD)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restApsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsUser))
            )
            .andExpect(status().isOk());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
        ApsUser testApsUser = apsUserList.get(apsUserList.size() - 1);
        assertThat(testApsUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApsUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testApsUser.getUsernameVerified()).isEqualTo(UPDATED_USERNAME_VERIFIED);
        assertThat(testApsUser.getPrivateEmail()).isEqualTo(UPDATED_PRIVATE_EMAIL);
        assertThat(testApsUser.getPrivateVerified()).isEqualTo(UPDATED_PRIVATE_VERIFIED);
        assertThat(testApsUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testApsUser.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testApsUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testApsUser.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testApsUser.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsUserWithPatch() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();

        // Update the apsUser using partial update
        ApsUser partialUpdatedApsUser = new ApsUser();
        partialUpdatedApsUser.setId(apsUser.getId());

        partialUpdatedApsUser
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .privateEmail(UPDATED_PRIVATE_EMAIL)
            .privateVerified(UPDATED_PRIVATE_VERIFIED)
            .created(UPDATED_CREATED);

        restApsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsUser))
            )
            .andExpect(status().isOk());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
        ApsUser testApsUser = apsUserList.get(apsUserList.size() - 1);
        assertThat(testApsUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApsUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testApsUser.getUsernameVerified()).isEqualTo(DEFAULT_USERNAME_VERIFIED);
        assertThat(testApsUser.getPrivateEmail()).isEqualTo(UPDATED_PRIVATE_EMAIL);
        assertThat(testApsUser.getPrivateVerified()).isEqualTo(UPDATED_PRIVATE_VERIFIED);
        assertThat(testApsUser.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testApsUser.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testApsUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testApsUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testApsUser.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testApsUser.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateApsUserWithPatch() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();

        // Update the apsUser using partial update
        ApsUser partialUpdatedApsUser = new ApsUser();
        partialUpdatedApsUser.setId(apsUser.getId());

        partialUpdatedApsUser
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .usernameVerified(UPDATED_USERNAME_VERIFIED)
            .privateEmail(UPDATED_PRIVATE_EMAIL)
            .privateVerified(UPDATED_PRIVATE_VERIFIED)
            .language(UPDATED_LANGUAGE)
            .license(UPDATED_LICENSE)
            .active(UPDATED_ACTIVE)
            .password(UPDATED_PASSWORD)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restApsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsUser))
            )
            .andExpect(status().isOk());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
        ApsUser testApsUser = apsUserList.get(apsUserList.size() - 1);
        assertThat(testApsUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApsUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testApsUser.getUsernameVerified()).isEqualTo(UPDATED_USERNAME_VERIFIED);
        assertThat(testApsUser.getPrivateEmail()).isEqualTo(UPDATED_PRIVATE_EMAIL);
        assertThat(testApsUser.getPrivateVerified()).isEqualTo(UPDATED_PRIVATE_VERIFIED);
        assertThat(testApsUser.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testApsUser.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testApsUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testApsUser.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testApsUser.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsUser() throws Exception {
        int databaseSizeBeforeUpdate = apsUserRepository.findAll().size();
        apsUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsUser in the database
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsUser() throws Exception {
        // Initialize the database
        apsUserRepository.saveAndFlush(apsUser);

        int databaseSizeBeforeDelete = apsUserRepository.findAll().size();

        // Delete the apsUser
        restApsUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsUser> apsUserList = apsUserRepository.findAll();
        assertThat(apsUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
