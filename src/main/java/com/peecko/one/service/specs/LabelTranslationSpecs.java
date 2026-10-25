package com.peecko.one.service.specs;

import com.peecko.one.domain.LabelTranslation;
import com.peecko.one.domain.LabelTranslation_;
import org.springframework.data.jpa.domain.Specification;

public class LabelTranslationSpecs extends BaseSpecs {

    public static Specification<LabelTranslation> codeLike(String code) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(LabelTranslation_.CODE)), anyText(code.toLowerCase()));
    }

    public static Specification<LabelTranslation> lang(String lang) {
        return (root, query, cb) -> cb.equal(root.get(LabelTranslation_.LANG), lang);
    }

    public static Specification<LabelTranslation> textLike(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(LabelTranslation_.TEXT)), anyText(text.toLowerCase()));
    }

    /**
     * Combines the optional filters of the label translation list; blank or null values are ignored.
     */
    public static Specification<LabelTranslation> filter(String code, String lang, String text) {
        Specification<LabelTranslation> spec = Specification.where(null);
        if (code != null && !code.isBlank()) {
            spec = spec.and(codeLike(code.trim()));
        }
        if (lang != null) {
            spec = spec.and(lang(lang));
        }
        if (text != null && !text.isBlank()) {
            spec = spec.and(textLike(text.trim()));
        }
        return spec;
    }
}
