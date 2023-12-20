package com.peecko.one.repository;

import com.peecko.one.domain.ApsDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApsDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsDeviceRepository extends JpaRepository<ApsDevice, Long> {}
