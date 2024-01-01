package com.peecko.one.repository;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByAgency(Agency agency, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.agency = :agency AND c.state = :customerState ORDER BY c.name")
    List<Customer> findByAgencyAndCustomerState(Agency agency, CustomerState customerState);
}
