package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VideoItem.
 */
@Entity
@Table(name = "video_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VideoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "previous_video_item_id")
    private VideoItem next;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "next_video_item_id")
    private VideoItem previous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "play_list_id", nullable = false)
    @JsonIgnoreProperties(value = { "videoItems", "apsUser" }, allowSetters = true)
    private PlayList playList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VideoItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VideoItem getPrevious() {
        return this.previous;
    }

    public VideoItem previous(VideoItem previous) {
        this.setPrevious(previous);
        return this;
    }

    public void setPrevious(VideoItem previous) {
        this.previous = previous;
    }

    public String getCode() {
        return this.code;
    }

    public VideoItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VideoItem getNext() {
        return this.next;
    }

    public VideoItem next(VideoItem next) {
        this.setNext(next);
        return this;
    }

    public void setNext(VideoItem next) {
        this.next = next;
    }

    public PlayList getPlayList() {
        return this.playList;
    }

    public void setPlayList(PlayList playList) {
        this.playList = playList;
    }

    public VideoItem playList(PlayList playList) {
        this.setPlayList(playList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VideoItem)) {
            return false;
        }
        return getId() != null && getId().equals(((VideoItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VideoItem{" +
            "id=" + getId() +
            ", previous='" + getPrevious() + "'" +
            ", code='" + getCode() + "'" +
            ", next='" + getNext() + "'" +
            "}";
    }
}
