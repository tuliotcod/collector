package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ArcDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Arc and its DTO ArcDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArcMapper extends EntityMapper<ArcDTO, Arc> {



    default Arc fromId(Long id) {
        if (id == null) {
            return null;
        }
        Arc arc = new Arc();
        arc.setId(id);
        return arc;
    }
}
