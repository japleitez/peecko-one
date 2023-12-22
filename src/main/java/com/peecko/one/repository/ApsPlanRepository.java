package com.peecko.one.repository;

import com.peecko.one.domain.ApsPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the ApsPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsPlanRepository extends JpaRepository<ApsPlan, Long> {
    @Query("from ApsPlan p where p.customer.agency.id = :agencyId and p.customer.state = 'ACTIVE' and p.starts <= :endOfMonth and (p.ends is null or p.ends > :endOfMonth)")
    List<ApsPlan> currentActivePlans(@Param("agencyId") Long agencyId, @Param("endOfMonth") LocalDate endOfMonth);
}
