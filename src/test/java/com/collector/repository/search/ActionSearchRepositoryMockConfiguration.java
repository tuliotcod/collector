package com.collector.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ActionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ActionSearchRepositoryMockConfiguration {

    @MockBean
    private ActionSearchRepository mockActionSearchRepository;

}
