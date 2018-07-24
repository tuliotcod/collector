package com.collector.service.impl;

import com.collector.service.IssueInCollectionService;
import com.collector.domain.IssueInCollection;
import com.collector.repository.IssueInCollectionRepository;
import com.collector.repository.search.IssueInCollectionSearchRepository;
import com.collector.service.dto.IssueInCollectionDTO;
import com.collector.service.mapper.IssueInCollectionMapper;
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
 * Service Implementation for managing IssueInCollection.
 */
@Service
@Transactional
public class IssueInCollectionServiceImpl implements IssueInCollectionService {

    private final Logger log = LoggerFactory.getLogger(IssueInCollectionServiceImpl.class);

    private final IssueInCollectionRepository issueInCollectionRepository;

    private final IssueInCollectionMapper issueInCollectionMapper;

    private final IssueInCollectionSearchRepository issueInCollectionSearchRepository;

    public IssueInCollectionServiceImpl(IssueInCollectionRepository issueInCollectionRepository, IssueInCollectionMapper issueInCollectionMapper, IssueInCollectionSearchRepository issueInCollectionSearchRepository) {
        this.issueInCollectionRepository = issueInCollectionRepository;
        this.issueInCollectionMapper = issueInCollectionMapper;
        this.issueInCollectionSearchRepository = issueInCollectionSearchRepository;
    }

    /**
     * Save a issueInCollection.
     *
     * @param issueInCollectionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IssueInCollectionDTO save(IssueInCollectionDTO issueInCollectionDTO) {
        log.debug("Request to save IssueInCollection : {}", issueInCollectionDTO);
        IssueInCollection issueInCollection = issueInCollectionMapper.toEntity(issueInCollectionDTO);
        issueInCollection = issueInCollectionRepository.save(issueInCollection);
        IssueInCollectionDTO result = issueInCollectionMapper.toDto(issueInCollection);
        issueInCollectionSearchRepository.save(issueInCollection);
        return result;
    }

    /**
     * Get all the issueInCollections.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueInCollectionDTO> findAll() {
        log.debug("Request to get all IssueInCollections");
        return issueInCollectionRepository.findAll().stream()
            .map(issueInCollectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one issueInCollection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IssueInCollectionDTO> findOne(Long id) {
        log.debug("Request to get IssueInCollection : {}", id);
        return issueInCollectionRepository.findById(id)
            .map(issueInCollectionMapper::toDto);
    }

    /**
     * Delete the issueInCollection by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssueInCollection : {}", id);
        issueInCollectionRepository.deleteById(id);
        issueInCollectionSearchRepository.deleteById(id);
    }

    /**
     * Search for the issueInCollection corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueInCollectionDTO> search(String query) {
        log.debug("Request to search IssueInCollections for query {}", query);
        return StreamSupport
            .stream(issueInCollectionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(issueInCollectionMapper::toDto)
            .collect(Collectors.toList());
    }
}
