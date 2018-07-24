package com.collector.repository.search;

import com.collector.domain.IssueStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IssueStatus entity.
 */
public interface IssueStatusSearchRepository extends ElasticsearchRepository<IssueStatus, Long> {
}
