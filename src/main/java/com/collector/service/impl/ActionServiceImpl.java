package com.collector.service.impl;

import com.collector.service.ActionService;
import com.collector.domain.Action;
import com.collector.repository.ActionRepository;
import com.collector.repository.search.ActionSearchRepository;
import com.collector.service.dto.ActionDTO;
import com.collector.service.mapper.ActionMapper;
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
 * Service Implementation for managing Action.
 */
@Service
@Transactional
public class ActionServiceImpl implements ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionServiceImpl.class);

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    private final ActionSearchRepository actionSearchRepository;

    public ActionServiceImpl(ActionRepository actionRepository, ActionMapper actionMapper, ActionSearchRepository actionSearchRepository) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
        this.actionSearchRepository = actionSearchRepository;
    }

    /**
     * Save a action.
     *
     * @param actionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ActionDTO save(ActionDTO actionDTO) {
        log.debug("Request to save Action : {}", actionDTO);
        Action action = actionMapper.toEntity(actionDTO);
        action = actionRepository.save(action);
        ActionDTO result = actionMapper.toDto(action);
        actionSearchRepository.save(action);
        return result;
    }

    /**
     * Get all the actions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActionDTO> findAll() {
        log.debug("Request to get all Actions");
        return actionRepository.findAll().stream()
            .map(actionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one action by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ActionDTO> findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        return actionRepository.findById(id)
            .map(actionMapper::toDto);
    }

    /**
     * Delete the action by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.deleteById(id);
        actionSearchRepository.deleteById(id);
    }

    /**
     * Search for the action corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActionDTO> search(String query) {
        log.debug("Request to search Actions for query {}", query);
        return StreamSupport
            .stream(actionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(actionMapper::toDto)
            .collect(Collectors.toList());
    }
}
