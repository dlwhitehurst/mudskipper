package com.ciwise.mudskipper.repository.search;

import com.ciwise.mudskipper.domain.Registration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Registration entity.
 */
public interface RegistrationSearchRepository extends ElasticsearchRepository<Registration, Long> {
}
