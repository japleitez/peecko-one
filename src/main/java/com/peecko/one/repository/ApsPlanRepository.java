package com.peecko.one.repository;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.enumeration.PlanState;
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
public interface ApsPlanRepository extends JpaRepository<ApsPlan, Long>, JpaSpecificationExecutor<ApsPlan> {
    @Query("from ApsPlan p left join fetch p.customer where p.id = :id")
    List<ApsPlan> loadById(@Param("id") Long id);

    @Query("from ApsPlan p left join fetch p.customer where p.customer.agency.id = :agencyId")
    List<ApsPlan> getPlansForAgency(@Param("agencyId") Long agencyId);

    @Query("from ApsPlan p left join fetch p.customer where p.customer.agency.id = :agencyId and p.state in (:states)")
    List<ApsPlan> getPlansForAgencyAndStates(@Param("agencyId") Long agencyId, @Param("states") List<PlanState> states);

    @Query("from ApsPlan p join fetch p.customer where p.customer.agency.id = :agencyId and p.state = 'ACTIVE' and p.starts <= :starts and (p.ends is null or p.ends >= :ends)")
    List<ApsPlan> getActivePlansInPeriod(@Param("agencyId") Long agencyId, @Param("starts") LocalDate starts, @Param("ends") LocalDate ends);

    @Query("from ApsPlan p join fetch p.customer where p.state = 'ACTIVE' and p.ends is not null and p.ends < :today")
    List<ApsPlan> getActivePlansToClose(@Param("today") LocalDate today);

}
