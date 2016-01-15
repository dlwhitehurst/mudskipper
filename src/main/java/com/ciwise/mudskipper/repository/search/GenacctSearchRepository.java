package com.ciwise.mudskipper.repository.search;

import com.ciwise.mudskipper.domain.Genacct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Genacct entity.
 */
public interface GenacctSearchRepository extends ElasticsearchRepository<Genacct, Long> {
}
