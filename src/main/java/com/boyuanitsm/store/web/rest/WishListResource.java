package com.boyuanitsm.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.store.domain.WishList;

import com.boyuanitsm.store.repository.WishListRepository;
import com.boyuanitsm.store.web.rest.util.HeaderUtil;
import com.boyuanitsm.store.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WishList.
 */
@RestController
@RequestMapping("/api")
public class WishListResource {

    private final Logger log = LoggerFactory.getLogger(WishListResource.class);

    private static final String ENTITY_NAME = "wishList";
        
    private final WishListRepository wishListRepository;

    public WishListResource(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    /**
     * POST  /wish-lists : Create a new wishList.
     *
     * @param wishList the wishList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wishList, or with status 400 (Bad Request) if the wishList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wish-lists")
    @Timed
    public ResponseEntity<WishList> createWishList(@Valid @RequestBody WishList wishList) throws URISyntaxException {
        log.debug("REST request to save WishList : {}", wishList);
        if (wishList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wishList cannot already have an ID")).body(null);
        }
        WishList result = wishListRepository.save(wishList);
        return ResponseEntity.created(new URI("/api/wish-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wish-lists : Updates an existing wishList.
     *
     * @param wishList the wishList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wishList,
     * or with status 400 (Bad Request) if the wishList is not valid,
     * or with status 500 (Internal Server Error) if the wishList couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wish-lists")
    @Timed
    public ResponseEntity<WishList> updateWishList(@Valid @RequestBody WishList wishList) throws URISyntaxException {
        log.debug("REST request to update WishList : {}", wishList);
        if (wishList.getId() == null) {
            return createWishList(wishList);
        }
        WishList result = wishListRepository.save(wishList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wishList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wish-lists : get all the wishLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wishLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/wish-lists")
    @Timed
    public ResponseEntity<List<WishList>> getAllWishLists(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WishLists");
        Page<WishList> page = wishListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wish-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wish-lists/:id : get the "id" wishList.
     *
     * @param id the id of the wishList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wishList, or with status 404 (Not Found)
     */
    @GetMapping("/wish-lists/{id}")
    @Timed
    public ResponseEntity<WishList> getWishList(@PathVariable Long id) {
        log.debug("REST request to get WishList : {}", id);
        WishList wishList = wishListRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wishList));
    }

    /**
     * DELETE  /wish-lists/:id : delete the "id" wishList.
     *
     * @param id the id of the wishList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wish-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteWishList(@PathVariable Long id) {
        log.debug("REST request to delete WishList : {}", id);
        wishListRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
