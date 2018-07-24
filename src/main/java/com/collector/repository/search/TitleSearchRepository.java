package com.collector.repository.search;

import com.collector.domain.Title;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Title entity.
 */
public interface TitleSearchRepository extends ElasticsearchRepository<Title, Long> {
}
