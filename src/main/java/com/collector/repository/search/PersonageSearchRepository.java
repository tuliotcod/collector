package com.collector.repository.search;

import com.collector.domain.Personage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Personage entity.
 */
public interface PersonageSearchRepository extends ElasticsearchRepository<Personage, Long> {
}
