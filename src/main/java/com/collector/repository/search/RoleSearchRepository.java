package com.collector.repository.search;

import com.collector.domain.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Role entity.
 */
public interface RoleSearchRepository extends ElasticsearchRepository<Role, Long> {
}
