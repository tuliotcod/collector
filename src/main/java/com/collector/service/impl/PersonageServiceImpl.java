package com.collector.service.impl;

import com.collector.service.PersonageService;
import com.collector.domain.Personage;
import com.collector.repository.PersonageRepository;
import com.collector.repository.search.PersonageSearchRepository;
import com.collector.service.dto.PersonageDTO;
import com.collector.service.mapper.PersonageMapper;
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
 * Service Implementation for managing Personage.
 */
@Service
@Transactional
public class PersonageServiceImpl implements PersonageService {

    private final Logger log = LoggerFactory.getLogger(PersonageServiceImpl.class);

    private final PersonageRepository personageRepository;

    private final PersonageMapper personageMapper;

    private final PersonageSearchRepository personageSearchRepository;

    public PersonageServiceImpl(PersonageRepository personageRepository, PersonageMapper personageMapper, PersonageSearchRepository personageSearchRepository) {
        this.personageRepository = personageRepository;
        this.personageMapper = personageMapper;
        this.personageSearchRepository = personageSearchRepository;
    }

    /**
     * Save a personage.
     *
     * @param personageDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PersonageDTO save(PersonageDTO personageDTO) {
        log.debug("Request to save Personage : {}", personageDTO);
        Personage personage = personageMapper.toEntity(personageDTO);
        personage = personageRepository.save(personage);
        PersonageDTO result = personageMapper.toDto(personage);
        personageSearchRepository.save(personage);
        return result;
    }

    /**
     * Get all the personages.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonageDTO> findAll() {
        log.debug("Request to get all Personages");
        return personageRepository.findAll().stream()
            .map(personageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one personage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PersonageDTO> findOne(Long id) {
        log.debug("Request to get Personage : {}", id);
        return personageRepository.findById(id)
            .map(personageMapper::toDto);
    }

    /**
     * Delete the personage by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personage : {}", id);
        personageRepository.deleteById(id);
        personageSearchRepository.deleteById(id);
    }

    /**
     * Search for the personage corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonageDTO> search(String query) {
        log.debug("Request to search Personages for query {}", query);
        return StreamSupport
            .stream(personageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(personageMapper::toDto)
            .collect(Collectors.toList());
    }
}
