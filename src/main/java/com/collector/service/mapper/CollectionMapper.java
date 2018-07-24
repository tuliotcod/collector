package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.CollectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Collection and its DTO CollectionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CollectionMapper extends EntityMapper<CollectionDTO, Collection> {


    @Mapping(target = "issues", ignore = true)
    Collection toEntity(CollectionDTO collectionDTO);

    default Collection fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collection collection = new Collection();
        collection.setId(id);
        return collection;
    }
}
