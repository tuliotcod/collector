package com.collector.repository.search;

import com.collector.domain.Contribution;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Contribution entity.
 */
public interface ContributionSearchRepository extends ElasticsearchRepository<Contribution, Long> {
}
