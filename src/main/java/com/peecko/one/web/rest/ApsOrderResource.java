package com.peecko.one.web.rest;

import com.peecko.one.domain.ApsOrder;
import com.peecko.one.security.SecurityUtils;
import com.peecko.one.service.*;
import com.peecko.one.service.info.ApsOrderInfo;
import com.peecko.one.service.request.ApsOrderListRequest;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import com.peecko.one.web.rest.errors.ErrorConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ApsOrder}.
 */
@RestController
@RequestMapping("/api/aps-orders")
@Transactional
public class ApsOrderResource {

    public static final String ERR_VALIDATION = ErrorConstants.ERR_VALIDATION;
    private final Logger log = LoggerFactory.getLogger(ApsOrderResource.class);

    private static final String ENTITY_NAME = "apsOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final InvoiceService invoiceService;
    private final InvoicePdfService invoicePdfService;
    private final InvoiceEmailService invoiceEmailService;
    private final ApsOrderService apsOrderService;
    private final PropertyService propertyService;
    private final ApsMembershipService apsMembershipService;

    public ApsOrderResource(InvoiceService invoiceService, InvoicePdfService invoicePdfService, InvoiceEmailService invoiceEmailService, ApsOrderService apsOrderService, PropertyService propertyService, ApsMembershipService apsMembershipService) {
        this.invoiceService = invoiceService;
        this.invoicePdfService = invoicePdfService;
        this.invoiceEmailService = invoiceEmailService;
        this.apsOrderService = apsOrderService;
        this.propertyService = propertyService;
        this.apsMembershipService = apsMembershipService;
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
        ApsOrder result = apsOrderService.create(apsOrder);
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
        validateUpdateInput(apsOrder, id);
        Optional<ApsOrder> result = apsOrderService.partialUpdateApsOrder(apsOrder);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsOrder.getId().toString())
        );
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
        validateUpdateInput(apsOrder, id);
        Optional<ApsOrder> result = apsOrderService.partialUpdateApsOrder(apsOrder);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apsOrder.getId().toString())
        );
    }

    private void validateUpdateInput(ApsOrder apsOrder, Long id) {
        if (apsOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apsOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (apsOrderService.notFound(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code GET  /aps-orders/filtered} : get the apsOrdersInfo filtered by customer and periods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apsOrderInfos in body.
     */
    @GetMapping("/info")
    public List<ApsOrderInfo> getAllApsOrders(
        @RequestParam(required = false) String customer,
        @RequestParam(required = false) String contract,
        @RequestParam(required = false) Integer period,
        @RequestParam(required = false) Integer starts,
        @RequestParam(required = false) Integer ends) {
        log.debug("REST request to get ApsOrders------------------");
        ApsOrderListRequest request = new ApsOrderListRequest(customer, contract, period, starts, ends);
        return apsOrderService.findAll(request).stream().map(ApsOrder::toApsOrderInfo).toList();
    }

    @GetMapping("/batch/orders")
    public List<ApsOrderInfo> batchOrders(
        @RequestParam() Integer period,
        @RequestParam(required = false) String contract) {
        log.debug("REST request to generate ApsOrders in batch");
        return apsOrderService.batchOrders(period, contract);
    }

    @GetMapping("/batch/invoices")
    public List<ApsOrderInfo> batchInvoices(
        @RequestParam() Integer period,
        @RequestParam(required = false) String contract) {
        log.debug("REST request to generate Invoices in batch");
        List<ApsOrderInfo> list = invoiceService.batchInvoice(period, contract);
        new Thread(new InvoiceGenerator(period, contract)).start();
        return list;
    }

    @GetMapping("/batch/emails")
    public List<ApsOrderInfo> batchEmails(
        @RequestParam() Integer period,
        @RequestParam(required = false) String contract) {
        log.debug("REST request to generate Emails in batch");
        return invoiceEmailService.batchInvoiceEmail(contract, period);
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
        Optional<ApsOrder> apsOrder = apsOrderService.findById(id);
        return ResponseUtil.wrapOrNotFound(apsOrder);
    }

    @GetMapping("/{id}/download/invoice")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable("id") Long id) throws FileNotFoundException {
        String invoiceNumber = apsOrderService.findById(id).map(ApsOrder::getInvoiceNumber).orElse(null);
        if (!StringUtils.hasText(invoiceNumber)) {
            return ResponseEntity.notFound().build();
        }
        String pathname = propertyService.resolveInvoicePathname(invoiceNumber);
        File file = new File(pathname);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(file.length())
            .body(resource);
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
        apsOrderService.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/import/members")
    public ResponseEntity<?> createBulkMembership(
        @RequestParam() Long apsOrderId,
        @RequestParam() MultipartFile file) {
        log.debug("REST request to import ApsMembership file : {}", file.getName());
        int count = apsMembershipService.importMembers(apsOrderId, file);
        log.info("batch imported {} apsMemberships for apsOrder {} from file {}", apsOrderId, file.getName(), count);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

    private class InvoiceGenerator implements Runnable {
        private final String contract;
        private final Integer period;
        public InvoiceGenerator(Integer period, String contract) {
            this.period = period;
            this.contract = contract;
        }
        @Override
        public void run() {
            invoicePdfService.batchInvoicePDF(contract, period);
        }
    }
}
