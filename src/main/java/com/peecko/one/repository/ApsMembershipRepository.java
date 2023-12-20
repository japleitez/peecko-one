package com.peecko.one.repository;

import com.peecko.one.domain.ApsMembership;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApsMembership entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsMembershipRepository extends JpaRepository<ApsMembership, Long> {}
