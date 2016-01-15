package com.ciwise.mudskipper.repository.search;

import com.ciwise.mudskipper.domain.Genacctentry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Genacctentry entity.
 */
public interface GenacctentrySearchRepository extends ElasticsearchRepository<Genacctentry, Long> {
}
