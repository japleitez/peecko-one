package com.peecko.one.repository;

import com.peecko.one.domain.Contact;
import com.peecko.one.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("from Contact c left join fetch c.customer where c.id = :id")
    List<Contact> loadById(@Param("id") Long id);

    @Query("from Contact c left join fetch c.customer where c.customer.id = :customerId")
    List<Contact> getContactsByCustomer(@Param("customerId") Long customerId);

    @Query("from Contact c left join fetch c.customer where c.customer.code = :customerCode")
    List<Contact> getContactsByCustomerCode(@Param("customerCode") String customerCode);

}
