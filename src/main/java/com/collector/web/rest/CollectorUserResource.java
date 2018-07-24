package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.CollectorUserService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.CollectorUserDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CollectorUser.
 */
@RestController
@RequestMapping("/api")
public class CollectorUserResource {

    private final Logger log = LoggerFactory.getLogger(CollectorUserResource.class);

    private static final String ENTITY_NAME = "collectorUser";

    private final CollectorUserService collectorUserService;

    public CollectorUserResource(CollectorUserService collectorUserService) {
        this.collectorUserService = collectorUserService;
    }

    /**
     * POST  /collector-users : Create a new collectorUser.
     *
     * @param collectorUserDTO the collectorUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectorUserDTO, or with status 400 (Bad Request) if the collectorUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/collector-users")
    @Timed
    public ResponseEntity<CollectorUserDTO> createCollectorUser(@RequestBody CollectorUserDTO collectorUserDTO) throws URISyntaxException {
        log.debug("REST request to save CollectorUser : {}", collectorUserDTO);
        if (collectorUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectorUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectorUserDTO result = collectorUserService.save(collectorUserDTO);
        return ResponseEntity.created(new URI("/api/collector-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collector-users : Updates an existing collectorUser.
     *
     * @param collectorUserDTO the collectorUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectorUserDTO,
     * or with status 400 (Bad Request) if the collectorUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the collectorUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/collector-users")
    @Timed
    public ResponseEntity<CollectorUserDTO> updateCollectorUser(@RequestBody CollectorUserDTO collectorUserDTO) throws URISyntaxException {
        log.debug("REST request to update CollectorUser : {}", collectorUserDTO);
        if (collectorUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CollectorUserDTO result = collectorUserService.save(collectorUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collectorUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collector-users : get all the collectorUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of collectorUsers in body
     */
    @GetMapping("/collector-users")
    @Timed
    public List<CollectorUserDTO> getAllCollectorUsers() {
        log.debug("REST request to get all CollectorUsers");
        return collectorUserService.findAll();
    }

    /**
     * GET  /collector-users/:id : get the "id" collectorUser.
     *
     * @param id the id of the collectorUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectorUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/collector-users/{id}")
    @Timed
    public ResponseEntity<CollectorUserDTO> getCollectorUser(@PathVariable Long id) {
        log.debug("REST request to get CollectorUser : {}", id);
        Optional<CollectorUserDTO> collectorUserDTO = collectorUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectorUserDTO);
    }

    /**
     * DELETE  /collector-users/:id : delete the "id" collectorUser.
     *
     * @param id the id of the collectorUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/collector-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCollectorUser(@PathVariable Long id) {
        log.debug("REST request to delete CollectorUser : {}", id);
        collectorUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/collector-users?query=:query : search for the collectorUser corresponding
     * to the query.
     *
     * @param query the query of the collectorUser search
     * @return the result of the search
     */
    @GetMapping("/_search/collector-users")
    @Timed
    public List<CollectorUserDTO> searchCollectorUsers(@RequestParam String query) {
        log.debug("REST request to search CollectorUsers for query {}", query);
        return collectorUserService.search(query);
    }

}
