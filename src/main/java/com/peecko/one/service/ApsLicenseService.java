package com.peecko.one.service;

import com.peecko.one.domain.ApsPlan;
import com.peecko.one.domain.Customer;
import com.peecko.one.domain.enumeration.CustomerState;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.domain.enumeration.PricingType;
import com.peecko.one.repository.ApsPlanRepository;
import com.peecko.one.repository.CustomerRepository;
import com.peecko.one.service.request.ActivateTrialPlanRequest;
import com.peecko.one.utils.UUIDUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApsLicenseService {

    public ApsLicenseService() {
    }

}
