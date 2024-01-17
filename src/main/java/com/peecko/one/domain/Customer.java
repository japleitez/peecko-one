package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.CustomerState;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

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

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "license")
    private String license;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private CustomerState state;

    @Column(name = "close_reason")
    private String closeReason;

    @Column(name = "email_domains")
    private String emailDomains;

    @Column(name = "vat_id")
    private String vatId;

    @Column(name = "bank")
    private String bank;

    @Column(name = "iban")
    private String iban;

    @Column(name = "logo")
    private String logo;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "trialed")
    private Instant trialed;

    @Column(name = "declined")
    private Instant declined;

    @Column(name = "activated")
    private Instant activated;

    @Column(name = "closed")
    private Instant closed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apsOrders", "customer" }, allowSetters = true)
    private Set<ApsPlan> apsPlans = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "staff", "customers", "apsPricings" }, allowSetters = true)
    private Agency agency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public static Customer of(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Customer code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public Customer country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLicense() {
        return this.license;
    }

    public Customer license(String license) {
        this.setLicense(license);
        return this;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public CustomerState getState() {
        return this.state;
    }

    public Customer state(CustomerState state) {
        this.setState(state);
        return this;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public String getCloseReason() {
        return this.closeReason;
    }

    public Customer closeReason(String closeReason) {
        this.setCloseReason(closeReason);
        return this;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public String getEmailDomains() {
        return this.emailDomains;
    }

    public Customer emailDomains(String emailDomains) {
        this.setEmailDomains(emailDomains);
        return this;
    }

    public void setEmailDomains(String emailDomains) {
        this.emailDomains = emailDomains;
    }

    public String getVatId() {
        return this.vatId;
    }

    public Customer vatId(String vatId) {
        this.setVatId(vatId);
        return this;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public String getBank() {
        return this.bank;
    }

    public Customer bank(String bank) {
        this.setBank(bank);
        return this;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIban() {
        return this.iban;
    }

    public Customer iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getLogo() {
        return this.logo;
    }

    public Customer logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNotes() {
        return this.notes;
    }

    public Customer notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Customer created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public Customer updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getTrialed() {
        return this.trialed;
    }

    public Customer trialed(Instant trialed) {
        this.setTrialed(trialed);
        return this;
    }

    public void setTrialed(Instant trialed) {
        this.trialed = trialed;
    }

    public Instant getDeclined() {
        return this.declined;
    }

    public Customer declined(Instant declined) {
        this.setDeclined(declined);
        return this;
    }

    public void setDeclined(Instant declined) {
        this.declined = declined;
    }

    public Instant getActivated() {
        return this.activated;
    }

    public Customer activated(Instant activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Instant activated) {
        this.activated = activated;
    }

    public Instant getClosed() {
        return this.closed;
    }

    public Customer closed(Instant closed) {
        this.setClosed(closed);
        return this;
    }

    public void setClosed(Instant closed) {
        this.closed = closed;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setCustomer(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setCustomer(this));
        }
        this.contacts = contacts;
    }

    public Customer contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Customer addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setCustomer(this);
        return this;
    }

    public Customer removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setCustomer(null);
        return this;
    }

    public Set<ApsPlan> getApsPlans() {
        return this.apsPlans;
    }

    public void setApsPlans(Set<ApsPlan> apsPlans) {
        if (this.apsPlans != null) {
            this.apsPlans.forEach(i -> i.setCustomer(null));
        }
        if (apsPlans != null) {
            apsPlans.forEach(i -> i.setCustomer(this));
        }
        this.apsPlans = apsPlans;
    }

    public Customer apsPlans(Set<ApsPlan> apsPlans) {
        this.setApsPlans(apsPlans);
        return this;
    }

    public Customer addApsPlan(ApsPlan apsPlan) {
        this.apsPlans.add(apsPlan);
        apsPlan.setCustomer(this);
        return this;
    }

    public Customer removeApsPlan(ApsPlan apsPlan) {
        this.apsPlans.remove(apsPlan);
        apsPlan.setCustomer(null);
        return this;
    }

    public Agency getAgency() {
        return this.agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Customer agency(Agency agency) {
        this.setAgency(agency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Customer cloneForSelection() {
        Customer clone = new Customer();
        clone.id = this.id;
        clone.code = this.code;
        clone.name = this.name;
        clone.state = this.state;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", license='" + getLicense() + "'" +
            ", state='" + getState() + "'" +
            ", closeReason='" + getCloseReason() + "'" +
            ", emailDomains='" + getEmailDomains() + "'" +
            ", vatId='" + getVatId() + "'" +
            ", bank='" + getBank() + "'" +
            ", iban='" + getIban() + "'" +
            ", logo='" + getLogo() + "'" +
            ", notes='" + getNotes() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", trialed='" + getTrialed() + "'" +
            ", declined='" + getDeclined() + "'" +
            ", activated='" + getActivated() + "'" +
            ", closed='" + getClosed() + "'" +
            "}";
    }
}
