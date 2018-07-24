package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.LicensorService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.LicensorDTO;
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
 * REST controller for managing Licensor.
 */
@RestController
@RequestMapping("/api")
public class LicensorResource {

    private final Logger log = LoggerFactory.getLogger(LicensorResource.class);

    private static final String ENTITY_NAME = "licensor";

    private final LicensorService licensorService;

    public LicensorResource(LicensorService licensorService) {
        this.licensorService = licensorService;
    }

    /**
     * POST  /licensors : Create a new licensor.
     *
     * @param licensorDTO the licensorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new licensorDTO, or with status 400 (Bad Request) if the licensor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/licensors")
    @Timed
    public ResponseEntity<LicensorDTO> createLicensor(@Valid @RequestBody LicensorDTO licensorDTO) throws URISyntaxException {
        log.debug("REST request to save Licensor : {}", licensorDTO);
        if (licensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new licensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LicensorDTO result = licensorService.save(licensorDTO);
        return ResponseEntity.created(new URI("/api/licensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /licensors : Updates an existing licensor.
     *
     * @param licensorDTO the licensorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated licensorDTO,
     * or with status 400 (Bad Request) if the licensorDTO is not valid,
     * or with status 500 (Internal Server Error) if the licensorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/licensors")
    @Timed
    public ResponseEntity<LicensorDTO> updateLicensor(@Valid @RequestBody LicensorDTO licensorDTO) throws URISyntaxException {
        log.debug("REST request to update Licensor : {}", licensorDTO);
        if (licensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LicensorDTO result = licensorService.save(licensorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, licensorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /licensors : get all the licensors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of licensors in body
     */
    @GetMapping("/licensors")
    @Timed
    public List<LicensorDTO> getAllLicensors() {
        log.debug("REST request to get all Licensors");
        return licensorService.findAll();
    }

    /**
     * GET  /licensors/:id : get the "id" licensor.
     *
     * @param id the id of the licensorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the licensorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/licensors/{id}")
    @Timed
    public ResponseEntity<LicensorDTO> getLicensor(@PathVariable Long id) {
        log.debug("REST request to get Licensor : {}", id);
        Optional<LicensorDTO> licensorDTO = licensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(licensorDTO);
    }

    /**
     * DELETE  /licensors/:id : delete the "id" licensor.
     *
     * @param id the id of the licensorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/licensors/{id}")
    @Timed
    public ResponseEntity<Void> deleteLicensor(@PathVariable Long id) {
        log.debug("REST request to delete Licensor : {}", id);
        licensorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/licensors?query=:query : search for the licensor corresponding
     * to the query.
     *
     * @param query the query of the licensor search
     * @return the result of the search
     */
    @GetMapping("/_search/licensors")
    @Timed
    public List<LicensorDTO> searchLicensors(@RequestParam String query) {
        log.debug("REST request to search Licensors for query {}", query);
        return licensorService.search(query);
    }

}
