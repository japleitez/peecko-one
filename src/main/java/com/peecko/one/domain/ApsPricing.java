package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApsPricing.
 */
@Entity
@Table(name = "aps_pricing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApsPricing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aps_pricing_gen")
    @SequenceGenerator(name = "aps_pricing_gen", sequenceName = "aps_pricing_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "index", nullable = false)
    private Integer index;

    @NotNull
    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity;

    @NotNull
    @Column(name = "max_quantity", nullable = false)
    private Integer maxQuantity;

    @NotNull
    @Column(name = "fitness_price", nullable = false)
    private Double fitnessPrice;

    @NotNull
    @Column(name = "wellness_price", nullable = false)
    private Double wellnessPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "contacts", "apsPlans", "agency" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApsPricing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ApsPricing country(String country) {
        this.setCountry(country);
        return this;
    }

    public Integer getIndex() {
        return this.index;
    }

    public ApsPricing index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getMinQuantity() {
        return this.minQuantity;
    }

    public ApsPricing minQuantity(Integer minQuantity) {
        this.setMinQuantity(minQuantity);
        return this;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return this.maxQuantity;
    }

    public ApsPricing maxQuantity(Integer maxQuantity) {
        this.setMaxQuantity(maxQuantity);
        return this;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Double getFitnessPrice() {
        return this.fitnessPrice;
    }

    public ApsPricing fitnessPrice(Double fitnessPrice) {
        this.setFitnessPrice(fitnessPrice);
        return this;
    }

    public void setFitnessPrice(Double fitnessPrice) {
        this.fitnessPrice = fitnessPrice;
    }

    public Double getWellnessPrice() {
        return this.wellnessPrice;
    }

    public ApsPricing wellnessPrice(Double wellnessPrice) {
        this.setWellnessPrice(wellnessPrice);
        return this;
    }

    public void setWellnessPrice(Double premiumPrice) {
        this.wellnessPrice = premiumPrice;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ApsPricing customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApsPricing)) {
            return false;
        }
        return getId() != null && getId().equals(((ApsPricing) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApsPricing{" +
            "id=" + getId() +
            ", index=" + getIndex() +
            ", minQuantity=" + getMinQuantity() +
            ", maxQuantity=" + getMaxQuantity() +
            ", fitnessPrice=" + getFitnessPrice() +
            ", wellnessPrice=" + getWellnessPrice() +
            "}";
    }
}
