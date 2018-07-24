package com.collector.repository.search;

import com.collector.domain.Collaborator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Collaborator entity.
 */
public interface CollaboratorSearchRepository extends ElasticsearchRepository<Collaborator, Long> {
}
