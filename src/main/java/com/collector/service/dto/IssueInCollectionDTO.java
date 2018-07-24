package com.collector.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the IssueInCollection entity.
 */
public class IssueInCollectionDTO implements Serializable {

    private Long id;

    private BigDecimal price;

    private Integer amount;

    @Size(max = 75)
    private String notes;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long issueId;

    private Long issueStatusId;

    private Long readingStatusId;

    private Long collectionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getIssueStatusId() {
        return issueStatusId;
    }

    public void setIssueStatusId(Long issueStatusId) {
        this.issueStatusId = issueStatusId;
    }

    public Long getReadingStatusId() {
        return readingStatusId;
    }

    public void setReadingStatusId(Long readingStatusId) {
        this.readingStatusId = readingStatusId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueInCollectionDTO issueInCollectionDTO = (IssueInCollectionDTO) o;
        if (issueInCollectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueInCollectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueInCollectionDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", amount=" + getAmount() +
            ", notes='" + getNotes() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", issue=" + getIssueId() +
            ", issueStatus=" + getIssueStatusId() +
            ", readingStatus=" + getReadingStatusId() +
            ", collection=" + getCollectionId() +
            "}";
    }
}
