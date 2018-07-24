package com.collector.service.impl;

import com.collector.service.CollaboratorService;
import com.collector.domain.Collaborator;
import com.collector.repository.CollaboratorRepository;
import com.collector.repository.search.CollaboratorSearchRepository;
import com.collector.service.dto.CollaboratorDTO;
import com.collector.service.mapper.CollaboratorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Collaborator.
 */
@Service
@Transactional
public class CollaboratorServiceImpl implements CollaboratorService {

    private final Logger log = LoggerFactory.getLogger(CollaboratorServiceImpl.class);

    private final CollaboratorRepository collaboratorRepository;

    private final CollaboratorMapper collaboratorMapper;

    private final CollaboratorSearchRepository collaboratorSearchRepository;

    public CollaboratorServiceImpl(CollaboratorRepository collaboratorRepository, CollaboratorMapper collaboratorMapper, CollaboratorSearchRepository collaboratorSearchRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.collaboratorMapper = collaboratorMapper;
        this.collaboratorSearchRepository = collaboratorSearchRepository;
    }

    /**
     * Save a collaborator.
     *
     * @param collaboratorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CollaboratorDTO save(CollaboratorDTO collaboratorDTO) {
        log.debug("Request to save Collaborator : {}", collaboratorDTO);
        Collaborator collaborator = collaboratorMapper.toEntity(collaboratorDTO);
        collaborator = collaboratorRepository.save(collaborator);
        CollaboratorDTO result = collaboratorMapper.toDto(collaborator);
        collaboratorSearchRepository.save(collaborator);
        return result;
    }

    /**
     * Get all the collaborators.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollaboratorDTO> findAll() {
        log.debug("Request to get all Collaborators");
        return collaboratorRepository.findAll().stream()
            .map(collaboratorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one collaborator by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollaboratorDTO> findOne(Long id) {
        log.debug("Request to get Collaborator : {}", id);
        return collaboratorRepository.findById(id)
            .map(collaboratorMapper::toDto);
    }

    /**
     * Delete the collaborator by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collaborator : {}", id);
        collaboratorRepository.deleteById(id);
        collaboratorSearchRepository.deleteById(id);
    }

    /**
     * Search for the collaborator corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollaboratorDTO> search(String query) {
        log.debug("Request to search Collaborators for query {}", query);
        return StreamSupport
            .stream(collaboratorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(collaboratorMapper::toDto)
            .collect(Collectors.toList());
    }
}
