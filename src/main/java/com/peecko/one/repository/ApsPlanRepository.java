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
    @Query("from ApsPlan p where p.customer.agency.id = :agencyId")
    List<ApsPlan> getPlansForAgency(@Param("agencyId") Long agencyId);

    @Query("from ApsPlan p where p.customer.agency.id = :agencyId and p.state = 'ACTIVE'")
    List<ApsPlan> currentPaidActivePlans(@Param("agencyId") Long agencyId);

    @Query("from ApsPlan p where exists (from ApsPlan p2 where p2.customer.id = :customerId and p2.state = 'TRIAL' and ((p2.starts between :starts and :ends) or (p2.ends between :starts and :ends)))")
    List<ApsPlan> overlappingTrialPlans(@Param("customerId") Long customerId, @Param("starts") LocalDate start, @Param("ends") LocalDate ends);
}
