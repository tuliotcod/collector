package com.collector.service.impl;

import com.collector.service.IssueStatusService;
import com.collector.domain.IssueStatus;
import com.collector.repository.IssueStatusRepository;
import com.collector.repository.search.IssueStatusSearchRepository;
import com.collector.service.dto.IssueStatusDTO;
import com.collector.service.mapper.IssueStatusMapper;
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
 * Service Implementation for managing IssueStatus.
 */
@Service
@Transactional
public class IssueStatusServiceImpl implements IssueStatusService {

    private final Logger log = LoggerFactory.getLogger(IssueStatusServiceImpl.class);

    private final IssueStatusRepository issueStatusRepository;

    private final IssueStatusMapper issueStatusMapper;

    private final IssueStatusSearchRepository issueStatusSearchRepository;

    public IssueStatusServiceImpl(IssueStatusRepository issueStatusRepository, IssueStatusMapper issueStatusMapper, IssueStatusSearchRepository issueStatusSearchRepository) {
        this.issueStatusRepository = issueStatusRepository;
        this.issueStatusMapper = issueStatusMapper;
        this.issueStatusSearchRepository = issueStatusSearchRepository;
    }

    /**
     * Save a issueStatus.
     *
     * @param issueStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IssueStatusDTO save(IssueStatusDTO issueStatusDTO) {
        log.debug("Request to save IssueStatus : {}", issueStatusDTO);
        IssueStatus issueStatus = issueStatusMapper.toEntity(issueStatusDTO);
        issueStatus = issueStatusRepository.save(issueStatus);
        IssueStatusDTO result = issueStatusMapper.toDto(issueStatus);
        issueStatusSearchRepository.save(issueStatus);
        return result;
    }

    /**
     * Get all the issueStatuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueStatusDTO> findAll() {
        log.debug("Request to get all IssueStatuses");
        return issueStatusRepository.findAll().stream()
            .map(issueStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one issueStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IssueStatusDTO> findOne(Long id) {
        log.debug("Request to get IssueStatus : {}", id);
        return issueStatusRepository.findById(id)
            .map(issueStatusMapper::toDto);
    }

    /**
     * Delete the issueStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssueStatus : {}", id);
        issueStatusRepository.deleteById(id);
        issueStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the issueStatus corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueStatusDTO> search(String query) {
        log.debug("Request to search IssueStatuses for query {}", query);
        return StreamSupport
            .stream(issueStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(issueStatusMapper::toDto)
            .collect(Collectors.toList());
    }
}
