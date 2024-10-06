package com.peecko.one.domain;

import com.peecko.one.domain.enumeration.Verification;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pin_code")
public class PinCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "request_id", updatable = false, nullable = false)
    private UUID requestId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "agency_email")
    String agencyEmail;

    @Column(name = "language")
    String language;

    @Column(name = "template")
    String template;

    @Column(name = "expire_at", nullable = false)
    LocalDateTime expireAt;

    Verification verification;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAgencyEmail() {
        return agencyEmail;
    }

    public void setAgencyEmail(String agencyEmail) {
        this.agencyEmail = agencyEmail;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }
}
