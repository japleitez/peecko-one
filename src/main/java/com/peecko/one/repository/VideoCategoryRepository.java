package com.peecko.one.repository;

import com.peecko.one.domain.VideoCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VideoCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoCategoryRepository extends JpaRepository<VideoCategory, Long> {}
