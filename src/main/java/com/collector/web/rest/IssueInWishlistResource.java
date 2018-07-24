package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.IssueInWishlistService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.IssueInWishlistDTO;
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
 * REST controller for managing IssueInWishlist.
 */
@RestController
@RequestMapping("/api")
public class IssueInWishlistResource {

    private final Logger log = LoggerFactory.getLogger(IssueInWishlistResource.class);

    private static final String ENTITY_NAME = "issueInWishlist";

    private final IssueInWishlistService issueInWishlistService;

    public IssueInWishlistResource(IssueInWishlistService issueInWishlistService) {
        this.issueInWishlistService = issueInWishlistService;
    }

    /**
     * POST  /issue-in-wishlists : Create a new issueInWishlist.
     *
     * @param issueInWishlistDTO the issueInWishlistDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueInWishlistDTO, or with status 400 (Bad Request) if the issueInWishlist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issue-in-wishlists")
    @Timed
    public ResponseEntity<IssueInWishlistDTO> createIssueInWishlist(@RequestBody IssueInWishlistDTO issueInWishlistDTO) throws URISyntaxException {
        log.debug("REST request to save IssueInWishlist : {}", issueInWishlistDTO);
        if (issueInWishlistDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueInWishlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueInWishlistDTO result = issueInWishlistService.save(issueInWishlistDTO);
        return ResponseEntity.created(new URI("/api/issue-in-wishlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issue-in-wishlists : Updates an existing issueInWishlist.
     *
     * @param issueInWishlistDTO the issueInWishlistDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueInWishlistDTO,
     * or with status 400 (Bad Request) if the issueInWishlistDTO is not valid,
     * or with status 500 (Internal Server Error) if the issueInWishlistDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issue-in-wishlists")
    @Timed
    public ResponseEntity<IssueInWishlistDTO> updateIssueInWishlist(@RequestBody IssueInWishlistDTO issueInWishlistDTO) throws URISyntaxException {
        log.debug("REST request to update IssueInWishlist : {}", issueInWishlistDTO);
        if (issueInWishlistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueInWishlistDTO result = issueInWishlistService.save(issueInWishlistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueInWishlistDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issue-in-wishlists : get all the issueInWishlists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of issueInWishlists in body
     */
    @GetMapping("/issue-in-wishlists")
    @Timed
    public List<IssueInWishlistDTO> getAllIssueInWishlists() {
        log.debug("REST request to get all IssueInWishlists");
        return issueInWishlistService.findAll();
    }

    /**
     * GET  /issue-in-wishlists/:id : get the "id" issueInWishlist.
     *
     * @param id the id of the issueInWishlistDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueInWishlistDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issue-in-wishlists/{id}")
    @Timed
    public ResponseEntity<IssueInWishlistDTO> getIssueInWishlist(@PathVariable Long id) {
        log.debug("REST request to get IssueInWishlist : {}", id);
        Optional<IssueInWishlistDTO> issueInWishlistDTO = issueInWishlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueInWishlistDTO);
    }

    /**
     * DELETE  /issue-in-wishlists/:id : delete the "id" issueInWishlist.
     *
     * @param id the id of the issueInWishlistDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issue-in-wishlists/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssueInWishlist(@PathVariable Long id) {
        log.debug("REST request to delete IssueInWishlist : {}", id);
        issueInWishlistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issue-in-wishlists?query=:query : search for the issueInWishlist corresponding
     * to the query.
     *
     * @param query the query of the issueInWishlist search
     * @return the result of the search
     */
    @GetMapping("/_search/issue-in-wishlists")
    @Timed
    public List<IssueInWishlistDTO> searchIssueInWishlists(@RequestParam String query) {
        log.debug("REST request to search IssueInWishlists for query {}", query);
        return issueInWishlistService.search(query);
    }

}
