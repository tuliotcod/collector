package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.ContributionTypeService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.ContributionTypeDTO;
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
 * REST controller for managing ContributionType.
 */
@RestController
@RequestMapping("/api")
public class ContributionTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContributionTypeResource.class);

    private static final String ENTITY_NAME = "contributionType";

    private final ContributionTypeService contributionTypeService;

    public ContributionTypeResource(ContributionTypeService contributionTypeService) {
        this.contributionTypeService = contributionTypeService;
    }

    /**
     * POST  /contribution-types : Create a new contributionType.
     *
     * @param contributionTypeDTO the contributionTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contributionTypeDTO, or with status 400 (Bad Request) if the contributionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contribution-types")
    @Timed
    public ResponseEntity<ContributionTypeDTO> createContributionType(@RequestBody ContributionTypeDTO contributionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ContributionType : {}", contributionTypeDTO);
        if (contributionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new contributionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionTypeDTO result = contributionTypeService.save(contributionTypeDTO);
        return ResponseEntity.created(new URI("/api/contribution-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contribution-types : Updates an existing contributionType.
     *
     * @param contributionTypeDTO the contributionTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contributionTypeDTO,
     * or with status 400 (Bad Request) if the contributionTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the contributionTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contribution-types")
    @Timed
    public ResponseEntity<ContributionTypeDTO> updateContributionType(@RequestBody ContributionTypeDTO contributionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ContributionType : {}", contributionTypeDTO);
        if (contributionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContributionTypeDTO result = contributionTypeService.save(contributionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contributionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contribution-types : get all the contributionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contributionTypes in body
     */
    @GetMapping("/contribution-types")
    @Timed
    public List<ContributionTypeDTO> getAllContributionTypes() {
        log.debug("REST request to get all ContributionTypes");
        return contributionTypeService.findAll();
    }

    /**
     * GET  /contribution-types/:id : get the "id" contributionType.
     *
     * @param id the id of the contributionTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contributionTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contribution-types/{id}")
    @Timed
    public ResponseEntity<ContributionTypeDTO> getContributionType(@PathVariable Long id) {
        log.debug("REST request to get ContributionType : {}", id);
        Optional<ContributionTypeDTO> contributionTypeDTO = contributionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contributionTypeDTO);
    }

    /**
     * DELETE  /contribution-types/:id : delete the "id" contributionType.
     *
     * @param id the id of the contributionTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contribution-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteContributionType(@PathVariable Long id) {
        log.debug("REST request to delete ContributionType : {}", id);
        contributionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contribution-types?query=:query : search for the contributionType corresponding
     * to the query.
     *
     * @param query the query of the contributionType search
     * @return the result of the search
     */
    @GetMapping("/_search/contribution-types")
    @Timed
    public List<ContributionTypeDTO> searchContributionTypes(@RequestParam String query) {
        log.debug("REST request to search ContributionTypes for query {}", query);
        return contributionTypeService.search(query);
    }

}
