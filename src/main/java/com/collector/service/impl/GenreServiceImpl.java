package com.collector.service.impl;

import com.collector.service.GenreService;
import com.collector.domain.Genre;
import com.collector.repository.GenreRepository;
import com.collector.repository.search.GenreSearchRepository;
import com.collector.service.dto.GenreDTO;
import com.collector.service.mapper.GenreMapper;
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
 * Service Implementation for managing Genre.
 */
@Service
@Transactional
public class GenreServiceImpl implements GenreService {

    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    private final GenreSearchRepository genreSearchRepository;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper, GenreSearchRepository genreSearchRepository) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
        this.genreSearchRepository = genreSearchRepository;
    }

    /**
     * Save a genre.
     *
     * @param genreDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GenreDTO save(GenreDTO genreDTO) {
        log.debug("Request to save Genre : {}", genreDTO);
        Genre genre = genreMapper.toEntity(genreDTO);
        genre = genreRepository.save(genre);
        GenreDTO result = genreMapper.toDto(genre);
        genreSearchRepository.save(genre);
        return result;
    }

    /**
     * Get all the genres.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        log.debug("Request to get all Genres");
        return genreRepository.findAll().stream()
            .map(genreMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one genre by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GenreDTO> findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        return genreRepository.findById(id)
            .map(genreMapper::toDto);
    }

    /**
     * Delete the genre by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.deleteById(id);
        genreSearchRepository.deleteById(id);
    }

    /**
     * Search for the genre corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> search(String query) {
        log.debug("Request to search Genres for query {}", query);
        return StreamSupport
            .stream(genreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(genreMapper::toDto)
            .collect(Collectors.toList());
    }
}
