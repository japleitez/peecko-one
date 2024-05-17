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
public interface ApsOrderRepository extends JpaRepository<ApsOrder, Long>, JpaSpecificationExecutor<ApsOrder> {

    @Query("from ApsOrder o left join fetch o.apsPlan where o.period = :period and o.apsPlan.customer.agency.id = :agencyId")
    List<ApsOrder> findByAgencyAndPeriod(@Param("agencyId") Long agencyId, @Param("period") Integer period);

    @Query("from ApsOrder o left join fetch o.apsPlan where o.period = :period and o.apsPlan.customer.agency.id = :agencyId and o.apsPlan.state = 'ACTIVE'")
    List<ApsOrder> findByAgencyAndPeriodAndActive(@Param("agencyId") Long agencyId, @Param("period") Integer period);

    @Query("from ApsOrder o left join fetch o.apsPlan where o.apsPlan.customer.id = :customerId and o.period between :startPeriod and :endPeriod order by o.period")
    List<ApsOrder> findByCustomerAndBetweenPeriods(@Param("customerId") Long customerId, @Param("startPeriod") Integer startPeriod, @Param("endPeriod") Integer endPeriod);

    @Query("from ApsOrder o left join fetch o.apsPlan where o.apsPlan.customer.id = :customerId and o.period >= :startPeriod order by o.period")
    List<ApsOrder> findByCustomerAndStartPeriod(@Param("customerId") Long customerId, @Param("startPeriod") Integer startPeriod);

    @Query("from ApsOrder o left join fetch o.apsPlan where o.apsPlan.customer.id = :customerId and o.period <= :endPeriod order by o.period")
    List<ApsOrder> findByCustomerAndEndPeriod(@Param("customerId") Long customerId, @Param("endPeriod") Integer endPeriod);

    @Query("from ApsOrder o left join fetch o.apsPlan where o.apsPlan.contract = :contract")
    List<ApsOrder> findByApsPlanContract(@Param("contract") String contract);

}
