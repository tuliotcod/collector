package com.collector.service;

import com.collector.service.dto.ContributionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Contribution.
 */
public interface ContributionService {

    /**
     * Save a contribution.
     *
     * @param contributionDTO the entity to save
     * @return the persisted entity
     */
    ContributionDTO save(ContributionDTO contributionDTO);

    /**
     * Get all the contributions.
     *
     * @return the list of entities
     */
    List<ContributionDTO> findAll();


    /**
     * Get the "id" contribution.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContributionDTO> findOne(Long id);

    /**
     * Delete the "id" contribution.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the contribution corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ContributionDTO> search(String query);
}
