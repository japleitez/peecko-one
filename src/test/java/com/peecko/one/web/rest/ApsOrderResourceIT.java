package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.ApsOrder;
import com.peecko.one.repository.ApsOrderRepository;
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
 * Integration tests for the {@link ApsOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsOrderResourceIT {

    private static final Integer DEFAULT_PERIOD = 1;
    private static final Integer UPDATED_PERIOD = 2;

    private static final String DEFAULT_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;

    private static final Double DEFAULT_VAT_RATE = 1D;
    private static final Double UPDATED_VAT_RATE = 2D;

    private static final Integer DEFAULT_NUMBER_OF_USERS = 1;
    private static final Integer UPDATED_NUMBER_OF_USERS = 2;

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aps-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsOrderRepository apsOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsOrderMockMvc;

    private ApsOrder apsOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsOrder createEntity(EntityManager em) {
        ApsOrder apsOrder = new ApsOrder()
            .period(DEFAULT_PERIOD)
            .license(DEFAULT_LICENSE)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .vatRate(DEFAULT_VAT_RATE)
            .numberOfUsers(DEFAULT_NUMBER_OF_USERS)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER);
        return apsOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsOrder createUpdatedEntity(EntityManager em) {
        ApsOrder apsOrder = new ApsOrder()
            .period(UPDATED_PERIOD)
            .license(UPDATED_LICENSE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .numberOfUsers(UPDATED_NUMBER_OF_USERS)
            .invoiceNumber(UPDATED_INVOICE_NUMBER);
        return apsOrder;
    }

    @BeforeEach
    public void initTest() {
        apsOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createApsOrder() throws Exception {
        int databaseSizeBeforeCreate = apsOrderRepository.findAll().size();
        // Create the ApsOrder
        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isCreated());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ApsOrder testApsOrder = apsOrderList.get(apsOrderList.size() - 1);
        assertThat(testApsOrder.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testApsOrder.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testApsOrder.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testApsOrder.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testApsOrder.getNumberOfUsers()).isEqualTo(DEFAULT_NUMBER_OF_USERS);
        assertThat(testApsOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void createApsOrderWithExistingId() throws Exception {
        // Create the ApsOrder with an existing ID
        apsOrder.setId(1L);

        int databaseSizeBeforeCreate = apsOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsOrderRepository.findAll().size();
        // set the field null
        apsOrder.setPeriod(null);

        // Create the ApsOrder, which fails.

        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsOrderRepository.findAll().size();
        // set the field null
        apsOrder.setLicense(null);

        // Create the ApsOrder, which fails.

        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsOrderRepository.findAll().size();
        // set the field null
        apsOrder.setUnitPrice(null);

        // Create the ApsOrder, which fails.

        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsOrderRepository.findAll().size();
        // set the field null
        apsOrder.setVatRate(null);

        // Create the ApsOrder, which fails.

        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfUsersIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsOrderRepository.findAll().size();
        // set the field null
        apsOrder.setNumberOfUsers(null);

        // Create the ApsOrder, which fails.

        restApsOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isBadRequest());

        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsOrders() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        // Get all the apsOrderList
        restApsOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)))
            .andExpect(jsonPath("$.[*].license").value(hasItem(DEFAULT_LICENSE)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfUsers").value(hasItem(DEFAULT_NUMBER_OF_USERS)))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)));
    }

    @Test
    @Transactional
    void getApsOrder() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        // Get the apsOrder
        restApsOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, apsOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsOrder.getId().intValue()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD))
            .andExpect(jsonPath("$.license").value(DEFAULT_LICENSE))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.numberOfUsers").value(DEFAULT_NUMBER_OF_USERS))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingApsOrder() throws Exception {
        // Get the apsOrder
        restApsOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsOrder() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();

        // Update the apsOrder
        ApsOrder updatedApsOrder = apsOrderRepository.findById(apsOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsOrder are not directly saved in db
        em.detach(updatedApsOrder);
        updatedApsOrder
            .period(UPDATED_PERIOD)
            .license(UPDATED_LICENSE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .numberOfUsers(UPDATED_NUMBER_OF_USERS)
            .invoiceNumber(UPDATED_INVOICE_NUMBER);

        restApsOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsOrder))
            )
            .andExpect(status().isOk());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
        ApsOrder testApsOrder = apsOrderList.get(apsOrderList.size() - 1);
        assertThat(testApsOrder.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsOrder.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsOrder.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testApsOrder.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testApsOrder.getNumberOfUsers()).isEqualTo(UPDATED_NUMBER_OF_USERS);
        assertThat(testApsOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsOrderWithPatch() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();

        // Update the apsOrder using partial update
        ApsOrder partialUpdatedApsOrder = new ApsOrder();
        partialUpdatedApsOrder.setId(apsOrder.getId());

        partialUpdatedApsOrder
            .period(UPDATED_PERIOD)
            .license(UPDATED_LICENSE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .numberOfUsers(UPDATED_NUMBER_OF_USERS);

        restApsOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsOrder))
            )
            .andExpect(status().isOk());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
        ApsOrder testApsOrder = apsOrderList.get(apsOrderList.size() - 1);
        assertThat(testApsOrder.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsOrder.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsOrder.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testApsOrder.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testApsOrder.getNumberOfUsers()).isEqualTo(UPDATED_NUMBER_OF_USERS);
        assertThat(testApsOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateApsOrderWithPatch() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();

        // Update the apsOrder using partial update
        ApsOrder partialUpdatedApsOrder = new ApsOrder();
        partialUpdatedApsOrder.setId(apsOrder.getId());

        partialUpdatedApsOrder
            .period(UPDATED_PERIOD)
            .license(UPDATED_LICENSE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .numberOfUsers(UPDATED_NUMBER_OF_USERS)
            .invoiceNumber(UPDATED_INVOICE_NUMBER);

        restApsOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsOrder))
            )
            .andExpect(status().isOk());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
        ApsOrder testApsOrder = apsOrderList.get(apsOrderList.size() - 1);
        assertThat(testApsOrder.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testApsOrder.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsOrder.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testApsOrder.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testApsOrder.getNumberOfUsers()).isEqualTo(UPDATED_NUMBER_OF_USERS);
        assertThat(testApsOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsOrder() throws Exception {
        int databaseSizeBeforeUpdate = apsOrderRepository.findAll().size();
        apsOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsOrder in the database
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsOrder() throws Exception {
        // Initialize the database
        apsOrderRepository.saveAndFlush(apsOrder);

        int databaseSizeBeforeDelete = apsOrderRepository.findAll().size();

        // Delete the apsOrder
        restApsOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsOrder> apsOrderList = apsOrderRepository.findAll();
        assertThat(apsOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
