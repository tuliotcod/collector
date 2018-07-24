package com.collector.service.impl;

import com.collector.service.ContributionService;
import com.collector.domain.Contribution;
import com.collector.repository.ContributionRepository;
import com.collector.repository.search.ContributionSearchRepository;
import com.collector.service.dto.ContributionDTO;
import com.collector.service.mapper.ContributionMapper;
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
 * Service Implementation for managing Contribution.
 */
@Service
@Transactional
public class ContributionServiceImpl implements ContributionService {

    private final Logger log = LoggerFactory.getLogger(ContributionServiceImpl.class);

    private final ContributionRepository contributionRepository;

    private final ContributionMapper contributionMapper;

    private final ContributionSearchRepository contributionSearchRepository;

    public ContributionServiceImpl(ContributionRepository contributionRepository, ContributionMapper contributionMapper, ContributionSearchRepository contributionSearchRepository) {
        this.contributionRepository = contributionRepository;
        this.contributionMapper = contributionMapper;
        this.contributionSearchRepository = contributionSearchRepository;
    }

    /**
     * Save a contribution.
     *
     * @param contributionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributionDTO save(ContributionDTO contributionDTO) {
        log.debug("Request to save Contribution : {}", contributionDTO);
        Contribution contribution = contributionMapper.toEntity(contributionDTO);
        contribution = contributionRepository.save(contribution);
        ContributionDTO result = contributionMapper.toDto(contribution);
        contributionSearchRepository.save(contribution);
        return result;
    }

    /**
     * Get all the contributions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionDTO> findAll() {
        log.debug("Request to get all Contributions");
        return contributionRepository.findAll().stream()
            .map(contributionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one contribution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContributionDTO> findOne(Long id) {
        log.debug("Request to get Contribution : {}", id);
        return contributionRepository.findById(id)
            .map(contributionMapper::toDto);
    }

    /**
     * Delete the contribution by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contribution : {}", id);
        contributionRepository.deleteById(id);
        contributionSearchRepository.deleteById(id);
    }

    /**
     * Search for the contribution corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionDTO> search(String query) {
        log.debug("Request to search Contributions for query {}", query);
        return StreamSupport
            .stream(contributionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contributionMapper::toDto)
            .collect(Collectors.toList());
    }
}
