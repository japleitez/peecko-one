package com.peecko.one.repository;

import com.peecko.one.domain.TrialLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrialLicenseRepository extends JpaRepository<TrialLicense, String> {}
