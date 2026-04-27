package com.peecko.one.repository;

import com.peecko.one.domain.Coach;
import com.peecko.one.domain.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByCode(String code);
}
