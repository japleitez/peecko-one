package com.peecko.one.repository;

import com.peecko.one.domain.ApsUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApsUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsUserRepository extends JpaRepository<ApsUser, Long> {}
