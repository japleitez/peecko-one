package com.peecko.one.repository;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.Staff;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Staff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("from Staff s left join fetch s.agency where s.id = :id")
    List<Staff> loadById(@Param("id") Long id);

    @Query("from Staff s left join fetch s.agency")
    List<Staff> findAllWithAgencies();

}
