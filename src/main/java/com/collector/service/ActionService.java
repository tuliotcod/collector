package com.collector.service;

import com.collector.service.dto.ActionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Action.
 */
public interface ActionService {

    /**
     * Save a action.
     *
     * @param actionDTO the entity to save
     * @return the persisted entity
     */
    ActionDTO save(ActionDTO actionDTO);

    /**
     * Get all the actions.
     *
     * @return the list of entities
     */
    List<ActionDTO> findAll();


    /**
     * Get the "id" action.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ActionDTO> findOne(Long id);

    /**
     * Delete the "id" action.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the action corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ActionDTO> search(String query);
}
