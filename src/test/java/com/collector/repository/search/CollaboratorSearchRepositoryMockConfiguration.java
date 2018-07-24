package com.collector.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CollaboratorSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CollaboratorSearchRepositoryMockConfiguration {

    @MockBean
    private CollaboratorSearchRepository mockCollaboratorSearchRepository;

}
