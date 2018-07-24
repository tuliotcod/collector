package com.collector.service.impl;

import com.collector.service.TeamService;
import com.collector.domain.Team;
import com.collector.repository.TeamRepository;
import com.collector.repository.search.TeamSearchRepository;
import com.collector.service.dto.TeamDTO;
import com.collector.service.mapper.TeamMapper;
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
 * Service Implementation for managing Team.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final TeamSearchRepository teamSearchRepository;

    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper, TeamSearchRepository teamSearchRepository) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.teamSearchRepository = teamSearchRepository;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        TeamDTO result = teamMapper.toDto(team);
        teamSearchRepository.save(team);
        return result;
    }

    /**
     * Get all the teams.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TeamDTO> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository.findAll().stream()
            .map(teamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one team by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TeamDTO> findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findById(id)
            .map(teamMapper::toDto);
    }

    /**
     * Delete the team by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
        teamSearchRepository.deleteById(id);
    }

    /**
     * Search for the team corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TeamDTO> search(String query) {
        log.debug("Request to search Teams for query {}", query);
        return StreamSupport
            .stream(teamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(teamMapper::toDto)
            .collect(Collectors.toList());
    }
}
