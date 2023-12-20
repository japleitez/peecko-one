package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.CoachType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coach.
 */
@Entity
@Table(name = "coach")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CoachType type;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "country")
    private String country;

    @Column(name = "speaks")
    private String speaks;

    @Column(name = "resume")
    private String resume;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "videoCategory", "coach" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "articleCategory", "coach" }, allowSetters = true)
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coach id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoachType getType() {
        return this.type;
    }

    public Coach type(CoachType type) {
        this.setType(type);
        return this;
    }

    public void setType(CoachType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Coach name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Coach email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return this.website;
    }

    public Coach website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public Coach instagram(String instagram) {
        this.setInstagram(instagram);
        return this;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Coach phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return this.country;
    }

    public Coach country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSpeaks() {
        return this.speaks;
    }

    public Coach speaks(String speaks) {
        this.setSpeaks(speaks);
        return this;
    }

    public void setSpeaks(String speaks) {
        this.speaks = speaks;
    }

    public String getResume() {
        return this.resume;
    }

    public Coach resume(String resume) {
        this.setResume(resume);
        return this;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getNotes() {
        return this.notes;
    }

    public Coach notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Coach created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public Coach updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setCoach(null));
        }
        if (videos != null) {
            videos.forEach(i -> i.setCoach(this));
        }
        this.videos = videos;
    }

    public Coach videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public Coach addVideo(Video video) {
        this.videos.add(video);
        video.setCoach(this);
        return this;
    }

    public Coach removeVideo(Video video) {
        this.videos.remove(video);
        video.setCoach(null);
        return this;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setCoach(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setCoach(this));
        }
        this.articles = articles;
    }

    public Coach articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public Coach addArticle(Article article) {
        this.articles.add(article);
        article.setCoach(this);
        return this;
    }

    public Coach removeArticle(Article article) {
        this.articles.remove(article);
        article.setCoach(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coach)) {
            return false;
        }
        return getId() != null && getId().equals(((Coach) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coach{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            ", instagram='" + getInstagram() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", country='" + getCountry() + "'" +
            ", speaks='" + getSpeaks() + "'" +
            ", resume='" + getResume() + "'" +
            ", notes='" + getNotes() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
