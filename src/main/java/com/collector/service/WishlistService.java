package com.collector.service;

import com.collector.service.dto.WishlistDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Wishlist.
 */
public interface WishlistService {

    /**
     * Save a wishlist.
     *
     * @param wishlistDTO the entity to save
     * @return the persisted entity
     */
    WishlistDTO save(WishlistDTO wishlistDTO);

    /**
     * Get all the wishlists.
     *
     * @return the list of entities
     */
    List<WishlistDTO> findAll();


    /**
     * Get the "id" wishlist.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WishlistDTO> findOne(Long id);

    /**
     * Delete the "id" wishlist.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the wishlist corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<WishlistDTO> search(String query);
}
