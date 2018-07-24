package com.collector.repository.search;

import com.collector.domain.Publisher;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Publisher entity.
 */
public interface PublisherSearchRepository extends ElasticsearchRepository<Publisher, Long> {
}
