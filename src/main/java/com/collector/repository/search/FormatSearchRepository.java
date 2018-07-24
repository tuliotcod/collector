package com.collector.repository.search;

import com.collector.domain.Format;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Format entity.
 */
public interface FormatSearchRepository extends ElasticsearchRepository<Format, Long> {
}
