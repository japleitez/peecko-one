package com.peecko.one.repository;

import com.peecko.one.domain.ApsMembership;
import com.peecko.one.domain.ApsOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ApsMembership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsMembershipRepository extends JpaRepository<ApsMembership, Long> {
    Optional<ApsMembership> findByApsOrderAndUsername(ApsOrder apsOrder, String username);
    Long countByApsOrder(ApsOrder apsOrder);

    List<ApsMembership> findByApsOrder(ApsOrder apsOrder);
}
