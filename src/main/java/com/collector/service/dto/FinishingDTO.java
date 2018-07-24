package com.collector.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Finishing entity.
 */
public class FinishingDTO implements Serializable {

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

        FinishingDTO finishingDTO = (FinishingDTO) o;
        if (finishingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), finishingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FinishingDTO{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
