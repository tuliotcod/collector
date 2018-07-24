package com.collector.repository.search;

import com.collector.domain.Licensor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Licensor entity.
 */
public interface LicensorSearchRepository extends ElasticsearchRepository<Licensor, Long> {
}
