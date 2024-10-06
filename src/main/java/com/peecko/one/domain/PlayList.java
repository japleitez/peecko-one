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
 * A PlayList.
 */
@Entity
@Table(name = "play_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "counter", nullable = false)
    private Integer counter;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "playList" }, allowSetters = true)
    private Set<VideoItem> videoItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "apsDevices", "playLists" }, allowSetters = true)
    private ApsUser apsUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PlayList name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCounter() {
        return this.counter;
    }

    public PlayList counter(Integer counter) {
        this.setCounter(counter);
        return this;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Instant getCreated() {
        return this.created;
    }

    public PlayList created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public PlayList updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Set<VideoItem> getVideoItems() {
        return this.videoItems;
    }

    public void setVideoItems(Set<VideoItem> videoItems) {
        if (this.videoItems != null) {
            this.videoItems.forEach(i -> i.setPlayList(null));
        }
        if (videoItems != null) {
            videoItems.forEach(i -> i.setPlayList(this));
        }
        this.videoItems = videoItems;
    }

    public PlayList videoItems(Set<VideoItem> videoItems) {
        this.setVideoItems(videoItems);
        return this;
    }

    public PlayList addVideoItem(VideoItem videoItem) {
        this.videoItems.add(videoItem);
        videoItem.setPlayList(this);
        return this;
    }

    public PlayList removeVideoItem(VideoItem videoItem) {
        this.videoItems.remove(videoItem);
        videoItem.setPlayList(null);
        return this;
    }

    public ApsUser getApsUser() {
        return this.apsUser;
    }

    public void setApsUser(ApsUser apsUser) {
        this.apsUser = apsUser;
    }

    public PlayList apsUser(ApsUser apsUser) {
        this.setApsUser(apsUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayList)) {
            return false;
        }
        return getId() != null && getId().equals(((PlayList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", counter=" + getCounter() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
