package com.collector.repository.search;

import com.collector.domain.Genre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Genre entity.
 */
public interface GenreSearchRepository extends ElasticsearchRepository<Genre, Long> {
}
