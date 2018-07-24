package com.collector.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the State entity.
 */
public class StateDTO implements Serializable {

    private Long id;

    private String name;

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

        StateDTO stateDTO = (StateDTO) o;
        if (stateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country=" + getCountryId() +
            "}";
    }
}
