package com.collector.service;

import com.collector.service.dto.TeamDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Team.
 */
public interface TeamService {

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    TeamDTO save(TeamDTO teamDTO);

    /**
     * Get all the teams.
     *
     * @return the list of entities
     */
    List<TeamDTO> findAll();


    /**
     * Get the "id" team.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TeamDTO> findOne(Long id);

    /**
     * Delete the "id" team.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the team corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<TeamDTO> search(String query);
}
