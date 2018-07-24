package com.collector.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Format entity.
 */
public class FormatDTO implements Serializable {

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

        FormatDTO formatDTO = (FormatDTO) o;
        if (formatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormatDTO{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
