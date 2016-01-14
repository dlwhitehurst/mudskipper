package com.ciwise.mudskipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciwise.mudskipper.domain.Genaccount;
import com.ciwise.mudskipper.repository.GenaccountRepository;
import com.ciwise.mudskipper.repository.search.GenaccountSearchRepository;
import com.ciwise.mudskipper.web.rest.util.HeaderUtil;
import com.ciwise.mudskipper.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Genaccount.
 */
@RestController
@RequestMapping("/api")
public class GenaccountResource {

    private final Logger log = LoggerFactory.getLogger(GenaccountResource.class);
        
    @Inject
    private GenaccountRepository genaccountRepository;
    
    @Inject
    private GenaccountSearchRepository genaccountSearchRepository;
    
    /**
     * POST  /genaccounts -> Create a new genaccount.
     */
    @RequestMapping(value = "/genaccounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccount> createGenaccount(@RequestBody Genaccount genaccount) throws URISyntaxException {
        log.debug("REST request to save Genaccount : {}", genaccount);
        if (genaccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genaccount", "idexists", "A new genaccount cannot already have an ID")).body(null);
        }
        Genaccount result = genaccountRepository.save(genaccount);
        genaccountSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genaccounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genaccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genaccounts -> Updates an existing genaccount.
     */
    @RequestMapping(value = "/genaccounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccount> updateGenaccount(@RequestBody Genaccount genaccount) throws URISyntaxException {
        log.debug("REST request to update Genaccount : {}", genaccount);
        if (genaccount.getId() == null) {
            return createGenaccount(genaccount);
        }
        Genaccount result = genaccountRepository.save(genaccount);
        genaccountSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genaccount", genaccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genaccounts -> get all the genaccounts.
     */
    @RequestMapping(value = "/genaccounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genaccount>> getAllGenaccounts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Genaccounts");
        Page<Genaccount> page = genaccountRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genaccounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genaccounts/:id -> get the "id" genaccount.
     */
    @RequestMapping(value = "/genaccounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccount> getGenaccount(@PathVariable Long id) {
        log.debug("REST request to get Genaccount : {}", id);
        Genaccount genaccount = genaccountRepository.findOne(id);
        return Optional.ofNullable(genaccount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genaccounts/:id -> delete the "id" genaccount.
     */
    @RequestMapping(value = "/genaccounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenaccount(@PathVariable Long id) {
        log.debug("REST request to delete Genaccount : {}", id);
        genaccountRepository.delete(id);
        genaccountSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genaccount", id.toString())).build();
    }

    /**
     * SEARCH  /_search/genaccounts/:query -> search for the genaccount corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/genaccounts/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genaccount> searchGenaccounts(@PathVariable String query) {
        log.debug("REST request to search Genaccounts for query {}", query);
        return StreamSupport
            .stream(genaccountSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
