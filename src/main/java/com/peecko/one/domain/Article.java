package com.peecko.one.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.peecko.one.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Article implements Serializable {

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

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "summary")
    private String summary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "tags")
    private String tags;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "content")
    private String content;

    @Column(name = "series_id")
    private Long seriesId;

    @Column(name = "chapter")
    private Integer chapter;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "released")
    private Instant released;

    @Column(name = "archived")
    private Instant archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "articles" }, allowSetters = true)
    private ArticleCategory articleCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "videos", "articles" }, allowSetters = true)
    private Coach coach;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Article code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public Article title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public Article subtitle(String subtitle) {
        this.setSubtitle(subtitle);
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSummary() {
        return this.summary;
    }

    public Article summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Article language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getTags() {
        return this.tags;
    }

    public Article tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Article duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public Article thumbnail(String thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAudioUrl() {
        return this.audioUrl;
    }

    public Article audioUrl(String audioUrl) {
        this.setAudioUrl(audioUrl);
        return this;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getContent() {
        return this.content;
    }

    public Article content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSeriesId() {
        return this.seriesId;
    }

    public Article seriesId(Long seriesId) {
        this.setSeriesId(seriesId);
        return this;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getChapter() {
        return this.chapter;
    }

    public Article chapter(Integer chapter) {
        this.setChapter(chapter);
        return this;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Article created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return this.updated;
    }

    public Article updated(Instant updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getReleased() {
        return this.released;
    }

    public Article released(Instant released) {
        this.setReleased(released);
        return this;
    }

    public void setReleased(Instant released) {
        this.released = released;
    }

    public Instant getArchived() {
        return this.archived;
    }

    public Article archived(Instant archived) {
        this.setArchived(archived);
        return this;
    }

    public void setArchived(Instant archived) {
        this.archived = archived;
    }

    public ArticleCategory getArticleCategory() {
        return this.articleCategory;
    }

    public void setArticleCategory(ArticleCategory articleCategory) {
        this.articleCategory = articleCategory;
    }

    public Article articleCategory(ArticleCategory articleCategory) {
        this.setArticleCategory(articleCategory);
        return this;
    }

    public Coach getCoach() {
        return this.coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Article coach(Coach coach) {
        this.setCoach(coach);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return getId() != null && getId().equals(((Article) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", subtitle='" + getSubtitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", language='" + getLanguage() + "'" +
            ", tags='" + getTags() + "'" +
            ", duration=" + getDuration() +
            ", thumbnail='" + getThumbnail() + "'" +
            ", audioUrl='" + getAudioUrl() + "'" +
            ", content='" + getContent() + "'" +
            ", seriesId=" + getSeriesId() +
            ", chapter=" + getChapter() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", released='" + getReleased() + "'" +
            ", archived='" + getArchived() + "'" +
            "}";
    }
}
