package com.collector.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.collector.service.WishlistService;
import com.collector.web.rest.errors.BadRequestAlertException;
import com.collector.web.rest.util.HeaderUtil;
import com.collector.service.dto.WishlistDTO;
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
 * REST controller for managing Wishlist.
 */
@RestController
@RequestMapping("/api")
public class WishlistResource {

    private final Logger log = LoggerFactory.getLogger(WishlistResource.class);

    private static final String ENTITY_NAME = "wishlist";

    private final WishlistService wishlistService;

    public WishlistResource(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * POST  /wishlists : Create a new wishlist.
     *
     * @param wishlistDTO the wishlistDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wishlistDTO, or with status 400 (Bad Request) if the wishlist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wishlists")
    @Timed
    public ResponseEntity<WishlistDTO> createWishlist(@RequestBody WishlistDTO wishlistDTO) throws URISyntaxException {
        log.debug("REST request to save Wishlist : {}", wishlistDTO);
        if (wishlistDTO.getId() != null) {
            throw new BadRequestAlertException("A new wishlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WishlistDTO result = wishlistService.save(wishlistDTO);
        return ResponseEntity.created(new URI("/api/wishlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wishlists : Updates an existing wishlist.
     *
     * @param wishlistDTO the wishlistDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wishlistDTO,
     * or with status 400 (Bad Request) if the wishlistDTO is not valid,
     * or with status 500 (Internal Server Error) if the wishlistDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wishlists")
    @Timed
    public ResponseEntity<WishlistDTO> updateWishlist(@RequestBody WishlistDTO wishlistDTO) throws URISyntaxException {
        log.debug("REST request to update Wishlist : {}", wishlistDTO);
        if (wishlistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WishlistDTO result = wishlistService.save(wishlistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wishlistDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wishlists : get all the wishlists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wishlists in body
     */
    @GetMapping("/wishlists")
    @Timed
    public List<WishlistDTO> getAllWishlists() {
        log.debug("REST request to get all Wishlists");
        return wishlistService.findAll();
    }

    /**
     * GET  /wishlists/:id : get the "id" wishlist.
     *
     * @param id the id of the wishlistDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wishlistDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wishlists/{id}")
    @Timed
    public ResponseEntity<WishlistDTO> getWishlist(@PathVariable Long id) {
        log.debug("REST request to get Wishlist : {}", id);
        Optional<WishlistDTO> wishlistDTO = wishlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wishlistDTO);
    }

    /**
     * DELETE  /wishlists/:id : delete the "id" wishlist.
     *
     * @param id the id of the wishlistDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wishlists/{id}")
    @Timed
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        log.debug("REST request to delete Wishlist : {}", id);
        wishlistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wishlists?query=:query : search for the wishlist corresponding
     * to the query.
     *
     * @param query the query of the wishlist search
     * @return the result of the search
     */
    @GetMapping("/_search/wishlists")
    @Timed
    public List<WishlistDTO> searchWishlists(@RequestParam String query) {
        log.debug("REST request to search Wishlists for query {}", query);
        return wishlistService.search(query);
    }

}
