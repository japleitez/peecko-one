package com.peecko.one.repository;

import com.peecko.one.domain.ApsOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApsOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsOrderRepository extends JpaRepository<ApsOrder, Long> {}
