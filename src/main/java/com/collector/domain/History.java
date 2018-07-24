package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A History.
 */
@Entity
@Table(name = "history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "name")
    private String name;

    @Column(name = "pages")
    private Integer pages;

    @Size(max = 500)
    @Column(name = "jhi_desc", length = 500)
    private String desc;

    @Column(name = "part")
    private Integer part;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne
    @JsonIgnoreProperties("histories")
    private Issue issue;

    @OneToOne
    @JoinColumn(unique = true)
    private Arc arc;

    @OneToOne
    @JoinColumn(unique = true)
    private Issue originalIssue;

    @OneToMany(mappedBy = "history")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Collaborator> collaborators = new HashSet<>();

    @OneToMany(mappedBy = "history")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Personage> characters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public History order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public History name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPages() {
        return pages;
    }

    public History pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getDesc() {
        return desc;
    }

    public History desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPart() {
        return part;
    }

    public History part(Integer part) {
        this.part = part;
        return this;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public History creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public History lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Issue getIssue() {
        return issue;
    }

    public History issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Arc getArc() {
        return arc;
    }

    public History arc(Arc arc) {
        this.arc = arc;
        return this;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }

    public Issue getOriginalIssue() {
        return originalIssue;
    }

    public History originalIssue(Issue issue) {
        this.originalIssue = issue;
        return this;
    }

    public void setOriginalIssue(Issue issue) {
        this.originalIssue = issue;
    }

    public Set<Collaborator> getCollaborators() {
        return collaborators;
    }

    public History collaborators(Set<Collaborator> collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    public History addCollaborators(Collaborator collaborator) {
        this.collaborators.add(collaborator);
        collaborator.setHistory(this);
        return this;
    }

    public History removeCollaborators(Collaborator collaborator) {
        this.collaborators.remove(collaborator);
        collaborator.setHistory(null);
        return this;
    }

    public void setCollaborators(Set<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public Set<Personage> getCharacters() {
        return characters;
    }

    public History characters(Set<Personage> personages) {
        this.characters = personages;
        return this;
    }

    public History addCharacters(Personage personage) {
        this.characters.add(personage);
        personage.setHistory(this);
        return this;
    }

    public History removeCharacters(Personage personage) {
        this.characters.remove(personage);
        personage.setHistory(null);
        return this;
    }

    public void setCharacters(Set<Personage> personages) {
        this.characters = personages;
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
        History history = (History) o;
        if (history.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), history.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "History{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", name='" + getName() + "'" +
            ", pages=" + getPages() +
            ", desc='" + getDesc() + "'" +
            ", part=" + getPart() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
