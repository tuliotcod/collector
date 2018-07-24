package com.collector.service.impl;

import com.collector.service.CollectorUserService;
import com.collector.domain.CollectorUser;
import com.collector.repository.CollectorUserRepository;
import com.collector.repository.search.CollectorUserSearchRepository;
import com.collector.service.dto.CollectorUserDTO;
import com.collector.service.mapper.CollectorUserMapper;
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
 * Service Implementation for managing CollectorUser.
 */
@Service
@Transactional
public class CollectorUserServiceImpl implements CollectorUserService {

    private final Logger log = LoggerFactory.getLogger(CollectorUserServiceImpl.class);

    private final CollectorUserRepository collectorUserRepository;

    private final CollectorUserMapper collectorUserMapper;

    private final CollectorUserSearchRepository collectorUserSearchRepository;

    public CollectorUserServiceImpl(CollectorUserRepository collectorUserRepository, CollectorUserMapper collectorUserMapper, CollectorUserSearchRepository collectorUserSearchRepository) {
        this.collectorUserRepository = collectorUserRepository;
        this.collectorUserMapper = collectorUserMapper;
        this.collectorUserSearchRepository = collectorUserSearchRepository;
    }

    /**
     * Save a collectorUser.
     *
     * @param collectorUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CollectorUserDTO save(CollectorUserDTO collectorUserDTO) {
        log.debug("Request to save CollectorUser : {}", collectorUserDTO);
        CollectorUser collectorUser = collectorUserMapper.toEntity(collectorUserDTO);
        collectorUser = collectorUserRepository.save(collectorUser);
        CollectorUserDTO result = collectorUserMapper.toDto(collectorUser);
        collectorUserSearchRepository.save(collectorUser);
        return result;
    }

    /**
     * Get all the collectorUsers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollectorUserDTO> findAll() {
        log.debug("Request to get all CollectorUsers");
        return collectorUserRepository.findAll().stream()
            .map(collectorUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one collectorUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollectorUserDTO> findOne(Long id) {
        log.debug("Request to get CollectorUser : {}", id);
        return collectorUserRepository.findById(id)
            .map(collectorUserMapper::toDto);
    }

    /**
     * Delete the collectorUser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CollectorUser : {}", id);
        collectorUserRepository.deleteById(id);
        collectorUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the collectorUser corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollectorUserDTO> search(String query) {
        log.debug("Request to search CollectorUsers for query {}", query);
        return StreamSupport
            .stream(collectorUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(collectorUserMapper::toDto)
            .collect(Collectors.toList());
    }
}
