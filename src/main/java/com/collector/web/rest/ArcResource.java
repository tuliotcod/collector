package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.ArcService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.ArcDTO;
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
 * REST controller for managing Arc.
 */
@RestController
@RequestMapping("/api")
public class ArcResource {

    private final Logger log = LoggerFactory.getLogger(ArcResource.class);

    private static final String ENTITY_NAME = "arc";

    private final ArcService arcService;

    public ArcResource(ArcService arcService) {
        this.arcService = arcService;
    }

    /**
     * POST  /arcs : Create a new arc.
     *
     * @param arcDTO the arcDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new arcDTO, or with status 400 (Bad Request) if the arc has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/arcs")
    @Timed
    public ResponseEntity<ArcDTO> createArc(@Valid @RequestBody ArcDTO arcDTO) throws URISyntaxException {
        log.debug("REST request to save Arc : {}", arcDTO);
        if (arcDTO.getId() != null) {
            throw new BadRequestAlertException("A new arc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArcDTO result = arcService.save(arcDTO);
        return ResponseEntity.created(new URI("/api/arcs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /arcs : Updates an existing arc.
     *
     * @param arcDTO the arcDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated arcDTO,
     * or with status 400 (Bad Request) if the arcDTO is not valid,
     * or with status 500 (Internal Server Error) if the arcDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/arcs")
    @Timed
    public ResponseEntity<ArcDTO> updateArc(@Valid @RequestBody ArcDTO arcDTO) throws URISyntaxException {
        log.debug("REST request to update Arc : {}", arcDTO);
        if (arcDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArcDTO result = arcService.save(arcDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, arcDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /arcs : get all the arcs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of arcs in body
     */
    @GetMapping("/arcs")
    @Timed
    public List<ArcDTO> getAllArcs() {
        log.debug("REST request to get all Arcs");
        return arcService.findAll();
    }

    /**
     * GET  /arcs/:id : get the "id" arc.
     *
     * @param id the id of the arcDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the arcDTO, or with status 404 (Not Found)
     */
    @GetMapping("/arcs/{id}")
    @Timed
    public ResponseEntity<ArcDTO> getArc(@PathVariable Long id) {
        log.debug("REST request to get Arc : {}", id);
        Optional<ArcDTO> arcDTO = arcService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arcDTO);
    }

    /**
     * DELETE  /arcs/:id : delete the "id" arc.
     *
     * @param id the id of the arcDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/arcs/{id}")
    @Timed
    public ResponseEntity<Void> deleteArc(@PathVariable Long id) {
        log.debug("REST request to delete Arc : {}", id);
        arcService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/arcs?query=:query : search for the arc corresponding
     * to the query.
     *
     * @param query the query of the arc search
     * @return the result of the search
     */
    @GetMapping("/_search/arcs")
    @Timed
    public List<ArcDTO> searchArcs(@RequestParam String query) {
        log.debug("REST request to search Arcs for query {}", query);
        return arcService.search(query);
    }

}
