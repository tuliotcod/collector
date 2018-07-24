package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.IssueInCollectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IssueInCollection and its DTO IssueInCollectionDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, IssueStatusMapper.class, ReadingStatusMapper.class, CollectionMapper.class})
public interface IssueInCollectionMapper extends EntityMapper<IssueInCollectionDTO, IssueInCollection> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "issueStatus.id", target = "issueStatusId")
    @Mapping(source = "readingStatus.id", target = "readingStatusId")
    @Mapping(source = "collection.id", target = "collectionId")
    IssueInCollectionDTO toDto(IssueInCollection issueInCollection);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(source = "issueStatusId", target = "issueStatus")
    @Mapping(source = "readingStatusId", target = "readingStatus")
    @Mapping(source = "collectionId", target = "collection")
    IssueInCollection toEntity(IssueInCollectionDTO issueInCollectionDTO);

    default IssueInCollection fromId(Long id) {
        if (id == null) {
            return null;
        }
        IssueInCollection issueInCollection = new IssueInCollection();
        issueInCollection.setId(id);
        return issueInCollection;
    }
}
