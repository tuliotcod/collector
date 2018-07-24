package com.collector.service;

import com.collector.service.dto.HistoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing History.
 */
public interface HistoryService {

    /**
     * Save a history.
     *
     * @param historyDTO the entity to save
     * @return the persisted entity
     */
    HistoryDTO save(HistoryDTO historyDTO);

    /**
     * Get all the histories.
     *
     * @return the list of entities
     */
    List<HistoryDTO> findAll();


    /**
     * Get the "id" history.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HistoryDTO> findOne(Long id);

    /**
     * Delete the "id" history.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the history corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<HistoryDTO> search(String query);
}
