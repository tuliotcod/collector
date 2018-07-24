package com.collector.service.impl;

import com.collector.service.ArtistService;
import com.collector.domain.Artist;
import com.collector.repository.ArtistRepository;
import com.collector.repository.search.ArtistSearchRepository;
import com.collector.service.dto.ArtistDTO;
import com.collector.service.mapper.ArtistMapper;
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
 * Service Implementation for managing Artist.
 */
@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final Logger log = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final ArtistRepository artistRepository;

    private final ArtistMapper artistMapper;

    private final ArtistSearchRepository artistSearchRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper, ArtistSearchRepository artistSearchRepository) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.artistSearchRepository = artistSearchRepository;
    }

    /**
     * Save a artist.
     *
     * @param artistDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ArtistDTO save(ArtistDTO artistDTO) {
        log.debug("Request to save Artist : {}", artistDTO);
        Artist artist = artistMapper.toEntity(artistDTO);
        artist = artistRepository.save(artist);
        ArtistDTO result = artistMapper.toDto(artist);
        artistSearchRepository.save(artist);
        return result;
    }

    /**
     * Get all the artists.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArtistDTO> findAll() {
        log.debug("Request to get all Artists");
        return artistRepository.findAll().stream()
            .map(artistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one artist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArtistDTO> findOne(Long id) {
        log.debug("Request to get Artist : {}", id);
        return artistRepository.findById(id)
            .map(artistMapper::toDto);
    }

    /**
     * Delete the artist by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Artist : {}", id);
        artistRepository.deleteById(id);
        artistSearchRepository.deleteById(id);
    }

    /**
     * Search for the artist corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArtistDTO> search(String query) {
        log.debug("Request to search Artists for query {}", query);
        return StreamSupport
            .stream(artistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(artistMapper::toDto)
            .collect(Collectors.toList());
    }
}
