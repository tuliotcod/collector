package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.TeamService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.TeamDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    private final TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * POST  /teams : Create a new team.
     *
     * @param teamDTO the teamDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamDTO, or with status 400 (Bad Request) if the team has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teams")
    @Timed
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        if (teamDTO.getId() != null) {
            throw new BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teams : Updates an existing team.
     *
     * @param teamDTO the teamDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamDTO,
     * or with status 400 (Bad Request) if the teamDTO is not valid,
     * or with status 500 (Internal Server Error) if the teamDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teams")
    @Timed
    public ResponseEntity<TeamDTO> updateTeam(@Valid @RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to update Team : {}", teamDTO);
        if (teamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teams : get all the teams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teams in body
     */
    @GetMapping("/teams")
    @Timed
    public List<TeamDTO> getAllTeams() {
        log.debug("REST request to get all Teams");
        return teamService.findAll();
    }

    /**
     * GET  /teams/:id : get the "id" team.
     *
     * @param id the id of the teamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/teams/{id}")
    @Timed
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        Optional<TeamDTO> teamDTO = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamDTO);
    }

    /**
     * DELETE  /teams/:id : delete the "id" team.
     *
     * @param id the id of the teamDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/teams?query=:query : search for the team corresponding
     * to the query.
     *
     * @param query the query of the team search
     * @return the result of the search
     */
    @GetMapping("/_search/teams")
    @Timed
    public List<TeamDTO> searchTeams(@RequestParam String query) {
        log.debug("REST request to search Teams for query {}", query);
        return teamService.search(query);
    }

}
