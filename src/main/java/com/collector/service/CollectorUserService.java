package com.collector.service;

import com.collector.service.dto.CollectorUserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CollectorUser.
 */
public interface CollectorUserService {

    /**
     * Save a collectorUser.
     *
     * @param collectorUserDTO the entity to save
     * @return the persisted entity
     */
    CollectorUserDTO save(CollectorUserDTO collectorUserDTO);

    /**
     * Get all the collectorUsers.
     *
     * @return the list of entities
     */
    List<CollectorUserDTO> findAll();


    /**
     * Get the "id" collectorUser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CollectorUserDTO> findOne(Long id);

    /**
     * Delete the "id" collectorUser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the collectorUser corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CollectorUserDTO> search(String query);
}
