package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.CurrencyService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.CurrencyDTO;
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
 * REST controller for managing Currency.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    private final CurrencyService currencyService;

    public CurrencyResource(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * POST  /currencies : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyDTO, or with status 400 (Bad Request) if the currency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencyDTO> createCurrency(@RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity.created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currencies : Updates an existing currency.
     *
     * @param currencyDTO the currencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyDTO,
     * or with status 400 (Bad Request) if the currencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencyDTO> updateCurrency(@RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currencies : get all the currencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of currencies in body
     */
    @GetMapping("/currencies")
    @Timed
    public List<CurrencyDTO> getAllCurrencies() {
        log.debug("REST request to get all Currencies");
        return currencyService.findAll();
    }

    /**
     * GET  /currencies/:id : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDTO);
    }

    /**
     * DELETE  /currencies/:id : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/currencies?query=:query : search for the currency corresponding
     * to the query.
     *
     * @param query the query of the currency search
     * @return the result of the search
     */
    @GetMapping("/_search/currencies")
    @Timed
    public List<CurrencyDTO> searchCurrencies(@RequestParam String query) {
        log.debug("REST request to search Currencies for query {}", query);
        return currencyService.search(query);
    }

}
