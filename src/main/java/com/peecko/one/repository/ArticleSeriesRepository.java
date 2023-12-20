package com.peecko.one.repository;

import com.peecko.one.domain.ArticleSeries;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArticleSeries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleSeriesRepository extends JpaRepository<ArticleSeries, Long> {}
