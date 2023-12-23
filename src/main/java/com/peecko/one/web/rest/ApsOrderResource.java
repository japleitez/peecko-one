package com.peecko.one.web.rest;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.repository.ApsOrderRepository;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.ApsOrderService;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.utils.PeriodUtils;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.peecko.one.domain.ApsOrder}.
 */
@RestController
@RequestMapping("/api/aps-orders")
@Transactional
public class ApsOrderResource {

    private final Logger log = LoggerFactory.getLogger(ApsOrderResource.class);

    private static final String ENTITY_NAME = "apsOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApsOrderRepository apsOrderRepository;

    private final ApsOrderService apsOrderService;

    public ApsOrderResource(ApsOrderRepository apsOrderRepository, ApsOrderService apsOrderService) {
        this.apsOrderRepository = apsOrderRepository;
        this.apsOrderService = apsOrderService;
    }

    /**
     * {@code POST  /aps-orders} : Create a new apsOrder.
     *
     * @param apsOrder the apsOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apsOrder, or with status {@code 400 (Bad Request)} if the apsOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApsOrder> createApsOrder(@Valid @RequestBody ApsOrder apsOrder) throws URISyntaxException {
        log.debug("REST request to save ApsOrder : {}", apsOrder);
        if (apsOrder.getId() != null) {
            throw new BadRequestAlertException("A new apsOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApsOrder result = apsOrderRepository.save(apsOrder);
        return ResponseEntity
            .created(new URI("/api/aps-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aps-orders/:id} : Updates an existing apsOrder.
     *
     * @param id the id of the apsOrder to save.
     * @param apsOrder the apsOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsOrder,
     * or with status {@code 400 (Bad Request)} if the apsOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apsOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApsOrder> updateApsOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApsOrder apsOrder
    ) throws URISyntaxException {
        log.debug("REST request to update ApsOrder : {}, {}", id, apsOrder);
        if (apsOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApsOrder result = apsOrderRepository.save(apsOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aps-orders/:id} : Partial updates given fields of an existing apsOrder, field will ignore if it is null
     *
     * @param id the id of the apsOrder to save.
     * @param apsOrder the apsOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apsOrder,
     * or with status {@code 400 (Bad Request)} if the apsOrder is not valid,
     * or with status {@code 404 (Not Found)} if the apsOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the apsOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApsOrder> partialUpdateApsOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApsOrder apsOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApsOrder partially : {}, {}", id, apsOrder);
        if (apsOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apsOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApsOrder> result = apsOrderRepository
            .findById(apsOrder.getId())
            .map(existingApsOrder -> {
                if (apsOrder.getPeriod() != null) {
                    existingApsOrder.setPeriod(apsOrder.getPeriod());
                }
                if (apsOrder.getLicense() != null) {
                    existingApsOrder.setLicense(apsOrder.getLicense());
                }
                if (apsOrder.getUnitPrice() != null) {
                    existingApsOrder.setUnitPrice(apsOrder.getUnitPrice());
                }
                if (apsOrder.getVatRate() != null) {
                    existingApsOrder.setVatRate(apsOrder.getVatRate());
                }
                if (apsOrder.getNumberOfUsers() != null) {
                    existingApsOrder.setNumberOfUsers(apsOrder.getNumberOfUsers());
                }
                if (apsOrder.getInvoiceNumber() != null) {
                    existingApsOrder.setInvoiceNumber(apsOrder.getInvoiceNumber());
                }

                return existingApsOrder;
            })
            .map(apsOrderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /aps-orders} : get all the apsOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apsOrders in body.
     */
    @GetMapping("")
    public List<ApsOrderInfo> getAllApsOrders() {
        log.debug("REST request to get all ApsOrders");
        YearMonth yearMonth = YearMonth.now();
        Integer period = PeriodUtils.getPeriod(yearMonth);
        Long agencyId = SecurityUtils.getCurrentUserAgencyId();
        return apsOrderRepository.findByPeriod(agencyId, period).stream().map(ApsOrder::toApsOrderInfo).toList();
    }

    @GetMapping("/batch/generate")
    public List<ApsOrderInfo> batchGenerate() {
        log.debug("REST request to batch generate ApsOrders");
        YearMonth yearMonth = YearMonth.now();
        Long agencyId = SecurityUtils.getCurrentUserAgencyId();
        return apsOrderService.batchGenerate(agencyId, yearMonth);
    }

    /**
     * {@code GET  /aps-orders/:id} : get the "id" apsOrder.
     *
     * @param id the id of the apsOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apsOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApsOrder> getApsOrder(@PathVariable("id") Long id) {
        log.debug("REST request to get ApsOrder : {}", id);
        Optional<ApsOrder> apsOrder = apsOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apsOrder);
    }

    /**
     * {@code DELETE  /aps-orders/:id} : delete the "id" apsOrder.
     *
     * @param id the id of the apsOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApsOrder(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApsOrder : {}", id);
        apsOrderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
