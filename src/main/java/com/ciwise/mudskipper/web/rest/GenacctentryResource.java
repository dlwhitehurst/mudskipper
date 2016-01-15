package com.ciwise.mudskipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciwise.mudskipper.domain.Genacctentry;
import com.ciwise.mudskipper.repository.GenacctentryRepository;
import com.ciwise.mudskipper.repository.search.GenacctentrySearchRepository;
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
 * REST controller for managing Genacctentry.
 */
@RestController
@RequestMapping("/api")
public class GenacctentryResource {

    private final Logger log = LoggerFactory.getLogger(GenacctentryResource.class);
        
    @Inject
    private GenacctentryRepository genacctentryRepository;
    
    @Inject
    private GenacctentrySearchRepository genacctentrySearchRepository;
    
    /**
     * POST  /genacctentrys -> Create a new genacctentry.
     */
    @RequestMapping(value = "/genacctentrys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacctentry> createGenacctentry(@Valid @RequestBody Genacctentry genacctentry) throws URISyntaxException {
        log.debug("REST request to save Genacctentry : {}", genacctentry);
        if (genacctentry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genacctentry", "idexists", "A new genacctentry cannot already have an ID")).body(null);
        }
        Genacctentry result = genacctentryRepository.save(genacctentry);
        genacctentrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genacctentrys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genacctentry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genacctentrys -> Updates an existing genacctentry.
     */
    @RequestMapping(value = "/genacctentrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacctentry> updateGenacctentry(@Valid @RequestBody Genacctentry genacctentry) throws URISyntaxException {
        log.debug("REST request to update Genacctentry : {}", genacctentry);
        if (genacctentry.getId() == null) {
            return createGenacctentry(genacctentry);
        }
        Genacctentry result = genacctentryRepository.save(genacctentry);
        genacctentrySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genacctentry", genacctentry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genacctentrys -> get all the genacctentrys.
     */
    @RequestMapping(value = "/genacctentrys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genacctentry>> getAllGenacctentrys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Genacctentrys");
        Page<Genacctentry> page = genacctentryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genacctentrys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genacctentrys/:id -> get the "id" genacctentry.
     */
    @RequestMapping(value = "/genacctentrys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genacctentry> getGenacctentry(@PathVariable Long id) {
        log.debug("REST request to get Genacctentry : {}", id);
        Genacctentry genacctentry = genacctentryRepository.findOne(id);
        return Optional.ofNullable(genacctentry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genacctentrys/:id -> delete the "id" genacctentry.
     */
    @RequestMapping(value = "/genacctentrys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenacctentry(@PathVariable Long id) {
        log.debug("REST request to delete Genacctentry : {}", id);
        genacctentryRepository.delete(id);
        genacctentrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genacctentry", id.toString())).build();
    }

    /**
     * SEARCH  /_search/genacctentrys/:query -> search for the genacctentry corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/genacctentrys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genacctentry> searchGenacctentrys(@PathVariable String query) {
        log.debug("REST request to search Genacctentrys for query {}", query);
        return StreamSupport
            .stream(genacctentrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
