package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.CollectorUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CollectorUser and its DTO CollectorUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CollectorUserMapper extends EntityMapper<CollectorUserDTO, CollectorUser> {



    default CollectorUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        CollectorUser collectorUser = new CollectorUser();
        collectorUser.setId(id);
        return collectorUser;
    }
}
