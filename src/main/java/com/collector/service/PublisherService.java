package com.collector.service;

import com.collector.service.dto.PublisherDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Publisher.
 */
public interface PublisherService {

    /**
     * Save a publisher.
     *
     * @param publisherDTO the entity to save
     * @return the persisted entity
     */
    PublisherDTO save(PublisherDTO publisherDTO);

    /**
     * Get all the publishers.
     *
     * @return the list of entities
     */
    List<PublisherDTO> findAll();


    /**
     * Get the "id" publisher.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PublisherDTO> findOne(Long id);

    /**
     * Delete the "id" publisher.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the publisher corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<PublisherDTO> search(String query);
}
