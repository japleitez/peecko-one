package com.peecko.one.web.rest;

import com.peecko.one.domain.InvoiceItem;
import com.peecko.one.repository.InvoiceItemRepository;
import com.peecko.one.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.peecko.one.domain.InvoiceItem}.
 */
@RestController
@RequestMapping("/api/invoice-items")
@Transactional
public class InvoiceItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemResource.class);

    private static final String ENTITY_NAME = "invoiceItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceItemRepository invoiceItemRepository;

    public InvoiceItemResource(InvoiceItemRepository invoiceItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
    }

    /**
     * {@code POST  /invoice-items} : Create a new invoiceItem.
     *
     * @param invoiceItem the invoiceItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItem, or with status {@code 400 (Bad Request)} if the invoiceItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceItem> createInvoiceItem(@Valid @RequestBody InvoiceItem invoiceItem) throws URISyntaxException {
        log.debug("REST request to save InvoiceItem : {}", invoiceItem);
        if (invoiceItem.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceItem result = invoiceItemRepository.save(invoiceItem);
        return ResponseEntity
            .created(new URI("/api/invoice-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-items/:id} : Updates an existing invoiceItem.
     *
     * @param id the id of the invoiceItem to save.
     * @param invoiceItem the invoiceItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItem,
     * or with status {@code 400 (Bad Request)} if the invoiceItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItem> updateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceItem invoiceItem
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceItem : {}, {}", id, invoiceItem);
        if (invoiceItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceItem result = invoiceItemRepository.save(invoiceItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-items/:id} : Partial updates given fields of an existing invoiceItem, field will ignore if it is null
     *
     * @param id the id of the invoiceItem to save.
     * @param invoiceItem the invoiceItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItem,
     * or with status {@code 400 (Bad Request)} if the invoiceItem is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceItem> partialUpdateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceItem invoiceItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceItem partially : {}, {}", id, invoiceItem);
        if (invoiceItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItem> result = invoiceItemRepository
            .findById(invoiceItem.getId())
            .map(existingInvoiceItem -> {
                if (invoiceItem.getType() != null) {
                    existingInvoiceItem.setType(invoiceItem.getType());
                }
                if (invoiceItem.getDescription() != null) {
                    existingInvoiceItem.setDescription(invoiceItem.getDescription());
                }
                if (invoiceItem.getQuantity() != null) {
                    existingInvoiceItem.setQuantity(invoiceItem.getQuantity());
                }
                if (invoiceItem.getPriceUnit() != null) {
                    existingInvoiceItem.setPriceUnit(invoiceItem.getPriceUnit());
                }
                if (invoiceItem.getSubtotal() != null) {
                    existingInvoiceItem.setSubtotal(invoiceItem.getSubtotal());
                }
                if (invoiceItem.getVatRate() != null) {
                    existingInvoiceItem.setVatRate(invoiceItem.getVatRate());
                }
                if (invoiceItem.getVat() != null) {
                    existingInvoiceItem.setVat(invoiceItem.getVat());
                }
                if (invoiceItem.getTotal() != null) {
                    existingInvoiceItem.setTotal(invoiceItem.getTotal());
                }

                return existingInvoiceItem;
            })
            .map(invoiceItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceItem.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-items} : get all the invoiceItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceItems in body.
     */
    @GetMapping("")
    public List<InvoiceItem> getAllInvoiceItems() {
        log.debug("REST request to get all InvoiceItems");
        return invoiceItemRepository.findAll();
    }

    /**
     * {@code GET  /invoice-items/:id} : get the "id" invoiceItem.
     *
     * @param id the id of the invoiceItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItem> getInvoiceItem(@PathVariable("id") Long id) {
        log.debug("REST request to get InvoiceItem : {}", id);
        Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoiceItem);
    }

    /**
     * {@code DELETE  /invoice-items/:id} : delete the "id" invoiceItem.
     *
     * @param id the id of the invoiceItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete InvoiceItem : {}", id);
        invoiceItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
