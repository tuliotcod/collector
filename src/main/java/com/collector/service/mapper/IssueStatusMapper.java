package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.IssueStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IssueStatus and its DTO IssueStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IssueStatusMapper extends EntityMapper<IssueStatusDTO, IssueStatus> {



    default IssueStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        IssueStatus issueStatus = new IssueStatus();
        issueStatus.setId(id);
        return issueStatus;
    }
}
