package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.FormatService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.FormatDTO;
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
 * REST controller for managing Format.
 */
@RestController
@RequestMapping("/api")
public class FormatResource {

    private final Logger log = LoggerFactory.getLogger(FormatResource.class);

    private static final String ENTITY_NAME = "format";

    private final FormatService formatService;

    public FormatResource(FormatService formatService) {
        this.formatService = formatService;
    }

    /**
     * POST  /formats : Create a new format.
     *
     * @param formatDTO the formatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formatDTO, or with status 400 (Bad Request) if the format has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/formats")
    @Timed
    public ResponseEntity<FormatDTO> createFormat(@RequestBody FormatDTO formatDTO) throws URISyntaxException {
        log.debug("REST request to save Format : {}", formatDTO);
        if (formatDTO.getId() != null) {
            throw new BadRequestAlertException("A new format cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormatDTO result = formatService.save(formatDTO);
        return ResponseEntity.created(new URI("/api/formats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /formats : Updates an existing format.
     *
     * @param formatDTO the formatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formatDTO,
     * or with status 400 (Bad Request) if the formatDTO is not valid,
     * or with status 500 (Internal Server Error) if the formatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/formats")
    @Timed
    public ResponseEntity<FormatDTO> updateFormat(@RequestBody FormatDTO formatDTO) throws URISyntaxException {
        log.debug("REST request to update Format : {}", formatDTO);
        if (formatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormatDTO result = formatService.save(formatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /formats : get all the formats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of formats in body
     */
    @GetMapping("/formats")
    @Timed
    public List<FormatDTO> getAllFormats() {
        log.debug("REST request to get all Formats");
        return formatService.findAll();
    }

    /**
     * GET  /formats/:id : get the "id" format.
     *
     * @param id the id of the formatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/formats/{id}")
    @Timed
    public ResponseEntity<FormatDTO> getFormat(@PathVariable Long id) {
        log.debug("REST request to get Format : {}", id);
        Optional<FormatDTO> formatDTO = formatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formatDTO);
    }

    /**
     * DELETE  /formats/:id : delete the "id" format.
     *
     * @param id the id of the formatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/formats/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormat(@PathVariable Long id) {
        log.debug("REST request to delete Format : {}", id);
        formatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/formats?query=:query : search for the format corresponding
     * to the query.
     *
     * @param query the query of the format search
     * @return the result of the search
     */
    @GetMapping("/_search/formats")
    @Timed
    public List<FormatDTO> searchFormats(@RequestParam String query) {
        log.debug("REST request to search Formats for query {}", query);
        return formatService.search(query);
    }

}
