package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.IssueInCollectionService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.IssueInCollectionDTO;
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
 * REST controller for managing IssueInCollection.
 */
@RestController
@RequestMapping("/api")
public class IssueInCollectionResource {

    private final Logger log = LoggerFactory.getLogger(IssueInCollectionResource.class);

    private static final String ENTITY_NAME = "issueInCollection";

    private final IssueInCollectionService issueInCollectionService;

    public IssueInCollectionResource(IssueInCollectionService issueInCollectionService) {
        this.issueInCollectionService = issueInCollectionService;
    }

    /**
     * POST  /issue-in-collections : Create a new issueInCollection.
     *
     * @param issueInCollectionDTO the issueInCollectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueInCollectionDTO, or with status 400 (Bad Request) if the issueInCollection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issue-in-collections")
    @Timed
    public ResponseEntity<IssueInCollectionDTO> createIssueInCollection(@Valid @RequestBody IssueInCollectionDTO issueInCollectionDTO) throws URISyntaxException {
        log.debug("REST request to save IssueInCollection : {}", issueInCollectionDTO);
        if (issueInCollectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueInCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueInCollectionDTO result = issueInCollectionService.save(issueInCollectionDTO);
        return ResponseEntity.created(new URI("/api/issue-in-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issue-in-collections : Updates an existing issueInCollection.
     *
     * @param issueInCollectionDTO the issueInCollectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueInCollectionDTO,
     * or with status 400 (Bad Request) if the issueInCollectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the issueInCollectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issue-in-collections")
    @Timed
    public ResponseEntity<IssueInCollectionDTO> updateIssueInCollection(@Valid @RequestBody IssueInCollectionDTO issueInCollectionDTO) throws URISyntaxException {
        log.debug("REST request to update IssueInCollection : {}", issueInCollectionDTO);
        if (issueInCollectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueInCollectionDTO result = issueInCollectionService.save(issueInCollectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueInCollectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issue-in-collections : get all the issueInCollections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of issueInCollections in body
     */
    @GetMapping("/issue-in-collections")
    @Timed
    public List<IssueInCollectionDTO> getAllIssueInCollections() {
        log.debug("REST request to get all IssueInCollections");
        return issueInCollectionService.findAll();
    }

    /**
     * GET  /issue-in-collections/:id : get the "id" issueInCollection.
     *
     * @param id the id of the issueInCollectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueInCollectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issue-in-collections/{id}")
    @Timed
    public ResponseEntity<IssueInCollectionDTO> getIssueInCollection(@PathVariable Long id) {
        log.debug("REST request to get IssueInCollection : {}", id);
        Optional<IssueInCollectionDTO> issueInCollectionDTO = issueInCollectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueInCollectionDTO);
    }

    /**
     * DELETE  /issue-in-collections/:id : delete the "id" issueInCollection.
     *
     * @param id the id of the issueInCollectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issue-in-collections/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssueInCollection(@PathVariable Long id) {
        log.debug("REST request to delete IssueInCollection : {}", id);
        issueInCollectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issue-in-collections?query=:query : search for the issueInCollection corresponding
     * to the query.
     *
     * @param query the query of the issueInCollection search
     * @return the result of the search
     */
    @GetMapping("/_search/issue-in-collections")
    @Timed
    public List<IssueInCollectionDTO> searchIssueInCollections(@RequestParam String query) {
        log.debug("REST request to search IssueInCollections for query {}", query);
        return issueInCollectionService.search(query);
    }

}
