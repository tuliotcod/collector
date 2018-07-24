package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.FinishingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Finishing and its DTO FinishingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FinishingMapper extends EntityMapper<FinishingDTO, Finishing> {



    default Finishing fromId(Long id) {
        if (id == null) {
            return null;
        }
        Finishing finishing = new Finishing();
        finishing.setId(id);
        return finishing;
    }
}
