package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.GenreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Genre and its DTO GenreDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {



    default Genre fromId(Long id) {
        if (id == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(id);
        return genre;
    }
}
