package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.PlanState;
import com.peecko.one.domain.enumeration.PricingType;
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
 * A ApsPlan.
 */
@Entity
@Table(name = "aps_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApsPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contract", nullable = false)
    private String contract;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing", nullable = false)
    private PricingType pricing;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PlanState state;

    @NotNull
    @Column(name = "license")
    private String license;

    @NotNull
    @Column(name = "starts")
    private LocalDate starts;

    @Column(name = "ends")
    private LocalDate ends;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "agency_id")
    private Long agencyId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apsPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apsMemberships", "invoices", "apsPlan" }, allowSetters = true)
    private Set<ApsOrder> apsOrders = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "contacts", "apsPlans", "agency" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApsPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContract() {
        return this.contract;
    }

    public ApsPlan contract(String contract) {
        this.setContract(contract);
        return this;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public PricingType getPricing() {
        return this.pricing;
    }

    public ApsPlan pricing(PricingType pricing) {
        this.setPricing(pricing);
        return this;
    }

    public void setPricing(PricingType pricing) {
        this.pricing = pricing;
    }

    public PlanState getState() {
        return this.state;
    }

    public ApsPlan state(PlanState state) {
        this.setState(state);
        return this;
    }

    public void setState(PlanState state) {
        this.state = state;
    }

    public String getLicense() {
        return this.license;
    }

    public ApsPlan license(String license) {
        this.setLicense(license);
        return this;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public LocalDate getStarts() {
        return this.starts;
    }

    public ApsPlan starts(LocalDate starts) {
        this.setStarts(starts);
        return this;
    }

    public void setStarts(LocalDate starts) {
        this.starts = starts;
    }

    public LocalDate getEnds() {
        return this.ends;
    }

    public ApsPlan ends(LocalDate ends) {
        this.setEnds(ends);
        return this;
    }

    public void setEnds(LocalDate ends) {
        this.ends = ends;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public ApsPlan unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNotes() {
        return this.notes;
    }

    public ApsPlan notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return this.created;
    }

    public ApsPlan created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public ApsPlan updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public ApsPlan agencyId(Long agencyId) {
        this.setAgencyId(agencyId);
        return this;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Set<ApsOrder> getApsOrders() {
        return this.apsOrders;
    }

    public void setApsOrders(Set<ApsOrder> apsOrders) {
        if (this.apsOrders != null) {
            this.apsOrders.forEach(i -> i.setApsPlan(null));
        }
        if (apsOrders != null) {
            apsOrders.forEach(i -> i.setApsPlan(this));
        }
        this.apsOrders = apsOrders;
    }

    public ApsPlan apsOrders(Set<ApsOrder> apsOrders) {
        this.setApsOrders(apsOrders);
        return this;
    }

    public ApsPlan addApsOrder(ApsOrder apsOrder) {
        this.apsOrders.add(apsOrder);
        apsOrder.setApsPlan(this);
        return this;
    }

    public ApsPlan removeApsOrder(ApsOrder apsOrder) {
        this.apsOrders.remove(apsOrder);
        apsOrder.setApsPlan(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ApsPlan customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public static ApsPlan of(Long id) {
        ApsPlan apsPlan = new ApsPlan();
        apsPlan.setId(id);
        return apsPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApsPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((ApsPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "ApsPlan{" +
            "id=" + id +
            ", contract='" + contract + '\'' +
            ", pricing=" + pricing +
            ", state=" + state +
            ", license='" + license + '\'' +
            ", starts=" + starts +
            ", ends=" + ends +
            ", unitPrice=" + unitPrice +
            '}';
    }
}
