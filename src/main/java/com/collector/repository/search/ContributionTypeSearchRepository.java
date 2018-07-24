package com.collector.repository.search;

import com.collector.domain.ContributionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContributionType entity.
 */
public interface ContributionTypeSearchRepository extends ElasticsearchRepository<ContributionType, Long> {
}
