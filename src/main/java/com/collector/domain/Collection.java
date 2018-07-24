package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Collection.
 */
@Entity
@Table(name = "collection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "collection")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToMany(mappedBy = "collection")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IssueInCollection> issues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Collection creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public Collection lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<IssueInCollection> getIssues() {
        return issues;
    }

    public Collection issues(Set<IssueInCollection> issueInCollections) {
        this.issues = issueInCollections;
        return this;
    }

    public Collection addIssues(IssueInCollection issueInCollection) {
        this.issues.add(issueInCollection);
        issueInCollection.setCollection(this);
        return this;
    }

    public Collection removeIssues(IssueInCollection issueInCollection) {
        this.issues.remove(issueInCollection);
        issueInCollection.setCollection(null);
        return this;
    }

    public void setIssues(Set<IssueInCollection> issueInCollections) {
        this.issues = issueInCollections;
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
        Collection collection = (Collection) o;
        if (collection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collection{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
