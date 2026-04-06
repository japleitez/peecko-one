package com.peecko.one.repository;

import com.peecko.one.domain.Contact;
import com.peecko.one.domain.enumeration.ContactType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT c FROM Contact c WHERE c.customer.id = :customerId and c.type = :type")
    Optional<Contact> findByCustomerAndType(@Param("customerId") Long customerId, @Param("type") ContactType type);
}
