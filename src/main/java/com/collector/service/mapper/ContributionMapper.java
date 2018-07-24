package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.ContributionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contribution and its DTO ContributionDTO.
 */
@Mapper(componentModel = "spring", uses = {ContributionTypeMapper.class})
public interface ContributionMapper extends EntityMapper<ContributionDTO, Contribution> {

    @Mapping(source = "type.id", target = "typeId")
    ContributionDTO toDto(Contribution contribution);

    @Mapping(source = "typeId", target = "type")
    Contribution toEntity(ContributionDTO contributionDTO);

    default Contribution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contribution contribution = new Contribution();
        contribution.setId(id);
        return contribution;
    }
}
