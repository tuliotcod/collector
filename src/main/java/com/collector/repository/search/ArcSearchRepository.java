package com.collector.repository.search;

import com.collector.domain.Arc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Arc entity.
 */
public interface ArcSearchRepository extends ElasticsearchRepository<Arc, Long> {
}
