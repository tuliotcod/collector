package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.PersonageService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.PersonageDTO;
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
 * REST controller for managing Personage.
 */
@RestController
@RequestMapping("/api")
public class PersonageResource {

    private final Logger log = LoggerFactory.getLogger(PersonageResource.class);

    private static final String ENTITY_NAME = "personage";

    private final PersonageService personageService;

    public PersonageResource(PersonageService personageService) {
        this.personageService = personageService;
    }

    /**
     * POST  /personages : Create a new personage.
     *
     * @param personageDTO the personageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personageDTO, or with status 400 (Bad Request) if the personage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personages")
    @Timed
    public ResponseEntity<PersonageDTO> createPersonage(@Valid @RequestBody PersonageDTO personageDTO) throws URISyntaxException {
        log.debug("REST request to save Personage : {}", personageDTO);
        if (personageDTO.getId() != null) {
            throw new BadRequestAlertException("A new personage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonageDTO result = personageService.save(personageDTO);
        return ResponseEntity.created(new URI("/api/personages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personages : Updates an existing personage.
     *
     * @param personageDTO the personageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personageDTO,
     * or with status 400 (Bad Request) if the personageDTO is not valid,
     * or with status 500 (Internal Server Error) if the personageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personages")
    @Timed
    public ResponseEntity<PersonageDTO> updatePersonage(@Valid @RequestBody PersonageDTO personageDTO) throws URISyntaxException {
        log.debug("REST request to update Personage : {}", personageDTO);
        if (personageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonageDTO result = personageService.save(personageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personages : get all the personages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personages in body
     */
    @GetMapping("/personages")
    @Timed
    public List<PersonageDTO> getAllPersonages() {
        log.debug("REST request to get all Personages");
        return personageService.findAll();
    }

    /**
     * GET  /personages/:id : get the "id" personage.
     *
     * @param id the id of the personageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/personages/{id}")
    @Timed
    public ResponseEntity<PersonageDTO> getPersonage(@PathVariable Long id) {
        log.debug("REST request to get Personage : {}", id);
        Optional<PersonageDTO> personageDTO = personageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personageDTO);
    }

    /**
     * DELETE  /personages/:id : delete the "id" personage.
     *
     * @param id the id of the personageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personages/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonage(@PathVariable Long id) {
        log.debug("REST request to delete Personage : {}", id);
        personageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/personages?query=:query : search for the personage corresponding
     * to the query.
     *
     * @param query the query of the personage search
     * @return the result of the search
     */
    @GetMapping("/_search/personages")
    @Timed
    public List<PersonageDTO> searchPersonages(@RequestParam String query) {
        log.debug("REST request to search Personages for query {}", query);
        return personageService.search(query);
    }

}
