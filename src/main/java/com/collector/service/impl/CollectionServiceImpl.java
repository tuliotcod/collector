package com.collector.service.impl;

import com.collector.service.CollectionService;
import com.collector.domain.Collection;
import com.collector.repository.CollectionRepository;
import com.collector.repository.search.CollectionSearchRepository;
import com.collector.service.dto.CollectionDTO;
import com.collector.service.mapper.CollectionMapper;
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
 * Service Implementation for managing Collection.
 */
@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    private final CollectionSearchRepository collectionSearchRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository, CollectionMapper collectionMapper, CollectionSearchRepository collectionSearchRepository) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
        this.collectionSearchRepository = collectionSearchRepository;
    }

    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CollectionDTO save(CollectionDTO collectionDTO) {
        log.debug("Request to save Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        CollectionDTO result = collectionMapper.toDto(collection);
        collectionSearchRepository.save(collection);
        return result;
    }

    /**
     * Get all the collections.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> findAll() {
        log.debug("Request to get all Collections");
        return collectionRepository.findAll().stream()
            .map(collectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one collection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollectionDTO> findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        return collectionRepository.findById(id)
            .map(collectionMapper::toDto);
    }

    /**
     * Delete the collection by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.deleteById(id);
        collectionSearchRepository.deleteById(id);
    }

    /**
     * Search for the collection corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> search(String query) {
        log.debug("Request to search Collections for query {}", query);
        return StreamSupport
            .stream(collectionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(collectionMapper::toDto)
            .collect(Collectors.toList());
    }
}
