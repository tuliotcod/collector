package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.PersonageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Personage and its DTO PersonageDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class, LicensorMapper.class, HistoryMapper.class})
public interface PersonageMapper extends EntityMapper<PersonageDTO, Personage> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "licensor.id", target = "licensorId")
    @Mapping(source = "history.id", target = "historyId")
    PersonageDTO toDto(Personage personage);

    @Mapping(source = "countryId", target = "country")
    @Mapping(source = "licensorId", target = "licensor")
    @Mapping(source = "historyId", target = "history")
    Personage toEntity(PersonageDTO personageDTO);

    default Personage fromId(Long id) {
        if (id == null) {
            return null;
        }
        Personage personage = new Personage();
        personage.setId(id);
        return personage;
    }
}
