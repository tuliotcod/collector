package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.IssueStatusService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.IssueStatusDTO;
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
 * REST controller for managing IssueStatus.
 */
@RestController
@RequestMapping("/api")
public class IssueStatusResource {

    private final Logger log = LoggerFactory.getLogger(IssueStatusResource.class);

    private static final String ENTITY_NAME = "issueStatus";

    private final IssueStatusService issueStatusService;

    public IssueStatusResource(IssueStatusService issueStatusService) {
        this.issueStatusService = issueStatusService;
    }

    /**
     * POST  /issue-statuses : Create a new issueStatus.
     *
     * @param issueStatusDTO the issueStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueStatusDTO, or with status 400 (Bad Request) if the issueStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issue-statuses")
    @Timed
    public ResponseEntity<IssueStatusDTO> createIssueStatus(@RequestBody IssueStatusDTO issueStatusDTO) throws URISyntaxException {
        log.debug("REST request to save IssueStatus : {}", issueStatusDTO);
        if (issueStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueStatusDTO result = issueStatusService.save(issueStatusDTO);
        return ResponseEntity.created(new URI("/api/issue-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issue-statuses : Updates an existing issueStatus.
     *
     * @param issueStatusDTO the issueStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueStatusDTO,
     * or with status 400 (Bad Request) if the issueStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the issueStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issue-statuses")
    @Timed
    public ResponseEntity<IssueStatusDTO> updateIssueStatus(@RequestBody IssueStatusDTO issueStatusDTO) throws URISyntaxException {
        log.debug("REST request to update IssueStatus : {}", issueStatusDTO);
        if (issueStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueStatusDTO result = issueStatusService.save(issueStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issue-statuses : get all the issueStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of issueStatuses in body
     */
    @GetMapping("/issue-statuses")
    @Timed
    public List<IssueStatusDTO> getAllIssueStatuses() {
        log.debug("REST request to get all IssueStatuses");
        return issueStatusService.findAll();
    }

    /**
     * GET  /issue-statuses/:id : get the "id" issueStatus.
     *
     * @param id the id of the issueStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issue-statuses/{id}")
    @Timed
    public ResponseEntity<IssueStatusDTO> getIssueStatus(@PathVariable Long id) {
        log.debug("REST request to get IssueStatus : {}", id);
        Optional<IssueStatusDTO> issueStatusDTO = issueStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueStatusDTO);
    }

    /**
     * DELETE  /issue-statuses/:id : delete the "id" issueStatus.
     *
     * @param id the id of the issueStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issue-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssueStatus(@PathVariable Long id) {
        log.debug("REST request to delete IssueStatus : {}", id);
        issueStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issue-statuses?query=:query : search for the issueStatus corresponding
     * to the query.
     *
     * @param query the query of the issueStatus search
     * @return the result of the search
     */
    @GetMapping("/_search/issue-statuses")
    @Timed
    public List<IssueStatusDTO> searchIssueStatuses(@RequestParam String query) {
        log.debug("REST request to search IssueStatuses for query {}", query);
        return issueStatusService.search(query);
    }

}
