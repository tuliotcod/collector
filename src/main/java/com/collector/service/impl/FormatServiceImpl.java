package com.collector.service.impl;

import com.collector.service.FormatService;
import com.collector.domain.Format;
import com.collector.repository.FormatRepository;
import com.collector.repository.search.FormatSearchRepository;
import com.collector.service.dto.FormatDTO;
import com.collector.service.mapper.FormatMapper;
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
 * Service Implementation for managing Format.
 */
@Service
@Transactional
public class FormatServiceImpl implements FormatService {

    private final Logger log = LoggerFactory.getLogger(FormatServiceImpl.class);

    private final FormatRepository formatRepository;

    private final FormatMapper formatMapper;

    private final FormatSearchRepository formatSearchRepository;

    public FormatServiceImpl(FormatRepository formatRepository, FormatMapper formatMapper, FormatSearchRepository formatSearchRepository) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
        this.formatSearchRepository = formatSearchRepository;
    }

    /**
     * Save a format.
     *
     * @param formatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FormatDTO save(FormatDTO formatDTO) {
        log.debug("Request to save Format : {}", formatDTO);
        Format format = formatMapper.toEntity(formatDTO);
        format = formatRepository.save(format);
        FormatDTO result = formatMapper.toDto(format);
        formatSearchRepository.save(format);
        return result;
    }

    /**
     * Get all the formats.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormatDTO> findAll() {
        log.debug("Request to get all Formats");
        return formatRepository.findAll().stream()
            .map(formatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one format by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FormatDTO> findOne(Long id) {
        log.debug("Request to get Format : {}", id);
        return formatRepository.findById(id)
            .map(formatMapper::toDto);
    }

    /**
     * Delete the format by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Format : {}", id);
        formatRepository.deleteById(id);
        formatSearchRepository.deleteById(id);
    }

    /**
     * Search for the format corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormatDTO> search(String query) {
        log.debug("Request to search Formats for query {}", query);
        return StreamSupport
            .stream(formatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(formatMapper::toDto)
            .collect(Collectors.toList());
    }
}
