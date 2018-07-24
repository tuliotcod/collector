package com.collector.repository.search;

import com.collector.domain.Finishing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Finishing entity.
 */
public interface FinishingSearchRepository extends ElasticsearchRepository<Finishing, Long> {
}
