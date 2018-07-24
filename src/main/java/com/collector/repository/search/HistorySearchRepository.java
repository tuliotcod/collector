package com.collector.repository.search;

import com.collector.domain.History;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the History entity.
 */
public interface HistorySearchRepository extends ElasticsearchRepository<History, Long> {
}
