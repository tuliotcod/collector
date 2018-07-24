package com.collector.service.impl;

import com.collector.service.FinishingService;
import com.collector.domain.Finishing;
import com.collector.repository.FinishingRepository;
import com.collector.repository.search.FinishingSearchRepository;
import com.collector.service.dto.FinishingDTO;
import com.collector.service.mapper.FinishingMapper;
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
 * Service Implementation for managing Finishing.
 */
@Service
@Transactional
public class FinishingServiceImpl implements FinishingService {

    private final Logger log = LoggerFactory.getLogger(FinishingServiceImpl.class);

    private final FinishingRepository finishingRepository;

    private final FinishingMapper finishingMapper;

    private final FinishingSearchRepository finishingSearchRepository;

    public FinishingServiceImpl(FinishingRepository finishingRepository, FinishingMapper finishingMapper, FinishingSearchRepository finishingSearchRepository) {
        this.finishingRepository = finishingRepository;
        this.finishingMapper = finishingMapper;
        this.finishingSearchRepository = finishingSearchRepository;
    }

    /**
     * Save a finishing.
     *
     * @param finishingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FinishingDTO save(FinishingDTO finishingDTO) {
        log.debug("Request to save Finishing : {}", finishingDTO);
        Finishing finishing = finishingMapper.toEntity(finishingDTO);
        finishing = finishingRepository.save(finishing);
        FinishingDTO result = finishingMapper.toDto(finishing);
        finishingSearchRepository.save(finishing);
        return result;
    }

    /**
     * Get all the finishings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FinishingDTO> findAll() {
        log.debug("Request to get all Finishings");
        return finishingRepository.findAll().stream()
            .map(finishingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one finishing by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FinishingDTO> findOne(Long id) {
        log.debug("Request to get Finishing : {}", id);
        return finishingRepository.findById(id)
            .map(finishingMapper::toDto);
    }

    /**
     * Delete the finishing by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Finishing : {}", id);
        finishingRepository.deleteById(id);
        finishingSearchRepository.deleteById(id);
    }

    /**
     * Search for the finishing corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FinishingDTO> search(String query) {
        log.debug("Request to search Finishings for query {}", query);
        return StreamSupport
            .stream(finishingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(finishingMapper::toDto)
            .collect(Collectors.toList());
    }
}
