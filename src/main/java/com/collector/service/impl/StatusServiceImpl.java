package com.collector.service.impl;

import com.collector.service.StatusService;
import com.collector.domain.Status;
import com.collector.repository.StatusRepository;
import com.collector.repository.search.StatusSearchRepository;
import com.collector.service.dto.StatusDTO;
import com.collector.service.mapper.StatusMapper;
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
 * Service Implementation for managing Status.
 */
@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final StatusRepository statusRepository;

    private final StatusMapper statusMapper;

    private final StatusSearchRepository statusSearchRepository;

    public StatusServiceImpl(StatusRepository statusRepository, StatusMapper statusMapper, StatusSearchRepository statusSearchRepository) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
        this.statusSearchRepository = statusSearchRepository;
    }

    /**
     * Save a status.
     *
     * @param statusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StatusDTO save(StatusDTO statusDTO) {
        log.debug("Request to save Status : {}", statusDTO);
        Status status = statusMapper.toEntity(statusDTO);
        status = statusRepository.save(status);
        StatusDTO result = statusMapper.toDto(status);
        statusSearchRepository.save(status);
        return result;
    }

    /**
     * Get all the statuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StatusDTO> findAll() {
        log.debug("Request to get all Statuses");
        return statusRepository.findAll().stream()
            .map(statusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one status by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StatusDTO> findOne(Long id) {
        log.debug("Request to get Status : {}", id);
        return statusRepository.findById(id)
            .map(statusMapper::toDto);
    }

    /**
     * Delete the status by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Status : {}", id);
        statusRepository.deleteById(id);
        statusSearchRepository.deleteById(id);
    }

    /**
     * Search for the status corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StatusDTO> search(String query) {
        log.debug("Request to search Statuses for query {}", query);
        return StreamSupport
            .stream(statusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(statusMapper::toDto)
            .collect(Collectors.toList());
    }
}
