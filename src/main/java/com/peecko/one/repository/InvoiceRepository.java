package com.peecko.one.repository;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.Invoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByApsOrder(ApsOrder apsOrder);

    Long countByAgencyIdAndPeriod(Long agencyId, Integer period);

}
