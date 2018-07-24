package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.IssueInWishlistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IssueInWishlist and its DTO IssueInWishlistDTO.
 */
@Mapper(componentModel = "spring", uses = {WishlistMapper.class})
public interface IssueInWishlistMapper extends EntityMapper<IssueInWishlistDTO, IssueInWishlist> {

    @Mapping(source = "wishlist.id", target = "wishlistId")
    IssueInWishlistDTO toDto(IssueInWishlist issueInWishlist);

    @Mapping(source = "wishlistId", target = "wishlist")
    IssueInWishlist toEntity(IssueInWishlistDTO issueInWishlistDTO);

    default IssueInWishlist fromId(Long id) {
        if (id == null) {
            return null;
        }
        IssueInWishlist issueInWishlist = new IssueInWishlist();
        issueInWishlist.setId(id);
        return issueInWishlist;
    }
}
