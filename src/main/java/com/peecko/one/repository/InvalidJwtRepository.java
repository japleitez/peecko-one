package com.peecko.one.repository;

import com.peecko.one.domain.InvalidJwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidJwtRepository extends JpaRepository<InvalidJwt, Long> {

}
