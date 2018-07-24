package com.collector.service;

import com.collector.service.dto.IssueInCollectionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing IssueInCollection.
 */
public interface IssueInCollectionService {

    /**
     * Save a issueInCollection.
     *
     * @param issueInCollectionDTO the entity to save
     * @return the persisted entity
     */
    IssueInCollectionDTO save(IssueInCollectionDTO issueInCollectionDTO);

    /**
     * Get all the issueInCollections.
     *
     * @return the list of entities
     */
    List<IssueInCollectionDTO> findAll();


    /**
     * Get the "id" issueInCollection.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IssueInCollectionDTO> findOne(Long id);

    /**
     * Delete the "id" issueInCollection.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issueInCollection corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<IssueInCollectionDTO> search(String query);
}
