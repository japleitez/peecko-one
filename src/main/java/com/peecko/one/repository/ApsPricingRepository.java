package com.peecko.one.repository;

import com.peecko.one.domain.ApsPricing;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ApsPricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApsPricingRepository extends JpaRepository<ApsPricing, Long> {
    @Query("from ApsPricing p left join fetch p.customer where p.customer.id = :customerId and p.minQuantity <= :numberOfUsers order by p.minQuantity desc")
    List<ApsPricing> findByCustomerIdAndNumberOfUsers(@Param("customerId") Long customerId, @Param("numberOfUsers") Integer numberOfUsers);

}
