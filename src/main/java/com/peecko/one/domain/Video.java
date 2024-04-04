package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.Intensity;
import com.peecko.one.domain.enumeration.Language;
import com.peecko.one.domain.enumeration.Player;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Video.
 */
@Entity
@Table(name = "video")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Video implements Serializable {

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
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "duration")
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "tags")
    private String tags;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "player", nullable = false)
    private Player player;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "url")
    private String url;

    @Column(name = "audience")
    private String audience;

    @Enumerated(EnumType.STRING)
    @Column(name = "intensity")
    private Intensity intensity;

    @Column(name = "filename")
    private String filename;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private Instant created;

    @Column(name = "released")
    private Instant released;

    @Column(name = "archived")
    private Instant archived;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "videos" }, allowSetters = true)
    private VideoCategory videoCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "videos", "articles" }, allowSetters = true)
    private Coach coach;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Video id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Video code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public Video title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Video duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Video language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getTags() {
        return this.tags;
    }

    public Video tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Video player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public Video thumbnail(String thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return this.url;
    }

    public Video url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudience() {
        return this.audience;
    }

    public Video audience(String audience) {
        this.setAudience(audience);
        return this;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Intensity getIntensity() {
        return this.intensity;
    }

    public Video intensity(Intensity intensity) {
        this.setIntensity(intensity);
        return this;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public String getFilename() {
        return this.filename;
    }

    public Video filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return this.description;
    }

    public Video description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Video created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getReleased() {
        return this.released;
    }

    public Video released(Instant released) {
        this.setReleased(released);
        return this;
    }

    public void setReleased(Instant released) {
        this.released = released;
    }

    public Instant getArchived() {
        return this.archived;
    }

    public Video archived(Instant archived) {
        this.setArchived(archived);
        return this;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    public VideoCategory getVideoCategory() {
        return this.videoCategory;
    }

    public void setVideoCategory(VideoCategory videoCategory) {
        this.videoCategory = videoCategory;
    }

    public Video videoCategory(VideoCategory videoCategory) {
        this.setVideoCategory(videoCategory);
        return this;
    }

    public Coach getCoach() {
        return this.coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Video coach(Coach coach) {
        this.setCoach(coach);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Video)) {
            return false;
        }
        return getId() != null && getId().equals(((Video) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Video{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", duration=" + getDuration() +
            ", language='" + getLanguage() + "'" +
            ", tags='" + getTags() + "'" +
            ", player='" + getPlayer() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", url='" + getUrl() + "'" +
            ", audience='" + getAudience() + "'" +
            ", intensity='" + getIntensity() + "'" +
            ", filename='" + getFilename() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            ", released='" + getReleased() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
