package com.collector.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Contribution entity.
 */
public class ContributionDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Long typeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long contributionTypeId) {
        this.typeId = contributionTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContributionDTO contributionDTO = (ContributionDTO) o;
        if (contributionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contributionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContributionDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", type=" + getTypeId() +
            "}";
    }
}
