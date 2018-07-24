package com.collector.service;

import com.collector.service.dto.PersonageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Personage.
 */
public interface PersonageService {

    /**
     * Save a personage.
     *
     * @param personageDTO the entity to save
     * @return the persisted entity
     */
    PersonageDTO save(PersonageDTO personageDTO);

    /**
     * Get all the personages.
     *
     * @return the list of entities
     */
    List<PersonageDTO> findAll();


    /**
     * Get the "id" personage.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PersonageDTO> findOne(Long id);

    /**
     * Delete the "id" personage.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the personage corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<PersonageDTO> search(String query);
}
