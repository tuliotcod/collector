package com.collector.service;

import com.collector.service.dto.FormatDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Format.
 */
public interface FormatService {

    /**
     * Save a format.
     *
     * @param formatDTO the entity to save
     * @return the persisted entity
     */
    FormatDTO save(FormatDTO formatDTO);

    /**
     * Get all the formats.
     *
     * @return the list of entities
     */
    List<FormatDTO> findAll();


    /**
     * Get the "id" format.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FormatDTO> findOne(Long id);

    /**
     * Delete the "id" format.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the format corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<FormatDTO> search(String query);
}
