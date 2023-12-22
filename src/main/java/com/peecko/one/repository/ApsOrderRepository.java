package com.peecko.one.repository;

import com.peecko.one.domain.ApsOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ApsOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsOrderRepository extends JpaRepository<ApsOrder, Long> {

    @Query("from ApsOrder o where o.period = :period and o.apsPlan.customer.agency.id = :agencyId")
    List<ApsOrder> findByPeriod(@Param("agencyId") Long agencyId, @Param("period") Integer period);

}
