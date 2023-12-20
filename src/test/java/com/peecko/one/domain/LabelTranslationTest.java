package com.peecko.one.domain;

import static com.peecko.one.domain.LabelTranslationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.peecko.one.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabelTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabelTranslation.class);
        LabelTranslation labelTranslation1 = getLabelTranslationSample1();
        LabelTranslation labelTranslation2 = new LabelTranslation();
        assertThat(labelTranslation1).isNotEqualTo(labelTranslation2);

        labelTranslation2.setId(labelTranslation1.getId());
        assertThat(labelTranslation1).isEqualTo(labelTranslation2);

        labelTranslation2 = getLabelTranslationSample2();
        assertThat(labelTranslation1).isNotEqualTo(labelTranslation2);
    }
}
