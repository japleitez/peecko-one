package com.peecko.one.repository;

import com.peecko.one.domain.LabelTranslation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LabelTranslation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabelTranslationRepository extends JpaRepository<LabelTranslation, Long> {}
