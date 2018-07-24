package com.collector.service;

import com.collector.service.dto.CollaboratorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Collaborator.
 */
public interface CollaboratorService {

    /**
     * Save a collaborator.
     *
     * @param collaboratorDTO the entity to save
     * @return the persisted entity
     */
    CollaboratorDTO save(CollaboratorDTO collaboratorDTO);

    /**
     * Get all the collaborators.
     *
     * @return the list of entities
     */
    List<CollaboratorDTO> findAll();


    /**
     * Get the "id" collaborator.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CollaboratorDTO> findOne(Long id);

    /**
     * Delete the "id" collaborator.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the collaborator corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CollaboratorDTO> search(String query);
}
