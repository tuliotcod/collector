package com.collector.service;

import com.collector.service.dto.TitleDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Title.
 */
public interface TitleService {

    /**
     * Save a title.
     *
     * @param titleDTO the entity to save
     * @return the persisted entity
     */
    TitleDTO save(TitleDTO titleDTO);

    /**
     * Get all the titles.
     *
     * @return the list of entities
     */
    List<TitleDTO> findAll();


    /**
     * Get the "id" title.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TitleDTO> findOne(Long id);

    /**
     * Delete the "id" title.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the title corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<TitleDTO> search(String query);
}
