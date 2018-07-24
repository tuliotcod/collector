package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.FinishingService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.FinishingDTO;
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
 * REST controller for managing Finishing.
 */
@RestController
@RequestMapping("/api")
public class FinishingResource {

    private final Logger log = LoggerFactory.getLogger(FinishingResource.class);

    private static final String ENTITY_NAME = "finishing";

    private final FinishingService finishingService;

    public FinishingResource(FinishingService finishingService) {
        this.finishingService = finishingService;
    }

    /**
     * POST  /finishings : Create a new finishing.
     *
     * @param finishingDTO the finishingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new finishingDTO, or with status 400 (Bad Request) if the finishing has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/finishings")
    @Timed
    public ResponseEntity<FinishingDTO> createFinishing(@RequestBody FinishingDTO finishingDTO) throws URISyntaxException {
        log.debug("REST request to save Finishing : {}", finishingDTO);
        if (finishingDTO.getId() != null) {
            throw new BadRequestAlertException("A new finishing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinishingDTO result = finishingService.save(finishingDTO);
        return ResponseEntity.created(new URI("/api/finishings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /finishings : Updates an existing finishing.
     *
     * @param finishingDTO the finishingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated finishingDTO,
     * or with status 400 (Bad Request) if the finishingDTO is not valid,
     * or with status 500 (Internal Server Error) if the finishingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/finishings")
    @Timed
    public ResponseEntity<FinishingDTO> updateFinishing(@RequestBody FinishingDTO finishingDTO) throws URISyntaxException {
        log.debug("REST request to update Finishing : {}", finishingDTO);
        if (finishingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FinishingDTO result = finishingService.save(finishingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, finishingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /finishings : get all the finishings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of finishings in body
     */
    @GetMapping("/finishings")
    @Timed
    public List<FinishingDTO> getAllFinishings() {
        log.debug("REST request to get all Finishings");
        return finishingService.findAll();
    }

    /**
     * GET  /finishings/:id : get the "id" finishing.
     *
     * @param id the id of the finishingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the finishingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/finishings/{id}")
    @Timed
    public ResponseEntity<FinishingDTO> getFinishing(@PathVariable Long id) {
        log.debug("REST request to get Finishing : {}", id);
        Optional<FinishingDTO> finishingDTO = finishingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(finishingDTO);
    }

    /**
     * DELETE  /finishings/:id : delete the "id" finishing.
     *
     * @param id the id of the finishingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/finishings/{id}")
    @Timed
    public ResponseEntity<Void> deleteFinishing(@PathVariable Long id) {
        log.debug("REST request to delete Finishing : {}", id);
        finishingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/finishings?query=:query : search for the finishing corresponding
     * to the query.
     *
     * @param query the query of the finishing search
     * @return the result of the search
     */
    @GetMapping("/_search/finishings")
    @Timed
    public List<FinishingDTO> searchFinishings(@RequestParam String query) {
        log.debug("REST request to search Finishings for query {}", query);
        return finishingService.search(query);
    }

}
