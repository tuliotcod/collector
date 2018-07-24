package com.collector.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the IssueStatus entity.
 */
public class IssueStatusDTO implements Serializable {

    private Long id;

    private String desc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueStatusDTO issueStatusDTO = (IssueStatusDTO) o;
        if (issueStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueStatusDTO{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
