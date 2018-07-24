package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.WishlistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Wishlist and its DTO WishlistDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WishlistMapper extends EntityMapper<WishlistDTO, Wishlist> {


    @Mapping(target = "issues", ignore = true)
    Wishlist toEntity(WishlistDTO wishlistDTO);

    default Wishlist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setId(id);
        return wishlist;
    }
}
