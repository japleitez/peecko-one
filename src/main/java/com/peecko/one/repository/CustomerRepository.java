package com.peecko.one.repository;

import com.peecko.one.domain.Agency;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    @Query("select c from Customer c left join fetch c.agency where c.id = :id")
    List<Customer> loadById(@Param("id") Long id);

    Page<Customer> findByAgency(Agency agency, Pageable pageable);

    @Query("from Customer c left join fetch c.agency where c.agency = :agency and c.state = :customerState order by c.name")
    List<Customer> findByAgencyAndCustomerState(Agency agency, CustomerState customerState);

}
