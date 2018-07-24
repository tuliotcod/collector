package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.FormatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Format and its DTO FormatDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FormatMapper extends EntityMapper<FormatDTO, Format> {



    default Format fromId(Long id) {
        if (id == null) {
            return null;
        }
        Format format = new Format();
        format.setId(id);
        return format;
    }
}
