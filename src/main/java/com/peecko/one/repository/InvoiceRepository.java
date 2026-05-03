package com.peecko.one.repository;

import com.peecko.one.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    Long countByAgencyIdAndPeriod(Long agencyId, Integer period);

    @Query("from Invoice i left join fetch i.invoiceItems where i.agencyId = :agencyId and i.period = :period")
    List<Invoice> findByAgencyAndPeriod(@Param("agencyId") Long agencyId, @Param("period") Integer period);

    @Query("from Invoice i left join fetch i.invoiceItems where i.apsOrder.apsPlan.contract = :contract and i.period = :period")
    List<Invoice> findByContractAndPeriod(@Param("contract") String contract, @Param("period") Integer period);

    @EntityGraph(attributePaths = { "invoiceItems" })
    Optional<Invoice> findByApsOrderId(Long apsOrderId);

    @EntityGraph(attributePaths = { "invoiceItems" })
    Optional<Invoice> findWithItemsById(Long id);
}
