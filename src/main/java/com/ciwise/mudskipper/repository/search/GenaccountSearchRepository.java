package com.ciwise.mudskipper.repository.search;

import com.ciwise.mudskipper.domain.Genaccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Genaccount entity.
 */
public interface GenaccountSearchRepository extends ElasticsearchRepository<Genaccount, Long> {
}
