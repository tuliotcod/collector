package com.collector.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the History entity.
 */
public class HistoryDTO implements Serializable {

    private Long id;

    private Integer order;

    private String name;

    private Integer pages;

    @Size(max = 500)
    private String desc;

    private Integer part;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long issueId;

    private Long arcId;

    private Long originalIssueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
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

    public Long getArcId() {
        return arcId;
    }

    public void setArcId(Long arcId) {
        this.arcId = arcId;
    }

    public Long getOriginalIssueId() {
        return originalIssueId;
    }

    public void setOriginalIssueId(Long issueId) {
        this.originalIssueId = issueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoryDTO historyDTO = (HistoryDTO) o;
        if (historyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", name='" + getName() + "'" +
            ", pages=" + getPages() +
            ", desc='" + getDesc() + "'" +
            ", part=" + getPart() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", issue=" + getIssueId() +
            ", arc=" + getArcId() +
            ", originalIssue=" + getOriginalIssueId() +
            "}";
    }
}
