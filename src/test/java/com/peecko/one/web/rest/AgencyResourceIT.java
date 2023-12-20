package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Agency;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.repository.AgencyRepository;
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
 * Integration tests for the {@link AgencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.EN;
    private static final Language UPDATED_LANGUAGE = Language.FR;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_RCS = "AAAAAAAAAA";
    private static final String UPDATED_RCS = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_VAT_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_VAT_RATE = 1D;
    private static final Double UPDATED_VAT_RATE = 2D;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/agencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyMockMvc;

    private Agency agency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createEntity(EntityManager em) {
        Agency agency = new Agency()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .zip(DEFAULT_ZIP)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .language(DEFAULT_LANGUAGE)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .billingEmail(DEFAULT_BILLING_EMAIL)
            .billingPhone(DEFAULT_BILLING_PHONE)
            .bank(DEFAULT_BANK)
            .iban(DEFAULT_IBAN)
            .rcs(DEFAULT_RCS)
            .vatId(DEFAULT_VAT_ID)
            .vatRate(DEFAULT_VAT_RATE)
            .notes(DEFAULT_NOTES)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return agency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createUpdatedEntity(EntityManager em) {
        Agency agency = new Agency()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .zip(UPDATED_ZIP)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .billingEmail(UPDATED_BILLING_EMAIL)
            .billingPhone(UPDATED_BILLING_PHONE)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .rcs(UPDATED_RCS)
            .vatId(UPDATED_VAT_ID)
            .vatRate(UPDATED_VAT_RATE)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return agency;
    }

    @BeforeEach
    public void initTest() {
        agency = createEntity(em);
    }

    @Test
    @Transactional
    void createAgency() throws Exception {
        int databaseSizeBeforeCreate = agencyRepository.findAll().size();
        // Create the Agency
        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isCreated());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeCreate + 1);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAgency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgency.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testAgency.getLine2()).isEqualTo(DEFAULT_LINE_2);
        assertThat(testAgency.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testAgency.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAgency.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAgency.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testAgency.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAgency.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAgency.getBillingEmail()).isEqualTo(DEFAULT_BILLING_EMAIL);
        assertThat(testAgency.getBillingPhone()).isEqualTo(DEFAULT_BILLING_PHONE);
        assertThat(testAgency.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testAgency.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testAgency.getRcs()).isEqualTo(DEFAULT_RCS);
        assertThat(testAgency.getVatId()).isEqualTo(DEFAULT_VAT_ID);
        assertThat(testAgency.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testAgency.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAgency.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testAgency.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    void createAgencyWithExistingId() throws Exception {
        // Create the Agency with an existing ID
        agency.setId(1L);

        int databaseSizeBeforeCreate = agencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setCode(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setName(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setCity(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setCountry(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyRepository.findAll().size();
        // set the field null
        agency.setLanguage(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isBadRequest());

        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencies() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        // Get all the agencyList
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].billingEmail").value(hasItem(DEFAULT_BILLING_EMAIL)))
            .andExpect(jsonPath("$.[*].billingPhone").value(hasItem(DEFAULT_BILLING_PHONE)))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].rcs").value(hasItem(DEFAULT_RCS)))
            .andExpect(jsonPath("$.[*].vatId").value(hasItem(DEFAULT_VAT_ID)))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        // Get the agency
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL_ID, agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.billingEmail").value(DEFAULT_BILLING_EMAIL))
            .andExpect(jsonPath("$.billingPhone").value(DEFAULT_BILLING_PHONE))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.rcs").value(DEFAULT_RCS))
            .andExpect(jsonPath("$.vatId").value(DEFAULT_VAT_ID))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAgency() throws Exception {
        // Get the agency
        restAgencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();

        // Update the agency
        Agency updatedAgency = agencyRepository.findById(agency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgency are not directly saved in db
        em.detach(updatedAgency);
        updatedAgency
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .zip(UPDATED_ZIP)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .billingEmail(UPDATED_BILLING_EMAIL)
            .billingPhone(UPDATED_BILLING_PHONE)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .rcs(UPDATED_RCS)
            .vatId(UPDATED_VAT_ID)
            .vatRate(UPDATED_VAT_RATE)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgency.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgency.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAgency.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAgency.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testAgency.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAgency.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAgency.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testAgency.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgency.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAgency.getBillingEmail()).isEqualTo(UPDATED_BILLING_EMAIL);
        assertThat(testAgency.getBillingPhone()).isEqualTo(UPDATED_BILLING_PHONE);
        assertThat(testAgency.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testAgency.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testAgency.getRcs()).isEqualTo(UPDATED_RCS);
        assertThat(testAgency.getVatId()).isEqualTo(UPDATED_VAT_ID);
        assertThat(testAgency.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testAgency.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAgency.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAgency.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void putNonExistingAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agency.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency
            .name(UPDATED_NAME)
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL)
            .iban(UPDATED_IBAN)
            .rcs(UPDATED_RCS)
            .vatId(UPDATED_VAT_ID)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgency.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testAgency.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAgency.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testAgency.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAgency.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAgency.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testAgency.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgency.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAgency.getBillingEmail()).isEqualTo(DEFAULT_BILLING_EMAIL);
        assertThat(testAgency.getBillingPhone()).isEqualTo(DEFAULT_BILLING_PHONE);
        assertThat(testAgency.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testAgency.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testAgency.getRcs()).isEqualTo(UPDATED_RCS);
        assertThat(testAgency.getVatId()).isEqualTo(UPDATED_VAT_ID);
        assertThat(testAgency.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testAgency.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAgency.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAgency.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void fullUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .zip(UPDATED_ZIP)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .language(UPDATED_LANGUAGE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .billingEmail(UPDATED_BILLING_EMAIL)
            .billingPhone(UPDATED_BILLING_PHONE)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .rcs(UPDATED_RCS)
            .vatId(UPDATED_VAT_ID)
            .vatRate(UPDATED_VAT_RATE)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
        Agency testAgency = agencyList.get(agencyList.size() - 1);
        assertThat(testAgency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAgency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgency.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAgency.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAgency.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testAgency.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAgency.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAgency.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testAgency.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAgency.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAgency.getBillingEmail()).isEqualTo(UPDATED_BILLING_EMAIL);
        assertThat(testAgency.getBillingPhone()).isEqualTo(UPDATED_BILLING_PHONE);
        assertThat(testAgency.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testAgency.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testAgency.getRcs()).isEqualTo(UPDATED_RCS);
        assertThat(testAgency.getVatId()).isEqualTo(UPDATED_VAT_ID);
        assertThat(testAgency.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testAgency.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAgency.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testAgency.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    void patchNonExistingAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgency() throws Exception {
        int databaseSizeBeforeUpdate = agencyRepository.findAll().size();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(agency)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgency() throws Exception {
        // Initialize the database
        agencyRepository.saveAndFlush(agency);

        int databaseSizeBeforeDelete = agencyRepository.findAll().size();

        // Delete the agency
        restAgencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, agency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agency> agencyList = agencyRepository.findAll();
        assertThat(agencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
