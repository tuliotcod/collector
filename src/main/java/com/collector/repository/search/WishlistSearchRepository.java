package com.collector.repository.search;

import com.collector.domain.Wishlist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Wishlist entity.
 */
public interface WishlistSearchRepository extends ElasticsearchRepository<Wishlist, Long> {
}
