package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_number")
    private Long number;

    @Column(name = "info")
    private String info;

    @Lob
    @Column(name = "cover")
    private byte[] cover;

    @Column(name = "cover_content_type")
    private String coverContentType;

    @Column(name = "month")
    private Integer month;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "day")
    private Integer day;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "same_format_all_issues")
    private Boolean sameFormatAllIssues;

    @Column(name = "cover_price", precision = 10, scale = 2)
    private BigDecimal coverPrice;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToOne
    @JoinColumn(unique = true)
    private Title title;

    @OneToOne
    @JoinColumn(unique = true)
    private CollectorUser coverCollectorUser;

    @OneToOne
    @JoinColumn(unique = true)
    private Format format;

    @OneToOne
    @JoinColumn(unique = true)
    private Finishing finishing;

    @OneToOne
    @JoinColumn(unique = true)
    private Currency currency;

    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToMany(mappedBy = "issue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Collaborator> collaborators = new HashSet<>();

    @OneToMany(mappedBy = "issue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<History> histories = new HashSet<>();

    @OneToMany(mappedBy = "issue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public Issue number(Long number) {
        this.number = number;
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getInfo() {
        return info;
    }

    public Issue info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getCover() {
        return cover;
    }

    public Issue cover(byte[] cover) {
        this.cover = cover;
        return this;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getCoverContentType() {
        return coverContentType;
    }

    public Issue coverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
        return this;
    }

    public void setCoverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
    }

    public Integer getMonth() {
        return month;
    }

    public Issue month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public Issue year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDay() {
        return day;
    }

    public Issue day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getPages() {
        return pages;
    }

    public Issue pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean isSameFormatAllIssues() {
        return sameFormatAllIssues;
    }

    public Issue sameFormatAllIssues(Boolean sameFormatAllIssues) {
        this.sameFormatAllIssues = sameFormatAllIssues;
        return this;
    }

    public void setSameFormatAllIssues(Boolean sameFormatAllIssues) {
        this.sameFormatAllIssues = sameFormatAllIssues;
    }

    public BigDecimal getCoverPrice() {
        return coverPrice;
    }

    public Issue coverPrice(BigDecimal coverPrice) {
        this.coverPrice = coverPrice;
        return this;
    }

    public void setCoverPrice(BigDecimal coverPrice) {
        this.coverPrice = coverPrice;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Issue creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public Issue lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Title getTitle() {
        return title;
    }

    public Issue title(Title title) {
        this.title = title;
        return this;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public CollectorUser getCoverCollectorUser() {
        return coverCollectorUser;
    }

    public Issue coverCollectorUser(CollectorUser collectorUser) {
        this.coverCollectorUser = collectorUser;
        return this;
    }

    public void setCoverCollectorUser(CollectorUser collectorUser) {
        this.coverCollectorUser = collectorUser;
    }

    public Format getFormat() {
        return format;
    }

    public Issue format(Format format) {
        this.format = format;
        return this;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Finishing getFinishing() {
        return finishing;
    }

    public Issue finishing(Finishing finishing) {
        this.finishing = finishing;
        return this;
    }

    public void setFinishing(Finishing finishing) {
        this.finishing = finishing;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Issue currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Country getCountry() {
        return country;
    }

    public Issue country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Collaborator> getCollaborators() {
        return collaborators;
    }

    public Issue collaborators(Set<Collaborator> collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    public Issue addCollaborators(Collaborator collaborator) {
        this.collaborators.add(collaborator);
        collaborator.setIssue(this);
        return this;
    }

    public Issue removeCollaborators(Collaborator collaborator) {
        this.collaborators.remove(collaborator);
        collaborator.setIssue(null);
        return this;
    }

    public void setCollaborators(Set<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public Issue histories(Set<History> histories) {
        this.histories = histories;
        return this;
    }

    public Issue addHistories(History history) {
        this.histories.add(history);
        history.setIssue(this);
        return this;
    }

    public Issue removeHistories(History history) {
        this.histories.remove(history);
        history.setIssue(null);
        return this;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Issue comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Issue addComments(Comment comment) {
        this.comments.add(comment);
        comment.setIssue(this);
        return this;
    }

    public Issue removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setIssue(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Issue issue = (Issue) o;
        if (issue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Issue{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", info='" + getInfo() + "'" +
            ", cover='" + getCover() + "'" +
            ", coverContentType='" + getCoverContentType() + "'" +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", day=" + getDay() +
            ", pages=" + getPages() +
            ", sameFormatAllIssues='" + isSameFormatAllIssues() + "'" +
            ", coverPrice=" + getCoverPrice() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
