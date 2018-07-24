package com.collector.repository.search;

import com.collector.domain.CollectorUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CollectorUser entity.
 */
public interface CollectorUserSearchRepository extends ElasticsearchRepository<CollectorUser, Long> {
}
