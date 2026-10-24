package com.peecko.one.repository;

import com.peecko.one.domain.Article;
import com.peecko.one.domain.enumeration.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByArticleCategoryId(Long articleCategoryId, Pageable pageable);

    Page<Article> findByLanguage(Language language, Pageable pageable);

    Page<Article> findByArticleCategoryIdAndLanguage(Long articleCategoryId, Language language, Pageable pageable);
}
