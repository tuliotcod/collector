package com.collector.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the IssueInWishlist entity.
 */
public class IssueInWishlistDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long wishlistId;

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

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueInWishlistDTO issueInWishlistDTO = (IssueInWishlistDTO) o;
        if (issueInWishlistDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueInWishlistDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueInWishlistDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", wishlist=" + getWishlistId() +
            "}";
    }
}
