package com.peecko.one.repository;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.domain.Invoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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
    @Query("from Invoice i left join fetch i.invoiceItems where i.agencyId = :agencyId and i.period = :period")
    List<Invoice> findByAgencyAndPeriod(@Param("agencyId") Long agencyId, @Param("period") Integer period);

    @Query("from Invoice i left join fetch i.invoiceItems where i.apsOrder.apsPlan.contract = :contract and i.period = :period")
    List<Invoice> findByContractAndPeriod(@Param("contract") String contract, @Param("period") Integer period);

}
