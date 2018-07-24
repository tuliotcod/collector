package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.TeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Team and its DTO TeamDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class, LicensorMapper.class})
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "licensor.id", target = "licensorId")
    TeamDTO toDto(Team team);

    @Mapping(source = "countryId", target = "country")
    @Mapping(source = "licensorId", target = "licensor")
    Team toEntity(TeamDTO teamDTO);

    default Team fromId(Long id) {
        if (id == null) {
            return null;
        }
        Team team = new Team();
        team.setId(id);
        return team;
    }
}
