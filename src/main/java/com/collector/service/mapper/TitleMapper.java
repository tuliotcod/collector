package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.TitleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Title and its DTO TitleDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, GenreMapper.class, StatusMapper.class})
public interface TitleMapper extends EntityMapper<TitleDTO, Title> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "genre.id", target = "genreId")
    @Mapping(source = "status.id", target = "statusId")
    TitleDTO toDto(Title title);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "genreId", target = "genre")
    @Mapping(source = "statusId", target = "status")
    Title toEntity(TitleDTO titleDTO);

    default Title fromId(Long id) {
        if (id == null) {
            return null;
        }
        Title title = new Title();
        title.setId(id);
        return title;
    }
}
