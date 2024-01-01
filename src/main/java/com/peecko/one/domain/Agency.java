package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Agency.
 */
@Entity
@Table(name = "agency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "line_1")
    private String line1;

    @Column(name = "line_2")
    private String line2;

    @Column(name = "zip")
    private String zip;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "billing_email")
    private String billingEmail;

    @Column(name = "billing_phone")
    private String billingPhone;

    @Column(name = "bank")
    private String bank;

    @Column(name = "iban")
    private String iban;

    @Column(name = "rcs")
    private String rcs;

    @Column(name = "vat_id")
    private String vatId;

    @Column(name = "vat_rate")
    private Double vatRate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agency" }, allowSetters = true)
    private Set<Staff> staff = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "apsPlans", "agency" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "agency" }, allowSetters = true)
    private Set<ApsPricing> apsPricings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Agency code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Agency name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return this.line1;
    }

    public Agency line1(String line1) {
        this.setLine1(line1);
        return this;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public Agency line2(String line2) {
        this.setLine2(line2);
        return this;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getZip() {
        return this.zip;
    }

    public Agency zip(String zip) {
        this.setZip(zip);
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return this.city;
    }

    public Agency city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Agency country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Agency language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getEmail() {
        return this.email;
    }

    public Agency email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Agency phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBillingEmail() {
        return this.billingEmail;
    }

    public Agency billingEmail(String billingEmail) {
        this.setBillingEmail(billingEmail);
        return this;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getBillingPhone() {
        return this.billingPhone;
    }

    public Agency billingPhone(String billingPhone) {
        this.setBillingPhone(billingPhone);
        return this;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getBank() {
        return this.bank;
    }

    public Agency bank(String bank) {
        this.setBank(bank);
        return this;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIban() {
        return this.iban;
    }

    public Agency iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getRcs() {
        return this.rcs;
    }

    public Agency rcs(String rcs) {
        this.setRcs(rcs);
        return this;
    }

    public void setRcs(String rcs) {
        this.rcs = rcs;
    }

    public String getVatId() {
        return this.vatId;
    }

    public Agency vatId(String vatId) {
        this.setVatId(vatId);
        return this;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public Double getVatRate() {
        return this.vatRate;
    }

    public Agency vatRate(Double vatRate) {
        this.setVatRate(vatRate);
        return this;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public String getNotes() {
        return this.notes;
    }

    public Agency notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Agency created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public Agency updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Set<Staff> getStaff() {
        return this.staff;
    }

    public void setStaff(Set<Staff> staff) {
        if (this.staff != null) {
            this.staff.forEach(i -> i.setAgency(null));
        }
        if (staff != null) {
            staff.forEach(i -> i.setAgency(this));
        }
        this.staff = staff;
    }

    public Agency staff(Set<Staff> staff) {
        this.setStaff(staff);
        return this;
    }

    public Agency addStaff(Staff staff) {
        this.staff.add(staff);
        staff.setAgency(this);
        return this;
    }

    public Agency removeStaff(Staff staff) {
        this.staff.remove(staff);
        staff.setAgency(null);
        return this;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setAgency(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setAgency(this));
        }
        this.customers = customers;
    }

    public Agency customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Agency addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setAgency(this);
        return this;
    }

    public Agency removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setAgency(null);
        return this;
    }

    public Set<ApsPricing> getApsPricings() {
        return this.apsPricings;
    }

    public void setApsPricings(Set<ApsPricing> apsPricings) {
        if (this.apsPricings != null) {
            this.apsPricings.forEach(i -> i.setAgency(null));
        }
        if (apsPricings != null) {
            apsPricings.forEach(i -> i.setAgency(this));
        }
        this.apsPricings = apsPricings;
    }

    public Agency apsPricings(Set<ApsPricing> apsPricings) {
        this.setApsPricings(apsPricings);
        return this;
    }

    public Agency addApsPricing(ApsPricing apsPricing) {
        this.apsPricings.add(apsPricing);
        apsPricing.setAgency(this);
        return this;
    }

    public Agency removeApsPricing(ApsPricing apsPricing) {
        this.apsPricings.remove(apsPricing);
        apsPricing.setAgency(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public static Agency of(Long id) {
        Agency agency = new Agency();
        agency.id = id;
        return agency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agency)) {
            return false;
        }
        return getId() != null && getId().equals(((Agency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agency{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", line1='" + getLine1() + "'" +
            ", line2='" + getLine2() + "'" +
            ", zip='" + getZip() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", language='" + getLanguage() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", billingEmail='" + getBillingEmail() + "'" +
            ", billingPhone='" + getBillingPhone() + "'" +
            ", bank='" + getBank() + "'" +
            ", iban='" + getIban() + "'" +
            ", rcs='" + getRcs() + "'" +
            ", vatId='" + getVatId() + "'" +
            ", vatRate=" + getVatRate() +
            ", notes='" + getNotes() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
