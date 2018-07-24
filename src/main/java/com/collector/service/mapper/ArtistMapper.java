package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ArtistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Artist and its DTO ArtistDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface ArtistMapper extends EntityMapper<ArtistDTO, Artist> {

    @Mapping(source = "country.id", target = "countryId")
    ArtistDTO toDto(Artist artist);

    @Mapping(source = "countryId", target = "country")
    Artist toEntity(ArtistDTO artistDTO);

    default Artist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Artist artist = new Artist();
        artist.setId(id);
        return artist;
    }
}
