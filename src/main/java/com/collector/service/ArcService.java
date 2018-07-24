package com.collector.service;

import com.collector.service.dto.ArcDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Arc.
 */
public interface ArcService {

    /**
     * Save a arc.
     *
     * @param arcDTO the entity to save
     * @return the persisted entity
     */
    ArcDTO save(ArcDTO arcDTO);

    /**
     * Get all the arcs.
     *
     * @return the list of entities
     */
    List<ArcDTO> findAll();


    /**
     * Get the "id" arc.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ArcDTO> findOne(Long id);

    /**
     * Delete the "id" arc.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the arc corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ArcDTO> search(String query);
}
