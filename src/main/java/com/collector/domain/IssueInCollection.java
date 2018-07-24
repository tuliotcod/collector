package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A IssueInCollection.
 */
@Entity
@Table(name = "issue_in_collection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "issueincollection")
public class IssueInCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "amount")
    private Integer amount;

    @Size(max = 75)
    @Column(name = "notes", length = 75)
    private String notes;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToOne
    @JoinColumn(unique = true)
    private Issue issue;

    @OneToOne
    @JoinColumn(unique = true)
    private IssueStatus issueStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private ReadingStatus readingStatus;

    @ManyToOne
    @JsonIgnoreProperties("issues")
    private Collection collection;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public IssueInCollection price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public IssueInCollection amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public IssueInCollection notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public IssueInCollection creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public IssueInCollection lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Issue getIssue() {
        return issue;
    }

    public IssueInCollection issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public IssueInCollection issueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
        return this;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public ReadingStatus getReadingStatus() {
        return readingStatus;
    }

    public IssueInCollection readingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
        return this;
    }

    public void setReadingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
    }

    public Collection getCollection() {
        return collection;
    }

    public IssueInCollection collection(Collection collection) {
        this.collection = collection;
        return this;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
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
        IssueInCollection issueInCollection = (IssueInCollection) o;
        if (issueInCollection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueInCollection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueInCollection{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", amount=" + getAmount() +
            ", notes='" + getNotes() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
