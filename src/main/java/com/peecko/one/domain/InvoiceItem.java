package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceItem.
 */
@Entity
@Table(name = "invoice_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProductType type;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price_unit", nullable = false)
    private Double priceUnit;

    @NotNull
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @NotNull
    @Column(name = "vat_rate", nullable = false)
    private Double vatRate;

    @NotNull
    @Column(name = "vat_amount", nullable = false)
    private Double vat;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "invoiceItems", "apsOrder" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getType() {
        return this.type;
    }

    public InvoiceItem type(ProductType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public InvoiceItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public InvoiceItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceUnit() {
        return this.priceUnit;
    }

    public InvoiceItem priceUnit(Double priceUnit) {
        this.setPriceUnit(priceUnit);
        return this;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public InvoiceItem priceExtended(Double priceExtended) {
        this.setSubtotal(priceExtended);
        return this;
    }

    public void setSubtotal(Double priceExtended) {
        this.subtotal = priceExtended;
    }

    public Double getVatRate() {
        return this.vatRate;
    }

    public InvoiceItem vatRate(Double vatRate) {
        this.setVatRate(vatRate);
        return this;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Double getVat() {
        return this.vat;
    }

    public InvoiceItem vatAmount(Double vatAmount) {
        this.setVat(vatAmount);
        return this;
    }

    public void setVat(Double vatAmount) {
        this.vat = vatAmount;
    }

    public Double getTotal() {
        return this.total;
    }

    public InvoiceItem total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceItem invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItem)) {
            return false;
        }
        return getId() != null && getId().equals(((InvoiceItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + id +
            ", type=" + type +
            ", description='" + description + '\'' +
            ", quantity=" + quantity +
            ", priceUnit=" + priceUnit +
            ", subtotal=" + subtotal +
            ", vatRate=" + vatRate +
            ", vat=" + vat +
            ", total=" + total +
            ", invoice=" + invoice +
            '}';
    }
}
