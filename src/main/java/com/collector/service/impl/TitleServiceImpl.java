package com.collector.service.impl;

import com.collector.service.TitleService;
import com.collector.domain.Title;
import com.collector.repository.TitleRepository;
import com.collector.repository.search.TitleSearchRepository;
import com.collector.service.dto.TitleDTO;
import com.collector.service.mapper.TitleMapper;
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
 * Service Implementation for managing Title.
 */
@Service
@Transactional
public class TitleServiceImpl implements TitleService {

    private final Logger log = LoggerFactory.getLogger(TitleServiceImpl.class);

    private final TitleRepository titleRepository;

    private final TitleMapper titleMapper;

    private final TitleSearchRepository titleSearchRepository;

    public TitleServiceImpl(TitleRepository titleRepository, TitleMapper titleMapper, TitleSearchRepository titleSearchRepository) {
        this.titleRepository = titleRepository;
        this.titleMapper = titleMapper;
        this.titleSearchRepository = titleSearchRepository;
    }

    /**
     * Save a title.
     *
     * @param titleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TitleDTO save(TitleDTO titleDTO) {
        log.debug("Request to save Title : {}", titleDTO);
        Title title = titleMapper.toEntity(titleDTO);
        title = titleRepository.save(title);
        TitleDTO result = titleMapper.toDto(title);
        titleSearchRepository.save(title);
        return result;
    }

    /**
     * Get all the titles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TitleDTO> findAll() {
        log.debug("Request to get all Titles");
        return titleRepository.findAll().stream()
            .map(titleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one title by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TitleDTO> findOne(Long id) {
        log.debug("Request to get Title : {}", id);
        return titleRepository.findById(id)
            .map(titleMapper::toDto);
    }

    /**
     * Delete the title by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Title : {}", id);
        titleRepository.deleteById(id);
        titleSearchRepository.deleteById(id);
    }

    /**
     * Search for the title corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TitleDTO> search(String query) {
        log.debug("Request to search Titles for query {}", query);
        return StreamSupport
            .stream(titleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(titleMapper::toDto)
            .collect(Collectors.toList());
    }
}
