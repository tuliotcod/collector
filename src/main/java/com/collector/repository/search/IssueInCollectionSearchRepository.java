package com.collector.repository.search;

import com.collector.domain.IssueInCollection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IssueInCollection entity.
 */
public interface IssueInCollectionSearchRepository extends ElasticsearchRepository<IssueInCollection, Long> {
}
