package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.IssueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Issue and its DTO IssueDTO.
 */
@Mapper(componentModel = "spring", uses = {TitleMapper.class, CollectorUserMapper.class, FormatMapper.class, FinishingMapper.class, CurrencyMapper.class, CountryMapper.class})
public interface IssueMapper extends EntityMapper<IssueDTO, Issue> {

    @Mapping(source = "title.id", target = "titleId")
    @Mapping(source = "coverCollectorUser.id", target = "coverCollectorUserId")
    @Mapping(source = "format.id", target = "formatId")
    @Mapping(source = "finishing.id", target = "finishingId")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "country.id", target = "countryId")
    IssueDTO toDto(Issue issue);

    @Mapping(source = "titleId", target = "title")
    @Mapping(source = "coverCollectorUserId", target = "coverCollectorUser")
    @Mapping(source = "formatId", target = "format")
    @Mapping(source = "finishingId", target = "finishing")
    @Mapping(source = "currencyId", target = "currency")
    @Mapping(source = "countryId", target = "country")
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "histories", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Issue toEntity(IssueDTO issueDTO);

    default Issue fromId(Long id) {
        if (id == null) {
            return null;
        }
        Issue issue = new Issue();
        issue.setId(id);
        return issue;
    }
}
