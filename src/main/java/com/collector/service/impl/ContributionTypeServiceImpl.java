package com.collector.service.impl;

import com.collector.service.ContributionTypeService;
import com.collector.domain.ContributionType;
import com.collector.repository.ContributionTypeRepository;
import com.collector.repository.search.ContributionTypeSearchRepository;
import com.collector.service.dto.ContributionTypeDTO;
import com.collector.service.mapper.ContributionTypeMapper;
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
 * Service Implementation for managing ContributionType.
 */
@Service
@Transactional
public class ContributionTypeServiceImpl implements ContributionTypeService {

    private final Logger log = LoggerFactory.getLogger(ContributionTypeServiceImpl.class);

    private final ContributionTypeRepository contributionTypeRepository;

    private final ContributionTypeMapper contributionTypeMapper;

    private final ContributionTypeSearchRepository contributionTypeSearchRepository;

    public ContributionTypeServiceImpl(ContributionTypeRepository contributionTypeRepository, ContributionTypeMapper contributionTypeMapper, ContributionTypeSearchRepository contributionTypeSearchRepository) {
        this.contributionTypeRepository = contributionTypeRepository;
        this.contributionTypeMapper = contributionTypeMapper;
        this.contributionTypeSearchRepository = contributionTypeSearchRepository;
    }

    /**
     * Save a contributionType.
     *
     * @param contributionTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributionTypeDTO save(ContributionTypeDTO contributionTypeDTO) {
        log.debug("Request to save ContributionType : {}", contributionTypeDTO);
        ContributionType contributionType = contributionTypeMapper.toEntity(contributionTypeDTO);
        contributionType = contributionTypeRepository.save(contributionType);
        ContributionTypeDTO result = contributionTypeMapper.toDto(contributionType);
        contributionTypeSearchRepository.save(contributionType);
        return result;
    }

    /**
     * Get all the contributionTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionTypeDTO> findAll() {
        log.debug("Request to get all ContributionTypes");
        return contributionTypeRepository.findAll().stream()
            .map(contributionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one contributionType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContributionTypeDTO> findOne(Long id) {
        log.debug("Request to get ContributionType : {}", id);
        return contributionTypeRepository.findById(id)
            .map(contributionTypeMapper::toDto);
    }

    /**
     * Delete the contributionType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContributionType : {}", id);
        contributionTypeRepository.deleteById(id);
        contributionTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the contributionType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionTypeDTO> search(String query) {
        log.debug("Request to search ContributionTypes for query {}", query);
        return StreamSupport
            .stream(contributionTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contributionTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
