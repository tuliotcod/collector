package com.collector.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Personage.
 */
@Entity
@Table(name = "personage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "personage")
public class Personage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "code_name")
    private String codeName;

    @Column(name = "original_name")
    private String originalName;

    @Size(max = 5000)
    @Column(name = "bio", length = 5000)
    private String bio;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToOne
    @JoinColumn(unique = true)
    private Licensor licensor;

    @ManyToOne
    @JsonIgnoreProperties("characters")
    private History history;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Personage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public Personage lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCodeName() {
        return codeName;
    }

    public Personage codeName(String codeName) {
        this.codeName = codeName;
        return this;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public Personage originalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getBio() {
        return bio;
    }

    public Personage bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Personage creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public Personage lastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Country getCountry() {
        return country;
    }

    public Personage country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Licensor getLicensor() {
        return licensor;
    }

    public Personage licensor(Licensor licensor) {
        this.licensor = licensor;
        return this;
    }

    public void setLicensor(Licensor licensor) {
        this.licensor = licensor;
    }

    public History getHistory() {
        return history;
    }

    public Personage history(History history) {
        this.history = history;
        return this;
    }

    public void setHistory(History history) {
        this.history = history;
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
        Personage personage = (Personage) o;
        if (personage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Personage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", codeName='" + getCodeName() + "'" +
            ", originalName='" + getOriginalName() + "'" +
            ", bio='" + getBio() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
