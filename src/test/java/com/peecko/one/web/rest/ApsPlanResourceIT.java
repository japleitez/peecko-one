package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Agency;
import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.repository.AgencyRepository;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ApsPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApsPlanResourceIT {

    private static final String DEFAULT_CONTRACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT = "BBBBBBBBBB";

    private static final PricingType DEFAULT_PRICING = PricingType.FIXED;
    private static final PricingType UPDATED_PRICING = PricingType.FITNESS;

    private static final PlanState DEFAULT_STATE = PlanState.TRIAL;
    private static final PlanState UPDATED_STATE = PlanState.TRIAL;

    private static final String DEFAULT_COUNTRY = "LU";
    private static final String UPDATED_COUNTRY = "FR";

    private static final String DEFAULT_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STARTS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTS = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDS = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TRIAL_STARTS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRIAL_STARTS = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TRIAL_ENDS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRIAL_ENDS = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/aps-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApsPlanRepository apsPlanRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApsPlanMockMvc;

    private Agency agency;
    private Customer customer;
    private ApsPlan apsPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsPlan createEntity(EntityManager em) {
        ApsPlan apsPlan = new ApsPlan()
            .contract(DEFAULT_CONTRACT)
            .pricing(DEFAULT_PRICING)
            .state(DEFAULT_STATE)
            .license(DEFAULT_LICENSE)
            .starts(DEFAULT_STARTS)
            .ends(DEFAULT_ENDS)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .notes(DEFAULT_NOTES)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return apsPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApsPlan createUpdatedEntity(EntityManager em) {
        ApsPlan apsPlan = new ApsPlan()
            .contract(UPDATED_CONTRACT)
            .pricing(UPDATED_PRICING)
            .state(UPDATED_STATE)
            .license(UPDATED_LICENSE)
            .starts(UPDATED_STARTS)
            .ends(UPDATED_ENDS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return apsPlan;
    }

    @BeforeEach
    public void initTest() {
        agency = AgencyResourceIT.createEntity(em);
        agency.setId(1L);
        agency = agencyRepository.saveAndFlush(agency);

        customer = CustomerResourceIT.createEntity(em);
        customer.setAgency(agency);
        customer = customerRepository.saveAndFlush(customer);

        apsPlan = createEntity(em);
        apsPlan.setCustomer(customer);
    }

    @Test
    @Transactional
    void createApsPlan() throws Exception {
        int databaseSizeBeforeCreate = apsPlanRepository.findAll().size();
        // Create the ApsPlan
        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isCreated());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeCreate + 1);
        ApsPlan testApsPlan = apsPlanList.get(apsPlanList.size() - 1);
        assertThat(testApsPlan.getContract()).isEqualTo(DEFAULT_CONTRACT);
        assertThat(testApsPlan.getPricing()).isEqualTo(DEFAULT_PRICING);
        assertThat(testApsPlan.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testApsPlan.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testApsPlan.getStarts()).isEqualTo(DEFAULT_STARTS);
        assertThat(testApsPlan.getEnds()).isEqualTo(DEFAULT_ENDS);
        assertThat(testApsPlan.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testApsPlan.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createApsPlanWithExistingId() throws Exception {
        // Create the ApsPlan with an existing ID
        apsPlan.setId(1L);

        int databaseSizeBeforeCreate = apsPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isBadRequest());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContractIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPlanRepository.findAll().size();
        // set the field null
        apsPlan.setContract(null);

        // Create the ApsPlan, which fails.

        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isBadRequest());

        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricingIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPlanRepository.findAll().size();
        // set the field null
        apsPlan.setPricing(null);

        // Create the ApsPlan, which fails.

        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isBadRequest());

        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPlanRepository.findAll().size();
        // set the field null
        apsPlan.setState(null);

        // Create the ApsPlan, which fails.

        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isBadRequest());

        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = apsPlanRepository.findAll().size();
        // set the field null
        apsPlan.setUnitPrice(null);

        // Create the ApsPlan, which fails.

        restApsPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isBadRequest());

        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApsPlans() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        // Get all the apsPlanList
        restApsPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apsPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(DEFAULT_CONTRACT)))
            .andExpect(jsonPath("$.[*].pricing").value(hasItem(DEFAULT_PRICING.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].license").value(hasItem(DEFAULT_LICENSE)))
            .andExpect(jsonPath("$.[*].starts").value(hasItem(DEFAULT_STARTS.toString())))
            .andExpect(jsonPath("$.[*].ends").value(hasItem(DEFAULT_ENDS.toString())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getApsPlan() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        // Get the apsPlan
        restApsPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, apsPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apsPlan.getId().intValue()))
            .andExpect(jsonPath("$.contract").value(DEFAULT_CONTRACT))
            .andExpect(jsonPath("$.pricing").value(DEFAULT_PRICING.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.license").value(DEFAULT_LICENSE))
            .andExpect(jsonPath("$.starts").value(DEFAULT_STARTS.toString()))
            .andExpect(jsonPath("$.ends").value(DEFAULT_ENDS.toString()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingApsPlan() throws Exception {
        // Get the apsPlan
        restApsPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApsPlan() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();

        // Update the apsPlan
        ApsPlan updatedApsPlan = apsPlanRepository.findById(apsPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApsPlan are not directly saved in db
        em.detach(updatedApsPlan);
        updatedApsPlan
            .contract(UPDATED_CONTRACT)
            .pricing(UPDATED_PRICING)
            .state(UPDATED_STATE)
            .license(UPDATED_LICENSE)
            .starts(UPDATED_STARTS)
            .ends(UPDATED_ENDS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .notes(UPDATED_NOTES);

        restApsPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApsPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApsPlan))
            )
            .andExpect(status().isOk());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
        ApsPlan testApsPlan = apsPlanList.get(apsPlanList.size() - 1);
        assertThat(testApsPlan.getContract()).isEqualTo(UPDATED_CONTRACT);
        assertThat(testApsPlan.getPricing()).isEqualTo(UPDATED_PRICING);
        assertThat(testApsPlan.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testApsPlan.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsPlan.getStarts()).isEqualTo(UPDATED_STARTS);
        assertThat(testApsPlan.getEnds()).isEqualTo(UPDATED_ENDS);
        assertThat(testApsPlan.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testApsPlan.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apsPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apsPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApsPlanWithPatch() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();

        // Update the apsPlan using partial update
        ApsPlan partialUpdatedApsPlan = new ApsPlan();
        partialUpdatedApsPlan.setId(apsPlan.getId());

        partialUpdatedApsPlan.contract(UPDATED_CONTRACT).pricing(UPDATED_PRICING).license(UPDATED_LICENSE).starts(UPDATED_STARTS);

        restApsPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsPlan))
            )
            .andExpect(status().isOk());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
        ApsPlan testApsPlan = apsPlanList.get(apsPlanList.size() - 1);

        assertThat(testApsPlan.getContract()).isEqualTo(UPDATED_CONTRACT);
        assertThat(testApsPlan.getPricing()).isEqualTo(UPDATED_PRICING);
        assertThat(testApsPlan.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsPlan.getStarts()).isEqualTo(UPDATED_STARTS);

        assertThat(testApsPlan.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testApsPlan.getEnds()).isEqualTo(DEFAULT_ENDS);
        assertThat(testApsPlan.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testApsPlan.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateApsPlanWithPatch() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();

        // Update the apsPlan using partial update
        ApsPlan partialUpdatedApsPlan = new ApsPlan();
        partialUpdatedApsPlan.setId(apsPlan.getId());

        partialUpdatedApsPlan
            .contract(UPDATED_CONTRACT)
            .pricing(UPDATED_PRICING)
            .state(UPDATED_STATE)
            .license(UPDATED_LICENSE)
            .starts(UPDATED_STARTS)
            .ends(UPDATED_ENDS)
            .unitPrice(UPDATED_UNIT_PRICE)
            .notes(UPDATED_NOTES);

        restApsPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApsPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApsPlan))
            )
            .andExpect(status().isOk());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
        ApsPlan testApsPlan = apsPlanList.get(apsPlanList.size() - 1);
        assertThat(testApsPlan.getContract()).isEqualTo(UPDATED_CONTRACT);
        assertThat(testApsPlan.getPricing()).isEqualTo(UPDATED_PRICING);
        assertThat(testApsPlan.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testApsPlan.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testApsPlan.getStarts()).isEqualTo(UPDATED_STARTS);
        assertThat(testApsPlan.getEnds()).isEqualTo(UPDATED_ENDS);
        assertThat(testApsPlan.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testApsPlan.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apsPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apsPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApsPlan() throws Exception {
        int databaseSizeBeforeUpdate = apsPlanRepository.findAll().size();
        apsPlan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApsPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apsPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApsPlan in the database
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApsPlan() throws Exception {
        // Initialize the database
        apsPlanRepository.saveAndFlush(apsPlan);

        int databaseSizeBeforeDelete = apsPlanRepository.findAll().size();

        // Delete the apsPlan
        restApsPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, apsPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApsPlan> apsPlanList = apsPlanRepository.findAll();
        assertThat(apsPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
