package com.collector.repository.search;

import com.collector.domain.Artist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Artist entity.
 */
public interface ArtistSearchRepository extends ElasticsearchRepository<Artist, Long> {
}
