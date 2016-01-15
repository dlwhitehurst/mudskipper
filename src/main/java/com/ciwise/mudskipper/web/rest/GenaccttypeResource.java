package com.ciwise.mudskipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciwise.mudskipper.domain.Genaccttype;
import com.ciwise.mudskipper.repository.GenaccttypeRepository;
import com.ciwise.mudskipper.repository.search.GenaccttypeSearchRepository;
import com.ciwise.mudskipper.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Genaccttype.
 */
@RestController
@RequestMapping("/api")
public class GenaccttypeResource {

    private final Logger log = LoggerFactory.getLogger(GenaccttypeResource.class);
        
    @Inject
    private GenaccttypeRepository genaccttypeRepository;
    
    @Inject
    private GenaccttypeSearchRepository genaccttypeSearchRepository;
    
    /**
     * POST  /genaccttypes -> Create a new genaccttype.
     */
    @RequestMapping(value = "/genaccttypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccttype> createGenaccttype(@Valid @RequestBody Genaccttype genaccttype) throws URISyntaxException {
        log.debug("REST request to save Genaccttype : {}", genaccttype);
        if (genaccttype.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genaccttype", "idexists", "A new genaccttype cannot already have an ID")).body(null);
        }
        Genaccttype result = genaccttypeRepository.save(genaccttype);
        genaccttypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genaccttypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genaccttype", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genaccttypes -> Updates an existing genaccttype.
     */
    @RequestMapping(value = "/genaccttypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccttype> updateGenaccttype(@Valid @RequestBody Genaccttype genaccttype) throws URISyntaxException {
        log.debug("REST request to update Genaccttype : {}", genaccttype);
        if (genaccttype.getId() == null) {
            return createGenaccttype(genaccttype);
        }
        Genaccttype result = genaccttypeRepository.save(genaccttype);
        genaccttypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genaccttype", genaccttype.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genaccttypes -> get all the genaccttypes.
     */
    @RequestMapping(value = "/genaccttypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genaccttype> getAllGenaccttypes() {
        log.debug("REST request to get all Genaccttypes");
        return genaccttypeRepository.findAll();
            }

    /**
     * GET  /genaccttypes/:id -> get the "id" genaccttype.
     */
    @RequestMapping(value = "/genaccttypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genaccttype> getGenaccttype(@PathVariable Long id) {
        log.debug("REST request to get Genaccttype : {}", id);
        Genaccttype genaccttype = genaccttypeRepository.findOne(id);
        return Optional.ofNullable(genaccttype)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genaccttypes/:id -> delete the "id" genaccttype.
     */
    @RequestMapping(value = "/genaccttypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenaccttype(@PathVariable Long id) {
        log.debug("REST request to delete Genaccttype : {}", id);
        genaccttypeRepository.delete(id);
        genaccttypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genaccttype", id.toString())).build();
    }

    /**
     * SEARCH  /_search/genaccttypes/:query -> search for the genaccttype corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/genaccttypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genaccttype> searchGenaccttypes(@PathVariable String query) {
        log.debug("REST request to search Genaccttypes for query {}", query);
        return StreamSupport
            .stream(genaccttypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
