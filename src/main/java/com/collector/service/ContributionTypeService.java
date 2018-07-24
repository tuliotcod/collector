package com.collector.service;

import com.collector.service.dto.ContributionTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ContributionType.
 */
public interface ContributionTypeService {

    /**
     * Save a contributionType.
     *
     * @param contributionTypeDTO the entity to save
     * @return the persisted entity
     */
    ContributionTypeDTO save(ContributionTypeDTO contributionTypeDTO);

    /**
     * Get all the contributionTypes.
     *
     * @return the list of entities
     */
    List<ContributionTypeDTO> findAll();


    /**
     * Get the "id" contributionType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContributionTypeDTO> findOne(Long id);

    /**
     * Delete the "id" contributionType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the contributionType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ContributionTypeDTO> search(String query);
}
