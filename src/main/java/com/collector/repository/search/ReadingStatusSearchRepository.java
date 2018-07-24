package com.collector.repository.search;

import com.collector.domain.ReadingStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReadingStatus entity.
 */
public interface ReadingStatusSearchRepository extends ElasticsearchRepository<ReadingStatus, Long> {
}
