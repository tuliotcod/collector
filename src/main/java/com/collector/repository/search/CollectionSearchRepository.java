package com.collector.repository.search;

import com.collector.domain.Collection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Collection entity.
 */
public interface CollectionSearchRepository extends ElasticsearchRepository<Collection, Long> {
}
