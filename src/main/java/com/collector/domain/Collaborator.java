package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Collaborator.
 */
@Entity
@Table(name = "collaborator")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "collaborator")
public class Collaborator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne
    @JsonIgnoreProperties("collaborators")
    private Issue issue;

    @ManyToOne
    @JsonIgnoreProperties("collaborators")
    private History history;

    @OneToOne
    @JoinColumn(unique = true)
    private Artist artist;

    @OneToOne
    @JoinColumn(unique = true)
    private Role function;

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

    public Collaborator creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public Collaborator lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Issue getIssue() {
        return issue;
    }

    public Collaborator issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public History getHistory() {
        return history;
    }

    public Collaborator history(History history) {
        this.history = history;
        return this;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public Artist getArtist() {
        return artist;
    }

    public Collaborator artist(Artist artist) {
        this.artist = artist;
        return this;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Role getFunction() {
        return function;
    }

    public Collaborator function(Role role) {
        this.function = role;
        return this;
    }

    public void setFunction(Role role) {
        this.function = role;
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
        Collaborator collaborator = (Collaborator) o;
        if (collaborator.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collaborator.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collaborator{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
