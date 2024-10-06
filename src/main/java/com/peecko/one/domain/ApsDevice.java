package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApsDevice.
 */
@Entity
@Table(name = "aps_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApsDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "phone_model")
    private String phoneModel;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "installed_on")
    private Instant installedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aps_user_id")
    @JsonIgnoreProperties(value = { "apsDevices", "playLists" }, allowSetters = true)
    private ApsUser apsUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApsDevice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public ApsDevice username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public ApsDevice deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneModel() {
        return this.phoneModel;
    }

    public ApsDevice phoneModel(String phoneModel) {
        this.setPhoneModel(phoneModel);
        return this;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public ApsDevice osVersion(String osVersion) {
        this.setOsVersion(osVersion);
        return this;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Instant getInstalledOn() {
        return this.installedOn;
    }

    public ApsDevice installedOn(Instant installedOn) {
        this.setInstalledOn(installedOn);
        return this;
    }

    public void setInstalledOn(Instant installedOn) {
        this.installedOn = installedOn;
    }

    public ApsUser getApsUser() {
        return this.apsUser;
    }

    public void setApsUser(ApsUser apsUser) {
        this.apsUser = apsUser;
    }

    public ApsDevice apsUser(ApsUser apsUser) {
        this.setApsUser(apsUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApsDevice)) {
            return false;
        }
        return getId() != null && getId().equals(((ApsDevice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApsDevice{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", phoneModel='" + getPhoneModel() + "'" +
            ", osVersion='" + getOsVersion() + "'" +
            ", installedOn='" + getInstalledOn() + "'" +
            "}";
    }
}
