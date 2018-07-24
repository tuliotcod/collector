package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.CollaboratorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Collaborator and its DTO CollaboratorDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, HistoryMapper.class, ArtistMapper.class, RoleMapper.class})
public interface CollaboratorMapper extends EntityMapper<CollaboratorDTO, Collaborator> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "history.id", target = "historyId")
    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "function.id", target = "functionId")
    CollaboratorDTO toDto(Collaborator collaborator);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(source = "historyId", target = "history")
    @Mapping(source = "artistId", target = "artist")
    @Mapping(source = "functionId", target = "function")
    Collaborator toEntity(CollaboratorDTO collaboratorDTO);

    default Collaborator fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collaborator collaborator = new Collaborator();
        collaborator.setId(id);
        return collaborator;
    }
}
