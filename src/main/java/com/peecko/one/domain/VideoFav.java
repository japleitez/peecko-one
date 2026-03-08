package com.peecko.one.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A VideoItem.
 */
@Entity
@Table(name = "video_fav")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VideoFav implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_fav_id_gen")
    @SequenceGenerator(name = "video_fav_id_gen", sequenceName = "video_fav_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "aps_user_id")
    private Long apsUserId;

    @Column(name = "video_id")
    private Long videoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApsUserId() {
        return apsUserId;
    }

    public void setApsUserId(Long apsUserId) {
        this.apsUserId = apsUserId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }
}
