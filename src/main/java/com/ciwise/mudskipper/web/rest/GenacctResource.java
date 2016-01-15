package com.ciwise.mudskipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciwise.mudskipper.domain.Genacct;
import com.ciwise.mudskipper.repository.GenacctRepository;
import com.ciwise.mudskipper.repository.search.GenacctSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Genacct.
 */
@RestController
@RequestMapping("/api")
public class GenacctResource {

    private final Logger log = LoggerFactory.getLogger(GenacctResource.class);
        
    @Inject
    private GenacctRepository genacctRepository;
    
    @Inject
    private GenacctSearchRepository genacctSearchRepository;
    
    /**
     * POST  /genaccts -> Create a new genacct.
     */
    @RequestMapping(value = "/genaccts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacct> createGenacct(@Valid @RequestBody Genacct genacct) throws URISyntaxException {
        log.debug("REST request to save Genacct : {}", genacct);
        if (genacct.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genacct", "idexists", "A new genacct cannot already have an ID")).body(null);
        }
        Genacct result = genacctRepository.save(genacct);
        genacctSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genaccts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genacct", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genaccts -> Updates an existing genacct.
     */
    @RequestMapping(value = "/genaccts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacct> updateGenacct(@Valid @RequestBody Genacct genacct) throws URISyntaxException {
        log.debug("REST request to update Genacct : {}", genacct);
        if (genacct.getId() == null) {
            return createGenacct(genacct);
        }
        Genacct result = genacctRepository.save(genacct);
        genacctSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genacct", genacct.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genaccts -> get all the genaccts.
     */
    @RequestMapping(value = "/genaccts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genacct>> getAllGenaccts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Genaccts");
        Page<Genacct> page = genacctRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genaccts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genaccts/:id -> get the "id" genacct.
     */
    @RequestMapping(value = "/genaccts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacct> getGenacct(@PathVariable Long id) {
        log.debug("REST request to get Genacct : {}", id);
        Genacct genacct = genacctRepository.findOne(id);
        return Optional.ofNullable(genacct)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genaccts/:id -> delete the "id" genacct.
     */
    @RequestMapping(value = "/genaccts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenacct(@PathVariable Long id) {
        log.debug("REST request to delete Genacct : {}", id);
        genacctRepository.delete(id);
        genacctSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genacct", id.toString())).build();
    }

    /**
     * SEARCH  /_search/genaccts/:query -> search for the genacct corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/genaccts/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genacct> searchGenaccts(@PathVariable String query) {
        log.debug("REST request to search Genaccts for query {}", query);
        return StreamSupport
            .stream(genacctSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
