package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.repository.CustomerRepository;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE = "BBBBBBBBBB";

    private static final CustomerState DEFAULT_STATE = CustomerState.NEW;
    private static final CustomerState UPDATED_STATE = CustomerState.TRIAL;

    private static final String DEFAULT_CLOSE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_DOMAINS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_DOMAINS = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_VAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TRIALED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRIALED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DECLINED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECLINED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTIVATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .country(DEFAULT_COUNTRY)
            .license(DEFAULT_LICENSE)
            .state(DEFAULT_STATE)
            .closeReason(DEFAULT_CLOSE_REASON)
            .billingEmail(DEFAULT_EMAIL_DOMAINS)
            .vatId(DEFAULT_VAT_ID)
            .bank(DEFAULT_BANK)
            .iban(DEFAULT_IBAN)
            .logo(DEFAULT_LOGO)
            .notes(DEFAULT_NOTES)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .trialed(DEFAULT_TRIALED)
            .declined(DEFAULT_DECLINED)
            .activated(DEFAULT_ACTIVATED)
            .closed(DEFAULT_CLOSED);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .license(UPDATED_LICENSE)
            .state(UPDATED_STATE)
            .closeReason(UPDATED_CLOSE_REASON)
            .billingEmail(UPDATED_EMAIL_DOMAINS)
            .vatId(UPDATED_VAT_ID)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .logo(UPDATED_LOGO)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .trialed(UPDATED_TRIALED)
            .declined(UPDATED_DECLINED)
            .activated(UPDATED_ACTIVATED)
            .closed(UPDATED_CLOSED);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCustomer.getLicense()).isEqualTo(DEFAULT_LICENSE);
        assertThat(testCustomer.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCustomer.getCloseReason()).isEqualTo(DEFAULT_CLOSE_REASON);
        assertThat(testCustomer.getBillingEmail()).isEqualTo(DEFAULT_EMAIL_DOMAINS);
        assertThat(testCustomer.getVatId()).isEqualTo(DEFAULT_VAT_ID);
        assertThat(testCustomer.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testCustomer.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testCustomer.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCustomer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomer.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCustomer.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testCustomer.getTrialed()).isEqualTo(DEFAULT_TRIALED);
        assertThat(testCustomer.getDeclined()).isEqualTo(DEFAULT_DECLINED);
        assertThat(testCustomer.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testCustomer.getClosed()).isEqualTo(DEFAULT_CLOSED);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCode(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setName(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCountry(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setState(null);

        // Create the Customer, which fails.

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].license").value(hasItem(DEFAULT_LICENSE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].closeReason").value(hasItem(DEFAULT_CLOSE_REASON)))
            .andExpect(jsonPath("$.[*].emailDomains").value(hasItem(DEFAULT_EMAIL_DOMAINS)))
            .andExpect(jsonPath("$.[*].vatId").value(hasItem(DEFAULT_VAT_ID)))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].trialed").value(hasItem(DEFAULT_TRIALED.toString())))
            .andExpect(jsonPath("$.[*].declined").value(hasItem(DEFAULT_DECLINED.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.toString())))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.toString())));
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.license").value(DEFAULT_LICENSE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.closeReason").value(DEFAULT_CLOSE_REASON))
            .andExpect(jsonPath("$.emailDomains").value(DEFAULT_EMAIL_DOMAINS))
            .andExpect(jsonPath("$.vatId").value(DEFAULT_VAT_ID))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.trialed").value(DEFAULT_TRIALED.toString()))
            .andExpect(jsonPath("$.declined").value(DEFAULT_DECLINED.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.toString()))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .license(UPDATED_LICENSE)
            .state(UPDATED_STATE)
            .closeReason(UPDATED_CLOSE_REASON)
            .billingEmail(UPDATED_EMAIL_DOMAINS)
            .vatId(UPDATED_VAT_ID)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .logo(UPDATED_LOGO)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .trialed(UPDATED_TRIALED)
            .declined(UPDATED_DECLINED)
            .activated(UPDATED_ACTIVATED)
            .closed(UPDATED_CLOSED);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCustomer.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testCustomer.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCustomer.getCloseReason()).isEqualTo(UPDATED_CLOSE_REASON);
        assertThat(testCustomer.getBillingEmail()).isEqualTo(UPDATED_EMAIL_DOMAINS);
        assertThat(testCustomer.getVatId()).isEqualTo(UPDATED_VAT_ID);
        assertThat(testCustomer.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testCustomer.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCustomer.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCustomer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomer.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCustomer.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testCustomer.getTrialed()).isEqualTo(UPDATED_TRIALED);
        assertThat(testCustomer.getDeclined()).isEqualTo(UPDATED_DECLINED);
        assertThat(testCustomer.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testCustomer.getClosed()).isEqualTo(UPDATED_CLOSED);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .license(UPDATED_LICENSE)
            .state(UPDATED_STATE)
            .logo(UPDATED_LOGO)
            .created(UPDATED_CREATED)
            .activated(UPDATED_ACTIVATED)
            .closed(UPDATED_CLOSED);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCustomer.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testCustomer.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCustomer.getCloseReason()).isEqualTo(DEFAULT_CLOSE_REASON);
        assertThat(testCustomer.getBillingEmail()).isEqualTo(DEFAULT_EMAIL_DOMAINS);
        assertThat(testCustomer.getVatId()).isEqualTo(DEFAULT_VAT_ID);
        assertThat(testCustomer.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testCustomer.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testCustomer.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCustomer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomer.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCustomer.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testCustomer.getTrialed()).isEqualTo(DEFAULT_TRIALED);
        assertThat(testCustomer.getDeclined()).isEqualTo(DEFAULT_DECLINED);
        assertThat(testCustomer.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testCustomer.getClosed()).isEqualTo(UPDATED_CLOSED);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .license(UPDATED_LICENSE)
            .state(UPDATED_STATE)
            .closeReason(UPDATED_CLOSE_REASON)
            .billingEmail(UPDATED_EMAIL_DOMAINS)
            .vatId(UPDATED_VAT_ID)
            .bank(UPDATED_BANK)
            .iban(UPDATED_IBAN)
            .logo(UPDATED_LOGO)
            .notes(UPDATED_NOTES)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .trialed(UPDATED_TRIALED)
            .declined(UPDATED_DECLINED)
            .activated(UPDATED_ACTIVATED)
            .closed(UPDATED_CLOSED);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCustomer.getLicense()).isEqualTo(UPDATED_LICENSE);
        assertThat(testCustomer.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCustomer.getCloseReason()).isEqualTo(UPDATED_CLOSE_REASON);
        assertThat(testCustomer.getBillingEmail()).isEqualTo(UPDATED_EMAIL_DOMAINS);
        assertThat(testCustomer.getVatId()).isEqualTo(UPDATED_VAT_ID);
        assertThat(testCustomer.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testCustomer.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCustomer.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCustomer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomer.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCustomer.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testCustomer.getTrialed()).isEqualTo(UPDATED_TRIALED);
        assertThat(testCustomer.getDeclined()).isEqualTo(UPDATED_DECLINED);
        assertThat(testCustomer.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testCustomer.getClosed()).isEqualTo(UPDATED_CLOSED);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
