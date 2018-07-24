package com.collector.service;

import com.collector.service.dto.LicensorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Licensor.
 */
public interface LicensorService {

    /**
     * Save a licensor.
     *
     * @param licensorDTO the entity to save
     * @return the persisted entity
     */
    LicensorDTO save(LicensorDTO licensorDTO);

    /**
     * Get all the licensors.
     *
     * @return the list of entities
     */
    List<LicensorDTO> findAll();


    /**
     * Get the "id" licensor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LicensorDTO> findOne(Long id);

    /**
     * Delete the "id" licensor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the licensor corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<LicensorDTO> search(String query);
}
