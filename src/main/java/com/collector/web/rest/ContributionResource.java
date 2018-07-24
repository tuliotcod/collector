package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.ContributionService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.ContributionDTO;
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
 * REST controller for managing Contribution.
 */
@RestController
@RequestMapping("/api")
public class ContributionResource {

    private final Logger log = LoggerFactory.getLogger(ContributionResource.class);

    private static final String ENTITY_NAME = "contribution";

    private final ContributionService contributionService;

    public ContributionResource(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    /**
     * POST  /contributions : Create a new contribution.
     *
     * @param contributionDTO the contributionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contributionDTO, or with status 400 (Bad Request) if the contribution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contributions")
    @Timed
    public ResponseEntity<ContributionDTO> createContribution(@RequestBody ContributionDTO contributionDTO) throws URISyntaxException {
        log.debug("REST request to save Contribution : {}", contributionDTO);
        if (contributionDTO.getId() != null) {
            throw new BadRequestAlertException("A new contribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionDTO result = contributionService.save(contributionDTO);
        return ResponseEntity.created(new URI("/api/contributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contributions : Updates an existing contribution.
     *
     * @param contributionDTO the contributionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contributionDTO,
     * or with status 400 (Bad Request) if the contributionDTO is not valid,
     * or with status 500 (Internal Server Error) if the contributionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contributions")
    @Timed
    public ResponseEntity<ContributionDTO> updateContribution(@RequestBody ContributionDTO contributionDTO) throws URISyntaxException {
        log.debug("REST request to update Contribution : {}", contributionDTO);
        if (contributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContributionDTO result = contributionService.save(contributionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contributionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contributions : get all the contributions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contributions in body
     */
    @GetMapping("/contributions")
    @Timed
    public List<ContributionDTO> getAllContributions() {
        log.debug("REST request to get all Contributions");
        return contributionService.findAll();
    }

    /**
     * GET  /contributions/:id : get the "id" contribution.
     *
     * @param id the id of the contributionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contributionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contributions/{id}")
    @Timed
    public ResponseEntity<ContributionDTO> getContribution(@PathVariable Long id) {
        log.debug("REST request to get Contribution : {}", id);
        Optional<ContributionDTO> contributionDTO = contributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contributionDTO);
    }

    /**
     * DELETE  /contributions/:id : delete the "id" contribution.
     *
     * @param id the id of the contributionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contributions/{id}")
    @Timed
    public ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        log.debug("REST request to delete Contribution : {}", id);
        contributionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contributions?query=:query : search for the contribution corresponding
     * to the query.
     *
     * @param query the query of the contribution search
     * @return the result of the search
     */
    @GetMapping("/_search/contributions")
    @Timed
    public List<ContributionDTO> searchContributions(@RequestParam String query) {
        log.debug("REST request to search Contributions for query {}", query);
        return contributionService.search(query);
    }

}
