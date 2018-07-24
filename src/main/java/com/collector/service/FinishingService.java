package com.collector.service;

import com.collector.service.dto.FinishingDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Finishing.
 */
public interface FinishingService {

    /**
     * Save a finishing.
     *
     * @param finishingDTO the entity to save
     * @return the persisted entity
     */
    FinishingDTO save(FinishingDTO finishingDTO);

    /**
     * Get all the finishings.
     *
     * @return the list of entities
     */
    List<FinishingDTO> findAll();


    /**
     * Get the "id" finishing.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FinishingDTO> findOne(Long id);

    /**
     * Delete the "id" finishing.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the finishing corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<FinishingDTO> search(String query);
}
