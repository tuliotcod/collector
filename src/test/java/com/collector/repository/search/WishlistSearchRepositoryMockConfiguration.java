package com.collector.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of WishlistSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class WishlistSearchRepositoryMockConfiguration {

    @MockBean
    private WishlistSearchRepository mockWishlistSearchRepository;

}