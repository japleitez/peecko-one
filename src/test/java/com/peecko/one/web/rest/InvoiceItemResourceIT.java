package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.InvoiceItem;
import com.peecko.one.domain.enumeration.ProductType;
import com.peecko.one.repository.InvoiceItemRepository;
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
 * Integration tests for the {@link InvoiceItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemResourceIT {

    private static final ProductType DEFAULT_TYPE = ProductType.AMS;
    private static final ProductType UPDATED_TYPE = ProductType.AMS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_PRICE_UNIT = 1D;
    private static final Double UPDATED_PRICE_UNIT = 2D;

    private static final Double DEFAULT_PRICE_EXTENDED = 1D;
    private static final Double UPDATED_PRICE_EXTENDED = 2D;

    private static final Double DEFAULT_DIS_RATE = 1D;
    private static final Double UPDATED_DIS_RATE = 2D;

    private static final Double DEFAULT_DIS_AMOUNT = 1D;
    private static final Double UPDATED_DIS_AMOUNT = 2D;

    private static final Double DEFAULT_FINAL_PRICE = 1D;
    private static final Double UPDATED_FINAL_PRICE = 2D;

    private static final Double DEFAULT_VAT_RATE = 1D;
    private static final Double UPDATED_VAT_RATE = 2D;

    private static final Double DEFAULT_VAT_AMOUNT = 1D;
    private static final Double UPDATED_VAT_AMOUNT = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .priceUnit(DEFAULT_PRICE_UNIT)
            .priceExtended(DEFAULT_PRICE_EXTENDED)
            .disRate(DEFAULT_DIS_RATE)
            .disAmount(DEFAULT_DIS_AMOUNT)
            .finalPrice(DEFAULT_FINAL_PRICE)
            .vatRate(DEFAULT_VAT_RATE)
            .vatAmount(DEFAULT_VAT_AMOUNT)
            .total(DEFAULT_TOTAL);
        return invoiceItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createUpdatedEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem()
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .priceUnit(UPDATED_PRICE_UNIT)
            .priceExtended(UPDATED_PRICE_EXTENDED)
            .disRate(UPDATED_DIS_RATE)
            .disAmount(UPDATED_DIS_AMOUNT)
            .finalPrice(UPDATED_FINAL_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL);
        return invoiceItem;
    }

    @BeforeEach
    public void initTest() {
        invoiceItem = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();
        // Create the InvoiceItem
        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isCreated());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceItem.getPriceUnit()).isEqualTo(DEFAULT_PRICE_UNIT);
        assertThat(testInvoiceItem.getPriceExtended()).isEqualTo(DEFAULT_PRICE_EXTENDED);
        assertThat(testInvoiceItem.getDisRate()).isEqualTo(DEFAULT_DIS_RATE);
        assertThat(testInvoiceItem.getDisAmount()).isEqualTo(DEFAULT_DIS_AMOUNT);
        assertThat(testInvoiceItem.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testInvoiceItem.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testInvoiceItem.getVatAmount()).isEqualTo(DEFAULT_VAT_AMOUNT);
        assertThat(testInvoiceItem.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createInvoiceItemWithExistingId() throws Exception {
        // Create the InvoiceItem with an existing ID
        invoiceItem.setId(1L);

        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setType(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setDescription(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setQuantity(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setPriceUnit(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceExtendedIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setPriceExtended(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setDisRate(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setDisAmount(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setFinalPrice(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setVatRate(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVatAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setVatAmount(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setTotal(null);

        // Create the InvoiceItem, which fails.

        restInvoiceItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].priceUnit").value(hasItem(DEFAULT_PRICE_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].priceExtended").value(hasItem(DEFAULT_PRICE_EXTENDED.doubleValue())))
            .andExpect(jsonPath("$.[*].disRate").value(hasItem(DEFAULT_DIS_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].disAmount").value(hasItem(DEFAULT_DIS_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].finalPrice").value(hasItem(DEFAULT_FINAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].vatAmount").value(hasItem(DEFAULT_VAT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.priceUnit").value(DEFAULT_PRICE_UNIT.doubleValue()))
            .andExpect(jsonPath("$.priceExtended").value(DEFAULT_PRICE_EXTENDED.doubleValue()))
            .andExpect(jsonPath("$.disRate").value(DEFAULT_DIS_RATE.doubleValue()))
            .andExpect(jsonPath("$.disAmount").value(DEFAULT_DIS_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.finalPrice").value(DEFAULT_FINAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.vatAmount").value(DEFAULT_VAT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceItem are not directly saved in db
        em.detach(updatedInvoiceItem);
        updatedInvoiceItem
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .priceUnit(UPDATED_PRICE_UNIT)
            .priceExtended(UPDATED_PRICE_EXTENDED)
            .disRate(UPDATED_DIS_RATE)
            .disAmount(UPDATED_DIS_AMOUNT)
            .finalPrice(UPDATED_FINAL_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL);

        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItem.getPriceUnit()).isEqualTo(UPDATED_PRICE_UNIT);
        assertThat(testInvoiceItem.getPriceExtended()).isEqualTo(UPDATED_PRICE_EXTENDED);
        assertThat(testInvoiceItem.getDisRate()).isEqualTo(UPDATED_DIS_RATE);
        assertThat(testInvoiceItem.getDisAmount()).isEqualTo(UPDATED_DIS_AMOUNT);
        assertThat(testInvoiceItem.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testInvoiceItem.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testInvoiceItem.getVatAmount()).isEqualTo(UPDATED_VAT_AMOUNT);
        assertThat(testInvoiceItem.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .priceUnit(UPDATED_PRICE_UNIT)
            .priceExtended(UPDATED_PRICE_EXTENDED)
            .disRate(UPDATED_DIS_RATE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItem.getPriceUnit()).isEqualTo(UPDATED_PRICE_UNIT);
        assertThat(testInvoiceItem.getPriceExtended()).isEqualTo(UPDATED_PRICE_EXTENDED);
        assertThat(testInvoiceItem.getDisRate()).isEqualTo(UPDATED_DIS_RATE);
        assertThat(testInvoiceItem.getDisAmount()).isEqualTo(DEFAULT_DIS_AMOUNT);
        assertThat(testInvoiceItem.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testInvoiceItem.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testInvoiceItem.getVatAmount()).isEqualTo(UPDATED_VAT_AMOUNT);
        assertThat(testInvoiceItem.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .priceUnit(UPDATED_PRICE_UNIT)
            .priceExtended(UPDATED_PRICE_EXTENDED)
            .disRate(UPDATED_DIS_RATE)
            .disAmount(UPDATED_DIS_AMOUNT)
            .finalPrice(UPDATED_FINAL_PRICE)
            .vatRate(UPDATED_VAT_RATE)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItem.getPriceUnit()).isEqualTo(UPDATED_PRICE_UNIT);
        assertThat(testInvoiceItem.getPriceExtended()).isEqualTo(UPDATED_PRICE_EXTENDED);
        assertThat(testInvoiceItem.getDisRate()).isEqualTo(UPDATED_DIS_RATE);
        assertThat(testInvoiceItem.getDisAmount()).isEqualTo(UPDATED_DIS_AMOUNT);
        assertThat(testInvoiceItem.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testInvoiceItem.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testInvoiceItem.getVatAmount()).isEqualTo(UPDATED_VAT_AMOUNT);
        assertThat(testInvoiceItem.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeDelete = invoiceItemRepository.findAll().size();

        // Delete the invoiceItem
        restInvoiceItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
