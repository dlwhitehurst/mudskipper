package com.ciwise.mudskipper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciwise.mudskipper.domain.Registration;
import com.ciwise.mudskipper.repository.RegistrationRepository;
import com.ciwise.mudskipper.repository.search.RegistrationSearchRepository;
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
 * REST controller for managing Registration.
 */
@RestController
@RequestMapping("/api")
public class RegistrationResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationResource.class);
        
    @Inject
    private RegistrationRepository registrationRepository;
    
    @Inject
    private RegistrationSearchRepository registrationSearchRepository;
    
    /**
     * POST  /registrations -> Create a new registration.
     */
    @RequestMapping(value = "/registrations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to save Registration : {}", registration);
        if (registration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("registration", "idexists", "A new registration cannot already have an ID")).body(null);
        }
        Registration result = registrationRepository.save(registration);
        registrationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("registration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registrations -> Updates an existing registration.
     */
    @RequestMapping(value = "/registrations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registration> updateRegistration(@RequestBody Registration registration) throws URISyntaxException {
        log.debug("REST request to update Registration : {}", registration);
        if (registration.getId() == null) {
            return createRegistration(registration);
        }
        Registration result = registrationRepository.save(registration);
        registrationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("registration", registration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registrations -> get all the registrations.
     */
    @RequestMapping(value = "/registrations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Registration>> getAllRegistrations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Registrations");
        Page<Registration> page = registrationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registrations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /registrations/:id -> get the "id" registration.
     */
    @RequestMapping(value = "/registrations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        log.debug("REST request to get Registration : {}", id);
        Registration registration = registrationRepository.findOne(id);
        return Optional.ofNullable(registration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /registrations/:id -> delete the "id" registration.
     */
    @RequestMapping(value = "/registrations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        log.debug("REST request to delete Registration : {}", id);
        registrationRepository.delete(id);
        registrationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("registration", id.toString())).build();
    }

    /**
     * SEARCH  /_search/registrations/:query -> search for the registration corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/registrations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Registration> searchRegistrations(@PathVariable String query) {
        log.debug("REST request to search Registrations for query {}", query);
        return StreamSupport
            .stream(registrationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
