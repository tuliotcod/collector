package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.LicensorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Licensor and its DTO LicensorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LicensorMapper extends EntityMapper<LicensorDTO, Licensor> {



    default Licensor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Licensor licensor = new Licensor();
        licensor.setId(id);
        return licensor;
    }
}
