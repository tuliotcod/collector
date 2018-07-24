package com.collector.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Artist entity.
 */
public class ArtistDTO implements Serializable {

    private Long id;

    private String name;

    private String lastName;

    private String nickname;

    private Instant birthday;

    private Instant dateOfDeath;

    @Size(max = 5000)
    private String bio;

    @Lob
    private byte[] image;
    private String imageContentType;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long countryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public Instant getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Instant dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtistDTO artistDTO = (ArtistDTO) o;
        if (artistDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artistDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtistDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", dateOfDeath='" + getDateOfDeath() + "'" +
            ", bio='" + getBio() + "'" +
            ", image='" + getImage() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", country=" + getCountryId() +
            "}";
    }
}
