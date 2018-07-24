package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.StateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity State and its DTO StateDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface StateMapper extends EntityMapper<StateDTO, State> {

    @Mapping(source = "country.id", target = "countryId")
    StateDTO toDto(State state);

    @Mapping(source = "countryId", target = "country")
    State toEntity(StateDTO stateDTO);

    default State fromId(Long id) {
        if (id == null) {
            return null;
        }
        State state = new State();
        state.setId(id);
        return state;
    }
}
