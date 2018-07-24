package com.collector.service;

import com.collector.service.dto.CollectionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Collection.
 */
public interface CollectionService {

    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save
     * @return the persisted entity
     */
    CollectionDTO save(CollectionDTO collectionDTO);

    /**
     * Get all the collections.
     *
     * @return the list of entities
     */
    List<CollectionDTO> findAll();


    /**
     * Get the "id" collection.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CollectionDTO> findOne(Long id);

    /**
     * Delete the "id" collection.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the collection corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CollectionDTO> search(String query);
}
