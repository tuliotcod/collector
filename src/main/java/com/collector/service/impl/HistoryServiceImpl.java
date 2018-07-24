package com.collector.service.impl;

import com.collector.service.HistoryService;
import com.collector.domain.History;
import com.collector.repository.HistoryRepository;
import com.collector.repository.search.HistorySearchRepository;
import com.collector.service.dto.HistoryDTO;
import com.collector.service.mapper.HistoryMapper;
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
 * Service Implementation for managing History.
 */
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final HistoryRepository historyRepository;

    private final HistoryMapper historyMapper;

    private final HistorySearchRepository historySearchRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper, HistorySearchRepository historySearchRepository) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historySearchRepository = historySearchRepository;
    }

    /**
     * Save a history.
     *
     * @param historyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HistoryDTO save(HistoryDTO historyDTO) {
        log.debug("Request to save History : {}", historyDTO);
        History history = historyMapper.toEntity(historyDTO);
        history = historyRepository.save(history);
        HistoryDTO result = historyMapper.toDto(history);
        historySearchRepository.save(history);
        return result;
    }

    /**
     * Get all the histories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoryDTO> findAll() {
        log.debug("Request to get all Histories");
        return historyRepository.findAll().stream()
            .map(historyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one history by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HistoryDTO> findOne(Long id) {
        log.debug("Request to get History : {}", id);
        return historyRepository.findById(id)
            .map(historyMapper::toDto);
    }

    /**
     * Delete the history by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete History : {}", id);
        historyRepository.deleteById(id);
        historySearchRepository.deleteById(id);
    }

    /**
     * Search for the history corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoryDTO> search(String query) {
        log.debug("Request to search Histories for query {}", query);
        return StreamSupport
            .stream(historySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(historyMapper::toDto)
            .collect(Collectors.toList());
    }
}
