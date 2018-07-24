package com.collector.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Genre entity.
 */
public class GenreDTO implements Serializable {

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

        GenreDTO genreDTO = (GenreDTO) o;
        if (genreDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), genreDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GenreDTO{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
