package com.collector.service.impl;

import com.collector.service.ReadingStatusService;
import com.collector.domain.ReadingStatus;
import com.collector.repository.ReadingStatusRepository;
import com.collector.repository.search.ReadingStatusSearchRepository;
import com.collector.service.dto.ReadingStatusDTO;
import com.collector.service.mapper.ReadingStatusMapper;
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
 * Service Implementation for managing ReadingStatus.
 */
@Service
@Transactional
public class ReadingStatusServiceImpl implements ReadingStatusService {

    private final Logger log = LoggerFactory.getLogger(ReadingStatusServiceImpl.class);

    private final ReadingStatusRepository readingStatusRepository;

    private final ReadingStatusMapper readingStatusMapper;

    private final ReadingStatusSearchRepository readingStatusSearchRepository;

    public ReadingStatusServiceImpl(ReadingStatusRepository readingStatusRepository, ReadingStatusMapper readingStatusMapper, ReadingStatusSearchRepository readingStatusSearchRepository) {
        this.readingStatusRepository = readingStatusRepository;
        this.readingStatusMapper = readingStatusMapper;
        this.readingStatusSearchRepository = readingStatusSearchRepository;
    }

    /**
     * Save a readingStatus.
     *
     * @param readingStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReadingStatusDTO save(ReadingStatusDTO readingStatusDTO) {
        log.debug("Request to save ReadingStatus : {}", readingStatusDTO);
        ReadingStatus readingStatus = readingStatusMapper.toEntity(readingStatusDTO);
        readingStatus = readingStatusRepository.save(readingStatus);
        ReadingStatusDTO result = readingStatusMapper.toDto(readingStatus);
        readingStatusSearchRepository.save(readingStatus);
        return result;
    }

    /**
     * Get all the readingStatuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReadingStatusDTO> findAll() {
        log.debug("Request to get all ReadingStatuses");
        return readingStatusRepository.findAll().stream()
            .map(readingStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one readingStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReadingStatusDTO> findOne(Long id) {
        log.debug("Request to get ReadingStatus : {}", id);
        return readingStatusRepository.findById(id)
            .map(readingStatusMapper::toDto);
    }

    /**
     * Delete the readingStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReadingStatus : {}", id);
        readingStatusRepository.deleteById(id);
        readingStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the readingStatus corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReadingStatusDTO> search(String query) {
        log.debug("Request to search ReadingStatuses for query {}", query);
        return StreamSupport
            .stream(readingStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(readingStatusMapper::toDto)
            .collect(Collectors.toList());
    }
}
