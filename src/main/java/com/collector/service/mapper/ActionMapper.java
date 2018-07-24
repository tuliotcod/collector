package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ActionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Action and its DTO ActionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActionMapper extends EntityMapper<ActionDTO, Action> {



    default Action fromId(Long id) {
        if (id == null) {
            return null;
        }
        Action action = new Action();
        action.setId(id);
        return action;
    }
}
