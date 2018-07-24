package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.HistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity History and its DTO HistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, ArcMapper.class})
public interface HistoryMapper extends EntityMapper<HistoryDTO, History> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "arc.id", target = "arcId")
    @Mapping(source = "originalIssue.id", target = "originalIssueId")
    HistoryDTO toDto(History history);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(source = "arcId", target = "arc")
    @Mapping(source = "originalIssueId", target = "originalIssue")
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "characters", ignore = true)
    History toEntity(HistoryDTO historyDTO);

    default History fromId(Long id) {
        if (id == null) {
            return null;
        }
        History history = new History();
        history.setId(id);
        return history;
    }
}
