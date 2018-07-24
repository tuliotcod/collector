package com.collector.repository.search;

import com.collector.domain.IssueInWishlist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IssueInWishlist entity.
 */
public interface IssueInWishlistSearchRepository extends ElasticsearchRepository<IssueInWishlist, Long> {
}
