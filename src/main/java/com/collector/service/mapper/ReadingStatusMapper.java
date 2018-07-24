package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ReadingStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReadingStatus and its DTO ReadingStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReadingStatusMapper extends EntityMapper<ReadingStatusDTO, ReadingStatus> {



    default ReadingStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReadingStatus readingStatus = new ReadingStatus();
        readingStatus.setId(id);
        return readingStatus;
    }
}
