package com.collector.service;

import com.collector.service.dto.IssueInWishlistDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing IssueInWishlist.
 */
public interface IssueInWishlistService {

    /**
     * Save a issueInWishlist.
     *
     * @param issueInWishlistDTO the entity to save
     * @return the persisted entity
     */
    IssueInWishlistDTO save(IssueInWishlistDTO issueInWishlistDTO);

    /**
     * Get all the issueInWishlists.
     *
     * @return the list of entities
     */
    List<IssueInWishlistDTO> findAll();


    /**
     * Get the "id" issueInWishlist.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IssueInWishlistDTO> findOne(Long id);

    /**
     * Delete the "id" issueInWishlist.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issueInWishlist corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<IssueInWishlistDTO> search(String query);
}
