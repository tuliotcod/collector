package com.collector.service;

import com.collector.service.dto.ReadingStatusDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ReadingStatus.
 */
public interface ReadingStatusService {

    /**
     * Save a readingStatus.
     *
     * @param readingStatusDTO the entity to save
     * @return the persisted entity
     */
    ReadingStatusDTO save(ReadingStatusDTO readingStatusDTO);

    /**
     * Get all the readingStatuses.
     *
     * @return the list of entities
     */
    List<ReadingStatusDTO> findAll();


    /**
     * Get the "id" readingStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReadingStatusDTO> findOne(Long id);

    /**
     * Delete the "id" readingStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the readingStatus corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ReadingStatusDTO> search(String query);
}
