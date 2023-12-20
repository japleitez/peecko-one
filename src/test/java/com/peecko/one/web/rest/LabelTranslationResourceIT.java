package com.peecko.one.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peecko.one.IntegrationTest;
import com.peecko.one.domain.LabelTranslation;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.repository.LabelTranslationRepository;
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
 * Integration tests for the {@link LabelTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LabelTranslationResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Language DEFAULT_LANG = Language.EN;
    private static final Language UPDATED_LANG = Language.FR;

    private static final String DEFAULT_TRANSLATION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/label-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LabelTranslationRepository labelTranslationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLabelTranslationMockMvc;

    private LabelTranslation labelTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabelTranslation createEntity(EntityManager em) {
        LabelTranslation labelTranslation = new LabelTranslation().label(DEFAULT_LABEL).lang(DEFAULT_LANG).translation(DEFAULT_TRANSLATION);
        return labelTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabelTranslation createUpdatedEntity(EntityManager em) {
        LabelTranslation labelTranslation = new LabelTranslation().label(UPDATED_LABEL).lang(UPDATED_LANG).translation(UPDATED_TRANSLATION);
        return labelTranslation;
    }

    @BeforeEach
    public void initTest() {
        labelTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createLabelTranslation() throws Exception {
        int databaseSizeBeforeCreate = labelTranslationRepository.findAll().size();
        // Create the LabelTranslation
        restLabelTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isCreated());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        LabelTranslation testLabelTranslation = labelTranslationList.get(labelTranslationList.size() - 1);
        assertThat(testLabelTranslation.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testLabelTranslation.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testLabelTranslation.getTranslation()).isEqualTo(DEFAULT_TRANSLATION);
    }

    @Test
    @Transactional
    void createLabelTranslationWithExistingId() throws Exception {
        // Create the LabelTranslation with an existing ID
        labelTranslation.setId(1L);

        int databaseSizeBeforeCreate = labelTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabelTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelTranslationRepository.findAll().size();
        // set the field null
        labelTranslation.setLabel(null);

        // Create the LabelTranslation, which fails.

        restLabelTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLangIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelTranslationRepository.findAll().size();
        // set the field null
        labelTranslation.setLang(null);

        // Create the LabelTranslation, which fails.

        restLabelTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTranslationIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelTranslationRepository.findAll().size();
        // set the field null
        labelTranslation.setTranslation(null);

        // Create the LabelTranslation, which fails.

        restLabelTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLabelTranslations() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        // Get all the labelTranslationList
        restLabelTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(labelTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION)));
    }

    @Test
    @Transactional
    void getLabelTranslation() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        // Get the labelTranslation
        restLabelTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, labelTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(labelTranslation.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.translation").value(DEFAULT_TRANSLATION));
    }

    @Test
    @Transactional
    void getNonExistingLabelTranslation() throws Exception {
        // Get the labelTranslation
        restLabelTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLabelTranslation() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();

        // Update the labelTranslation
        LabelTranslation updatedLabelTranslation = labelTranslationRepository.findById(labelTranslation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLabelTranslation are not directly saved in db
        em.detach(updatedLabelTranslation);
        updatedLabelTranslation.label(UPDATED_LABEL).lang(UPDATED_LANG).translation(UPDATED_TRANSLATION);

        restLabelTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLabelTranslation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLabelTranslation))
            )
            .andExpect(status().isOk());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
        LabelTranslation testLabelTranslation = labelTranslationList.get(labelTranslationList.size() - 1);
        assertThat(testLabelTranslation.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testLabelTranslation.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testLabelTranslation.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    void putNonExistingLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, labelTranslation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLabelTranslationWithPatch() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();

        // Update the labelTranslation using partial update
        LabelTranslation partialUpdatedLabelTranslation = new LabelTranslation();
        partialUpdatedLabelTranslation.setId(labelTranslation.getId());

        partialUpdatedLabelTranslation.label(UPDATED_LABEL).translation(UPDATED_TRANSLATION);

        restLabelTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabelTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabelTranslation))
            )
            .andExpect(status().isOk());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
        LabelTranslation testLabelTranslation = labelTranslationList.get(labelTranslationList.size() - 1);
        assertThat(testLabelTranslation.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testLabelTranslation.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testLabelTranslation.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    void fullUpdateLabelTranslationWithPatch() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();

        // Update the labelTranslation using partial update
        LabelTranslation partialUpdatedLabelTranslation = new LabelTranslation();
        partialUpdatedLabelTranslation.setId(labelTranslation.getId());

        partialUpdatedLabelTranslation.label(UPDATED_LABEL).lang(UPDATED_LANG).translation(UPDATED_TRANSLATION);

        restLabelTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabelTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabelTranslation))
            )
            .andExpect(status().isOk());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
        LabelTranslation testLabelTranslation = labelTranslationList.get(labelTranslationList.size() - 1);
        assertThat(testLabelTranslation.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testLabelTranslation.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testLabelTranslation.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    void patchNonExistingLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, labelTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLabelTranslation() throws Exception {
        int databaseSizeBeforeUpdate = labelTranslationRepository.findAll().size();
        labelTranslation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(labelTranslation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabelTranslation in the database
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLabelTranslation() throws Exception {
        // Initialize the database
        labelTranslationRepository.saveAndFlush(labelTranslation);

        int databaseSizeBeforeDelete = labelTranslationRepository.findAll().size();

        // Delete the labelTranslation
        restLabelTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, labelTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LabelTranslation> labelTranslationList = labelTranslationRepository.findAll();
        assertThat(labelTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
