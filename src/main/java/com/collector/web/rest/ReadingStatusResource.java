package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.ReadingStatusService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.ReadingStatusDTO;
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
 * REST controller for managing ReadingStatus.
 */
@RestController
@RequestMapping("/api")
public class ReadingStatusResource {

    private final Logger log = LoggerFactory.getLogger(ReadingStatusResource.class);

    private static final String ENTITY_NAME = "readingStatus";

    private final ReadingStatusService readingStatusService;

    public ReadingStatusResource(ReadingStatusService readingStatusService) {
        this.readingStatusService = readingStatusService;
    }

    /**
     * POST  /reading-statuses : Create a new readingStatus.
     *
     * @param readingStatusDTO the readingStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new readingStatusDTO, or with status 400 (Bad Request) if the readingStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reading-statuses")
    @Timed
    public ResponseEntity<ReadingStatusDTO> createReadingStatus(@RequestBody ReadingStatusDTO readingStatusDTO) throws URISyntaxException {
        log.debug("REST request to save ReadingStatus : {}", readingStatusDTO);
        if (readingStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new readingStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReadingStatusDTO result = readingStatusService.save(readingStatusDTO);
        return ResponseEntity.created(new URI("/api/reading-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reading-statuses : Updates an existing readingStatus.
     *
     * @param readingStatusDTO the readingStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated readingStatusDTO,
     * or with status 400 (Bad Request) if the readingStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the readingStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reading-statuses")
    @Timed
    public ResponseEntity<ReadingStatusDTO> updateReadingStatus(@RequestBody ReadingStatusDTO readingStatusDTO) throws URISyntaxException {
        log.debug("REST request to update ReadingStatus : {}", readingStatusDTO);
        if (readingStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReadingStatusDTO result = readingStatusService.save(readingStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, readingStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reading-statuses : get all the readingStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of readingStatuses in body
     */
    @GetMapping("/reading-statuses")
    @Timed
    public List<ReadingStatusDTO> getAllReadingStatuses() {
        log.debug("REST request to get all ReadingStatuses");
        return readingStatusService.findAll();
    }

    /**
     * GET  /reading-statuses/:id : get the "id" readingStatus.
     *
     * @param id the id of the readingStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the readingStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reading-statuses/{id}")
    @Timed
    public ResponseEntity<ReadingStatusDTO> getReadingStatus(@PathVariable Long id) {
        log.debug("REST request to get ReadingStatus : {}", id);
        Optional<ReadingStatusDTO> readingStatusDTO = readingStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(readingStatusDTO);
    }

    /**
     * DELETE  /reading-statuses/:id : delete the "id" readingStatus.
     *
     * @param id the id of the readingStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reading-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteReadingStatus(@PathVariable Long id) {
        log.debug("REST request to delete ReadingStatus : {}", id);
        readingStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reading-statuses?query=:query : search for the readingStatus corresponding
     * to the query.
     *
     * @param query the query of the readingStatus search
     * @return the result of the search
     */
    @GetMapping("/_search/reading-statuses")
    @Timed
    public List<ReadingStatusDTO> searchReadingStatuses(@RequestParam String query) {
        log.debug("REST request to search ReadingStatuses for query {}", query);
        return readingStatusService.search(query);
    }

}
