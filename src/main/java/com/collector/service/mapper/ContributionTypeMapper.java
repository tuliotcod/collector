package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ContributionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContributionType and its DTO ContributionTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContributionTypeMapper extends EntityMapper<ContributionTypeDTO, ContributionType> {



    default ContributionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContributionType contributionType = new ContributionType();
        contributionType.setId(id);
        return contributionType;
    }
}
