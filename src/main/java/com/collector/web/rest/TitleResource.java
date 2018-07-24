package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.TitleService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.TitleDTO;
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
 * REST controller for managing Title.
 */
@RestController
@RequestMapping("/api")
public class TitleResource {

    private final Logger log = LoggerFactory.getLogger(TitleResource.class);

    private static final String ENTITY_NAME = "title";

    private final TitleService titleService;

    public TitleResource(TitleService titleService) {
        this.titleService = titleService;
    }

    /**
     * POST  /titles : Create a new title.
     *
     * @param titleDTO the titleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new titleDTO, or with status 400 (Bad Request) if the title has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/titles")
    @Timed
    public ResponseEntity<TitleDTO> createTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
        log.debug("REST request to save Title : {}", titleDTO);
        if (titleDTO.getId() != null) {
            throw new BadRequestAlertException("A new title cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TitleDTO result = titleService.save(titleDTO);
        return ResponseEntity.created(new URI("/api/titles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /titles : Updates an existing title.
     *
     * @param titleDTO the titleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated titleDTO,
     * or with status 400 (Bad Request) if the titleDTO is not valid,
     * or with status 500 (Internal Server Error) if the titleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/titles")
    @Timed
    public ResponseEntity<TitleDTO> updateTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
        log.debug("REST request to update Title : {}", titleDTO);
        if (titleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TitleDTO result = titleService.save(titleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, titleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /titles : get all the titles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of titles in body
     */
    @GetMapping("/titles")
    @Timed
    public List<TitleDTO> getAllTitles() {
        log.debug("REST request to get all Titles");
        return titleService.findAll();
    }

    /**
     * GET  /titles/:id : get the "id" title.
     *
     * @param id the id of the titleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the titleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/titles/{id}")
    @Timed
    public ResponseEntity<TitleDTO> getTitle(@PathVariable Long id) {
        log.debug("REST request to get Title : {}", id);
        Optional<TitleDTO> titleDTO = titleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(titleDTO);
    }

    /**
     * DELETE  /titles/:id : delete the "id" title.
     *
     * @param id the id of the titleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/titles/{id}")
    @Timed
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        log.debug("REST request to delete Title : {}", id);
        titleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/titles?query=:query : search for the title corresponding
     * to the query.
     *
     * @param query the query of the title search
     * @return the result of the search
     */
    @GetMapping("/_search/titles")
    @Timed
    public List<TitleDTO> searchTitles(@RequestParam String query) {
        log.debug("REST request to search Titles for query {}", query);
        return titleService.search(query);
    }

}
