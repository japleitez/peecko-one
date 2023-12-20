package com.peecko.one.repository;

import com.peecko.one.domain.ArticleCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {}
