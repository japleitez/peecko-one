package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ApsDevice;
import com.peecko.one.repository.ApsDeviceRepository;
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
 * Integration tests for the {@link ApsDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsDeviceResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_OS_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_OS_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_INSTALLED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTALLED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/aps-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsDeviceRepository apsDeviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsDeviceMockMvc;

    private ApsDevice apsDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsDevice createEntity(EntityManager em) {
        ApsDevice apsDevice = new ApsDevice()
            .username(DEFAULT_USERNAME)
            .deviceId(DEFAULT_DEVICE_ID)
            .phoneModel(DEFAULT_PHONE_MODEL)
            .osVersion(DEFAULT_OS_VERSION)
            .installedOn(DEFAULT_INSTALLED_ON);
        return apsDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsDevice createUpdatedEntity(EntityManager em) {
        ApsDevice apsDevice = new ApsDevice()
            .username(UPDATED_USERNAME)
            .deviceId(UPDATED_DEVICE_ID)
            .phoneModel(UPDATED_PHONE_MODEL)
            .osVersion(UPDATED_OS_VERSION)
            .installedOn(UPDATED_INSTALLED_ON);
        return apsDevice;
    }

    @BeforeEach
    public void initTest() {
        apsDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createApsDevice() throws Exception {
        int databaseSizeBeforeCreate = apsDeviceRepository.findAll().size();
        // Create the ApsDevice
        restApsDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsDevice)))
            .andExpect(status().isCreated());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        ApsDevice testApsDevice = apsDeviceList.get(apsDeviceList.size() - 1);
        assertThat(testApsDevice.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testApsDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testApsDevice.getPhoneModel()).isEqualTo(DEFAULT_PHONE_MODEL);
        assertThat(testApsDevice.getOsVersion()).isEqualTo(DEFAULT_OS_VERSION);
        assertThat(testApsDevice.getInstalledOn()).isEqualTo(DEFAULT_INSTALLED_ON);
    }

    @Test
    @Transactional
    void createApsDeviceWithExistingId() throws Exception {
        // Create the ApsDevice with an existing ID
        apsDevice.setId(1L);

        int databaseSizeBeforeCreate = apsDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsDevice)))
            .andExpect(status().isBadRequest());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsDeviceRepository.findAll().size();
        // set the field null
        apsDevice.setUsername(null);

        // Create the ApsDevice, which fails.

        restApsDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsDevice)))
            .andExpect(status().isBadRequest());

        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsDeviceRepository.findAll().size();
        // set the field null
        apsDevice.setDeviceId(null);

        // Create the ApsDevice, which fails.

        restApsDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsDevice)))
            .andExpect(status().isBadRequest());

        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsDevices() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        // Get all the apsDeviceList
        restApsDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].phoneModel").value(hasItem(DEFAULT_PHONE_MODEL)))
            .andExpect(jsonPath("$.[*].osVersion").value(hasItem(DEFAULT_OS_VERSION)))
            .andExpect(jsonPath("$.[*].installedOn").value(hasItem(DEFAULT_INSTALLED_ON.toString())));
    }

    @Test
    @Transactional
    void getApsDevice() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        // Get the apsDevice
        restApsDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, apsDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsDevice.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.phoneModel").value(DEFAULT_PHONE_MODEL))
            .andExpect(jsonPath("$.osVersion").value(DEFAULT_OS_VERSION))
            .andExpect(jsonPath("$.installedOn").value(DEFAULT_INSTALLED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApsDevice() throws Exception {
        // Get the apsDevice
        restApsDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsDevice() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();

        // Update the apsDevice
        ApsDevice updatedApsDevice = apsDeviceRepository.findById(apsDevice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsDevice are not directly saved in db
        em.detach(updatedApsDevice);
        updatedApsDevice
            .username(UPDATED_USERNAME)
            .deviceId(UPDATED_DEVICE_ID)
            .phoneModel(UPDATED_PHONE_MODEL)
            .osVersion(UPDATED_OS_VERSION)
            .installedOn(UPDATED_INSTALLED_ON);

        restApsDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsDevice))
            )
            .andExpect(status().isOk());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
        ApsDevice testApsDevice = apsDeviceList.get(apsDeviceList.size() - 1);
        assertThat(testApsDevice.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testApsDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testApsDevice.getPhoneModel()).isEqualTo(UPDATED_PHONE_MODEL);
        assertThat(testApsDevice.getOsVersion()).isEqualTo(UPDATED_OS_VERSION);
        assertThat(testApsDevice.getInstalledOn()).isEqualTo(UPDATED_INSTALLED_ON);
    }

    @Test
    @Transactional
    void putNonExistingApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsDevice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsDeviceWithPatch() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();

        // Update the apsDevice using partial update
        ApsDevice partialUpdatedApsDevice = new ApsDevice();
        partialUpdatedApsDevice.setId(apsDevice.getId());

        partialUpdatedApsDevice.phoneModel(UPDATED_PHONE_MODEL).installedOn(UPDATED_INSTALLED_ON);

        restApsDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsDevice))
            )
            .andExpect(status().isOk());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
        ApsDevice testApsDevice = apsDeviceList.get(apsDeviceList.size() - 1);
        assertThat(testApsDevice.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testApsDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testApsDevice.getPhoneModel()).isEqualTo(UPDATED_PHONE_MODEL);
        assertThat(testApsDevice.getOsVersion()).isEqualTo(DEFAULT_OS_VERSION);
        assertThat(testApsDevice.getInstalledOn()).isEqualTo(UPDATED_INSTALLED_ON);
    }

    @Test
    @Transactional
    void fullUpdateApsDeviceWithPatch() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();

        // Update the apsDevice using partial update
        ApsDevice partialUpdatedApsDevice = new ApsDevice();
        partialUpdatedApsDevice.setId(apsDevice.getId());

        partialUpdatedApsDevice
            .username(UPDATED_USERNAME)
            .deviceId(UPDATED_DEVICE_ID)
            .phoneModel(UPDATED_PHONE_MODEL)
            .osVersion(UPDATED_OS_VERSION)
            .installedOn(UPDATED_INSTALLED_ON);

        restApsDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsDevice))
            )
            .andExpect(status().isOk());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
        ApsDevice testApsDevice = apsDeviceList.get(apsDeviceList.size() - 1);
        assertThat(testApsDevice.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testApsDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testApsDevice.getPhoneModel()).isEqualTo(UPDATED_PHONE_MODEL);
        assertThat(testApsDevice.getOsVersion()).isEqualTo(UPDATED_OS_VERSION);
        assertThat(testApsDevice.getInstalledOn()).isEqualTo(UPDATED_INSTALLED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsDevice() throws Exception {
        int databaseSizeBeforeUpdate = apsDeviceRepository.findAll().size();
        apsDevice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsDevice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsDevice in the database
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsDevice() throws Exception {
        // Initialize the database
        apsDeviceRepository.saveAndFlush(apsDevice);

        int databaseSizeBeforeDelete = apsDeviceRepository.findAll().size();

        // Delete the apsDevice
        restApsDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsDevice> apsDeviceList = apsDeviceRepository.findAll();
        assertThat(apsDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
