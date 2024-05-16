package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "issued", nullable = false)
    private Instant issued;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @NotNull
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @NotNull
    @Column(name = "vat", nullable = false)
    private Double vat;

    @NotNull
    @Column(name = "vat_rate", nullable = false)
    private Double vatRate;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "paid")
    private Double paid;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "diff")
    private Double diff;

    @Column(name = "notes")
    private String notes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoice" }, allowSetters = true)
    private Set<InvoiceItem> invoiceItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "apsMemberships", "invoices", "apsPlan" }, allowSetters = true)
    private ApsOrder apsOrder;

    @Column(name = "country")
    private String country;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "aps_plan_id")
    private Long apsPlanId;

    @Column(name = "agency_id")
    private Long agencyId;

    @Column(name = "period")
    private Integer period;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public Invoice number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Instant getIssued() {
        return this.issued;
    }

    public Invoice issued(Instant issued) {
        this.setIssued(issued);
        return this;
    }

    public void setIssued(Instant issued) {
        this.issued = issued;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Invoice dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getSaleDate() {
        return this.saleDate;
    }

    public Invoice saleDate(LocalDate saleDate) {
        this.setSaleDate(saleDate);
        return this;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public Invoice subtotal(Double subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getVat() {
        return this.vat;
    }

    public Invoice vat(Double vat) {
        this.setVat(vat);
        return this;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Invoice vatRate(Double vatRate) {
        this.setVatRate(vatRate);
        return this;
    }

    public Double getTotal() {
        return this.total;
    }

    public Invoice total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPaid() {
        return this.paid;
    }

    public Invoice paid(Double paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public LocalDate getPaidDate() {
        return this.paidDate;
    }

    public Invoice paidDate(LocalDate paidDate) {
        this.setPaidDate(paidDate);
        return this;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public Double getDiff() {
        return this.diff;
    }

    public Invoice diff(Double diff) {
        this.setDiff(diff);
        return this;
    }

    public void setDiff(Double diff) {
        this.diff = diff;
    }

    public String getNotes() {
        return this.notes;
    }

    public Invoice notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<InvoiceItem> getInvoiceItems() {
        return this.invoiceItems;
    }

    public void setInvoiceItems(Set<InvoiceItem> invoiceItems) {
        if (this.invoiceItems != null) {
            this.invoiceItems.forEach(i -> i.setInvoice(null));
        }
        if (invoiceItems != null) {
            invoiceItems.forEach(i -> i.setInvoice(this));
        }
        this.invoiceItems = invoiceItems;
    }

    public Invoice invoiceItems(Set<InvoiceItem> invoiceItems) {
        this.setInvoiceItems(invoiceItems);
        return this;
    }

    public Invoice addInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItems.add(invoiceItem);
        invoiceItem.setInvoice(this);
        return this;
    }

    public Invoice removeInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItems.remove(invoiceItem);
        invoiceItem.setInvoice(null);
        return this;
    }

    public ApsOrder getApsOrder() {
        return this.apsOrder;
    }

    public void setApsOrder(ApsOrder apsOrder) {
        this.apsOrder = apsOrder;
    }

    public Invoice apsOrder(ApsOrder apsOrder) {
        this.setApsOrder(apsOrder);
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Invoice country(String country) {
        this.setCountry(country);
        return this;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Invoice customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public Long getApsPlanId() {
        return apsPlanId;
    }

    public void setApsPlanId(Long apsPlanId) {
        this.apsPlanId = apsPlanId;
    }

    public Invoice apsPlanId(Long apsPlanId) {
        this.setApsPlanId(apsPlanId);
        return this;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Invoice agencyId(Long agencyId) {
        this.setAgencyId(agencyId);
        return this;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Invoice period(Integer period) {
        this.setPeriod(period);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", issued='" + getIssued() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", saleDate='" + getSaleDate() + "'" +
            ", subtotal=" + getSubtotal() +
            ", vat=" + getVat() +
            ", total=" + getTotal() +
            ", paid=" + getPaid() +
            ", paidDate='" + getPaidDate() + "'" +
            ", diff=" + getDiff() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
