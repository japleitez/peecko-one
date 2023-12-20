package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VideoCategory.
 */
@Entity
@Table(name = "video_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VideoCategory implements Serializable {

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

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "created")
    private Instant created;

    @Column(name = "released")
    private Instant released;

    @Column(name = "archived")
    private Instant archived;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "videoCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "videoCategory", "coach" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VideoCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public VideoCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public VideoCategory title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return this.label;
    }

    public VideoCategory label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getCreated() {
        return this.created;
    }

    public VideoCategory created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getReleased() {
        return this.released;
    }

    public VideoCategory released(Instant released) {
        this.setReleased(released);
        return this;
    }

    public void setReleased(Instant released) {
        this.released = released;
    }

    public Instant getArchived() {
        return this.archived;
    }

    public VideoCategory archived(Instant archived) {
        this.setArchived(archived);
        return this;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setVideoCategory(null));
        }
        if (videos != null) {
            videos.forEach(i -> i.setVideoCategory(this));
        }
        this.videos = videos;
    }

    public VideoCategory videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public VideoCategory addVideo(Video video) {
        this.videos.add(video);
        video.setVideoCategory(this);
        return this;
    }

    public VideoCategory removeVideo(Video video) {
        this.videos.remove(video);
        video.setVideoCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VideoCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((VideoCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VideoCategory{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", label='" + getLabel() + "'" +
            ", created='" + getCreated() + "'" +
            ", released='" + getReleased() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
