package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ApsPricing;
import com.peecko.one.repository.ApsPricingRepository;
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
 * Integration tests for the {@link ApsPricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsPricingResourceIT {

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Integer DEFAULT_MIN_QUANTITY = 1;
    private static final Integer UPDATED_MIN_QUANTITY = 2;

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/aps-pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsPricingRepository apsPricingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsPricingMockMvc;

    private ApsPricing apsPricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsPricing createEntity(EntityManager em) {
        ApsPricing apsPricing = new ApsPricing()
            .index(DEFAULT_INDEX)
            .minQuantity(DEFAULT_MIN_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE);
        return apsPricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsPricing createUpdatedEntity(EntityManager em) {
        ApsPricing apsPricing = new ApsPricing()
            .index(UPDATED_INDEX)
            .minQuantity(UPDATED_MIN_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);
        return apsPricing;
    }

    @BeforeEach
    public void initTest() {
        apsPricing = createEntity(em);
    }

    @Test
    @Transactional
    void createApsPricing() throws Exception {
        int databaseSizeBeforeCreate = apsPricingRepository.findAll().size();
        // Create the ApsPricing
        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isCreated());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeCreate + 1);
        ApsPricing testApsPricing = apsPricingList.get(apsPricingList.size() - 1);
        assertThat(testApsPricing.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testApsPricing.getMinQuantity()).isEqualTo(DEFAULT_MIN_QUANTITY);
        assertThat(testApsPricing.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void createApsPricingWithExistingId() throws Exception {
        // Create the ApsPricing with an existing ID
        apsPricing.setId(1L);

        int databaseSizeBeforeCreate = apsPricingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isBadRequest());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPricingRepository.findAll().size();

        // Create the ApsPricing, which fails.

        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isBadRequest());

        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPricingRepository.findAll().size();
        // set the field null
        apsPricing.setIndex(null);

        // Create the ApsPricing, which fails.

        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isBadRequest());

        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPricingRepository.findAll().size();
        // set the field null
        apsPricing.setMinQuantity(null);

        // Create the ApsPricing, which fails.

        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isBadRequest());

        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPricingRepository.findAll().size();
        // set the field null
        apsPricing.setUnitPrice(null);

        // Create the ApsPricing, which fails.

        restApsPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isBadRequest());

        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsPricings() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        // Get all the apsPricingList
        restApsPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsPricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].minQuantity").value(hasItem(DEFAULT_MIN_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getApsPricing() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        // Get the apsPricing
        restApsPricingMockMvc
            .perform(get(ENTITY_API_URL_ID, apsPricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsPricing.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.minQuantity").value(DEFAULT_MIN_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingApsPricing() throws Exception {
        // Get the apsPricing
        restApsPricingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsPricing() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();

        // Update the apsPricing
        ApsPricing updatedApsPricing = apsPricingRepository.findById(apsPricing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsPricing are not directly saved in db
        em.detach(updatedApsPricing);
        updatedApsPricing
            .index(UPDATED_INDEX)
            .minQuantity(UPDATED_MIN_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restApsPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsPricing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsPricing))
            )
            .andExpect(status().isOk());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
        ApsPricing testApsPricing = apsPricingList.get(apsPricingList.size() - 1);
        assertThat(testApsPricing.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testApsPricing.getMinQuantity()).isEqualTo(UPDATED_MIN_QUANTITY);
        assertThat(testApsPricing.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsPricing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsPricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsPricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPricing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsPricingWithPatch() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();

        // Update the apsPricing using partial update
        ApsPricing partialUpdatedApsPricing = new ApsPricing();
        partialUpdatedApsPricing.setId(apsPricing.getId());


        restApsPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsPricing))
            )
            .andExpect(status().isOk());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
        ApsPricing testApsPricing = apsPricingList.get(apsPricingList.size() - 1);
        assertThat(testApsPricing.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testApsPricing.getMinQuantity()).isEqualTo(DEFAULT_MIN_QUANTITY);
        assertThat(testApsPricing.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateApsPricingWithPatch() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();

        // Update the apsPricing using partial update
        ApsPricing partialUpdatedApsPricing = new ApsPricing();
        partialUpdatedApsPricing.setId(apsPricing.getId());

        partialUpdatedApsPricing
            .index(UPDATED_INDEX)
            .minQuantity(UPDATED_MIN_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restApsPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsPricing))
            )
            .andExpect(status().isOk());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
        ApsPricing testApsPricing = apsPricingList.get(apsPricingList.size() - 1);
        assertThat(testApsPricing.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testApsPricing.getMinQuantity()).isEqualTo(UPDATED_MIN_QUANTITY);
        assertThat(testApsPricing.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsPricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsPricing))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsPricing() throws Exception {
        int databaseSizeBeforeUpdate = apsPricingRepository.findAll().size();
        apsPricing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPricingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsPricing))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsPricing in the database
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsPricing() throws Exception {
        // Initialize the database
        apsPricingRepository.saveAndFlush(apsPricing);

        int databaseSizeBeforeDelete = apsPricingRepository.findAll().size();

        // Delete the apsPricing
        restApsPricingMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsPricing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsPricing> apsPricingList = apsPricingRepository.findAll();
        assertThat(apsPricingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
