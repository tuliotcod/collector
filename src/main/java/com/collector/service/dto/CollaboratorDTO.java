package com.collector.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Collaborator entity.
 */
public class CollaboratorDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long issueId;

    private Long historyId;

    private Long artistId;

    private Long functionId;

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

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long roleId) {
        this.functionId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollaboratorDTO collaboratorDTO = (CollaboratorDTO) o;
        if (collaboratorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collaboratorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CollaboratorDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", issue=" + getIssueId() +
            ", history=" + getHistoryId() +
            ", artist=" + getArtistId() +
            ", function=" + getFunctionId() +
            "}";
    }
}
