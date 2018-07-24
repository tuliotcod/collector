package com.collector.service;

import com.collector.service.dto.IssueStatusDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing IssueStatus.
 */
public interface IssueStatusService {

    /**
     * Save a issueStatus.
     *
     * @param issueStatusDTO the entity to save
     * @return the persisted entity
     */
    IssueStatusDTO save(IssueStatusDTO issueStatusDTO);

    /**
     * Get all the issueStatuses.
     *
     * @return the list of entities
     */
    List<IssueStatusDTO> findAll();


    /**
     * Get the "id" issueStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IssueStatusDTO> findOne(Long id);

    /**
     * Delete the "id" issueStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issueStatus corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<IssueStatusDTO> search(String query);
}
