package com.collector.service.impl;

import com.collector.service.WishlistService;
import com.collector.domain.Wishlist;
import com.collector.repository.WishlistRepository;
import com.collector.repository.search.WishlistSearchRepository;
import com.collector.service.dto.WishlistDTO;
import com.collector.service.mapper.WishlistMapper;
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
 * Service Implementation for managing Wishlist.
 */
@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final Logger log = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistRepository wishlistRepository;

    private final WishlistMapper wishlistMapper;

    private final WishlistSearchRepository wishlistSearchRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, WishlistMapper wishlistMapper, WishlistSearchRepository wishlistSearchRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
        this.wishlistSearchRepository = wishlistSearchRepository;
    }

    /**
     * Save a wishlist.
     *
     * @param wishlistDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WishlistDTO save(WishlistDTO wishlistDTO) {
        log.debug("Request to save Wishlist : {}", wishlistDTO);
        Wishlist wishlist = wishlistMapper.toEntity(wishlistDTO);
        wishlist = wishlistRepository.save(wishlist);
        WishlistDTO result = wishlistMapper.toDto(wishlist);
        wishlistSearchRepository.save(wishlist);
        return result;
    }

    /**
     * Get all the wishlists.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WishlistDTO> findAll() {
        log.debug("Request to get all Wishlists");
        return wishlistRepository.findAll().stream()
            .map(wishlistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one wishlist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WishlistDTO> findOne(Long id) {
        log.debug("Request to get Wishlist : {}", id);
        return wishlistRepository.findById(id)
            .map(wishlistMapper::toDto);
    }

    /**
     * Delete the wishlist by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wishlist : {}", id);
        wishlistRepository.deleteById(id);
        wishlistSearchRepository.deleteById(id);
    }

    /**
     * Search for the wishlist corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WishlistDTO> search(String query) {
        log.debug("Request to search Wishlists for query {}", query);
        return StreamSupport
            .stream(wishlistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(wishlistMapper::toDto)
            .collect(Collectors.toList());
    }
}
