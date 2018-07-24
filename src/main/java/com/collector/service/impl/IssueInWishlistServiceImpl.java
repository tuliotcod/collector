package com.collector.service.impl;

import com.collector.service.IssueInWishlistService;
import com.collector.domain.IssueInWishlist;
import com.collector.repository.IssueInWishlistRepository;
import com.collector.repository.search.IssueInWishlistSearchRepository;
import com.collector.service.dto.IssueInWishlistDTO;
import com.collector.service.mapper.IssueInWishlistMapper;
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
 * Service Implementation for managing IssueInWishlist.
 */
@Service
@Transactional
public class IssueInWishlistServiceImpl implements IssueInWishlistService {

    private final Logger log = LoggerFactory.getLogger(IssueInWishlistServiceImpl.class);

    private final IssueInWishlistRepository issueInWishlistRepository;

    private final IssueInWishlistMapper issueInWishlistMapper;

    private final IssueInWishlistSearchRepository issueInWishlistSearchRepository;

    public IssueInWishlistServiceImpl(IssueInWishlistRepository issueInWishlistRepository, IssueInWishlistMapper issueInWishlistMapper, IssueInWishlistSearchRepository issueInWishlistSearchRepository) {
        this.issueInWishlistRepository = issueInWishlistRepository;
        this.issueInWishlistMapper = issueInWishlistMapper;
        this.issueInWishlistSearchRepository = issueInWishlistSearchRepository;
    }

    /**
     * Save a issueInWishlist.
     *
     * @param issueInWishlistDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IssueInWishlistDTO save(IssueInWishlistDTO issueInWishlistDTO) {
        log.debug("Request to save IssueInWishlist : {}", issueInWishlistDTO);
        IssueInWishlist issueInWishlist = issueInWishlistMapper.toEntity(issueInWishlistDTO);
        issueInWishlist = issueInWishlistRepository.save(issueInWishlist);
        IssueInWishlistDTO result = issueInWishlistMapper.toDto(issueInWishlist);
        issueInWishlistSearchRepository.save(issueInWishlist);
        return result;
    }

    /**
     * Get all the issueInWishlists.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueInWishlistDTO> findAll() {
        log.debug("Request to get all IssueInWishlists");
        return issueInWishlistRepository.findAll().stream()
            .map(issueInWishlistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one issueInWishlist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IssueInWishlistDTO> findOne(Long id) {
        log.debug("Request to get IssueInWishlist : {}", id);
        return issueInWishlistRepository.findById(id)
            .map(issueInWishlistMapper::toDto);
    }

    /**
     * Delete the issueInWishlist by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssueInWishlist : {}", id);
        issueInWishlistRepository.deleteById(id);
        issueInWishlistSearchRepository.deleteById(id);
    }

    /**
     * Search for the issueInWishlist corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IssueInWishlistDTO> search(String query) {
        log.debug("Request to search IssueInWishlists for query {}", query);
        return StreamSupport
            .stream(issueInWishlistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(issueInWishlistMapper::toDto)
            .collect(Collectors.toList());
    }
}
