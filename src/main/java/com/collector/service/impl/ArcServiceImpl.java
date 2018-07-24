package com.collector.service.impl;

import com.collector.service.ArcService;
import com.collector.domain.Arc;
import com.collector.repository.ArcRepository;
import com.collector.repository.search.ArcSearchRepository;
import com.collector.service.dto.ArcDTO;
import com.collector.service.mapper.ArcMapper;
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
 * Service Implementation for managing Arc.
 */
@Service
@Transactional
public class ArcServiceImpl implements ArcService {

    private final Logger log = LoggerFactory.getLogger(ArcServiceImpl.class);

    private final ArcRepository arcRepository;

    private final ArcMapper arcMapper;

    private final ArcSearchRepository arcSearchRepository;

    public ArcServiceImpl(ArcRepository arcRepository, ArcMapper arcMapper, ArcSearchRepository arcSearchRepository) {
        this.arcRepository = arcRepository;
        this.arcMapper = arcMapper;
        this.arcSearchRepository = arcSearchRepository;
    }

    /**
     * Save a arc.
     *
     * @param arcDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ArcDTO save(ArcDTO arcDTO) {
        log.debug("Request to save Arc : {}", arcDTO);
        Arc arc = arcMapper.toEntity(arcDTO);
        arc = arcRepository.save(arc);
        ArcDTO result = arcMapper.toDto(arc);
        arcSearchRepository.save(arc);
        return result;
    }

    /**
     * Get all the arcs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArcDTO> findAll() {
        log.debug("Request to get all Arcs");
        return arcRepository.findAll().stream()
            .map(arcMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one arc by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArcDTO> findOne(Long id) {
        log.debug("Request to get Arc : {}", id);
        return arcRepository.findById(id)
            .map(arcMapper::toDto);
    }

    /**
     * Delete the arc by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Arc : {}", id);
        arcRepository.deleteById(id);
        arcSearchRepository.deleteById(id);
    }

    /**
     * Search for the arc corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArcDTO> search(String query) {
        log.debug("Request to search Arcs for query {}", query);
        return StreamSupport
            .stream(arcSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(arcMapper::toDto)
            .collect(Collectors.toList());
    }
}
