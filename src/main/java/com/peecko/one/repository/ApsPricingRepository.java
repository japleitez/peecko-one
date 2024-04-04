package com.peecko.one.repository;

import com.peecko.one.domain.ApsPricing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the ApsPricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsPricingRepository extends JpaRepository<ApsPricing, Long> {}
