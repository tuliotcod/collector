package com.collector.service.impl;

import com.collector.service.LicensorService;
import com.collector.domain.Licensor;
import com.collector.repository.LicensorRepository;
import com.collector.repository.search.LicensorSearchRepository;
import com.collector.service.dto.LicensorDTO;
import com.collector.service.mapper.LicensorMapper;
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
 * Service Implementation for managing Licensor.
 */
@Service
@Transactional
public class LicensorServiceImpl implements LicensorService {

    private final Logger log = LoggerFactory.getLogger(LicensorServiceImpl.class);

    private final LicensorRepository licensorRepository;

    private final LicensorMapper licensorMapper;

    private final LicensorSearchRepository licensorSearchRepository;

    public LicensorServiceImpl(LicensorRepository licensorRepository, LicensorMapper licensorMapper, LicensorSearchRepository licensorSearchRepository) {
        this.licensorRepository = licensorRepository;
        this.licensorMapper = licensorMapper;
        this.licensorSearchRepository = licensorSearchRepository;
    }

    /**
     * Save a licensor.
     *
     * @param licensorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LicensorDTO save(LicensorDTO licensorDTO) {
        log.debug("Request to save Licensor : {}", licensorDTO);
        Licensor licensor = licensorMapper.toEntity(licensorDTO);
        licensor = licensorRepository.save(licensor);
        LicensorDTO result = licensorMapper.toDto(licensor);
        licensorSearchRepository.save(licensor);
        return result;
    }

    /**
     * Get all the licensors.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LicensorDTO> findAll() {
        log.debug("Request to get all Licensors");
        return licensorRepository.findAll().stream()
            .map(licensorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one licensor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LicensorDTO> findOne(Long id) {
        log.debug("Request to get Licensor : {}", id);
        return licensorRepository.findById(id)
            .map(licensorMapper::toDto);
    }

    /**
     * Delete the licensor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Licensor : {}", id);
        licensorRepository.deleteById(id);
        licensorSearchRepository.deleteById(id);
    }

    /**
     * Search for the licensor corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LicensorDTO> search(String query) {
        log.debug("Request to search Licensors for query {}", query);
        return StreamSupport
            .stream(licensorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(licensorMapper::toDto)
            .collect(Collectors.toList());
    }
}
