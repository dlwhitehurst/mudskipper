package com.ciwise.mudskipper.repository.search;

import com.ciwise.mudskipper.domain.Genaccttype;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Genaccttype entity.
 */
public interface GenaccttypeSearchRepository extends ElasticsearchRepository<Genaccttype, Long> {
}
