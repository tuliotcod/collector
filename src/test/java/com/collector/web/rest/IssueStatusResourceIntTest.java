package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.IssueStatus;
import com.collector.repository.IssueStatusRepository;
import com.collector.repository.search.IssueStatusSearchRepository;
import com.collector.service.IssueStatusService;
import com.collector.service.dto.IssueStatusDTO;
import com.collector.service.mapper.IssueStatusMapper;
import com.collector.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.collector.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IssueStatusResource REST controller.
 *
 * @see IssueStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class IssueStatusResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private IssueStatusRepository issueStatusRepository;


    @Autowired
    private IssueStatusMapper issueStatusMapper;
    

    @Autowired
    private IssueStatusService issueStatusService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.IssueStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueStatusSearchRepository mockIssueStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueStatusMockMvc;

    private IssueStatus issueStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueStatusResource issueStatusResource = new IssueStatusResource(issueStatusService);
        this.restIssueStatusMockMvc = MockMvcBuilders.standaloneSetup(issueStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueStatus createEntity(EntityManager em) {
        IssueStatus issueStatus = new IssueStatus()
            .desc(DEFAULT_DESC);
        return issueStatus;
    }

    @Before
    public void initTest() {
        issueStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssueStatus() throws Exception {
        int databaseSizeBeforeCreate = issueStatusRepository.findAll().size();

        // Create the IssueStatus
        IssueStatusDTO issueStatusDTO = issueStatusMapper.toDto(issueStatus);
        restIssueStatusMockMvc.perform(post("/api/issue-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the IssueStatus in the database
        List<IssueStatus> issueStatusList = issueStatusRepository.findAll();
        assertThat(issueStatusList).hasSize(databaseSizeBeforeCreate + 1);
        IssueStatus testIssueStatus = issueStatusList.get(issueStatusList.size() - 1);
        assertThat(testIssueStatus.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the IssueStatus in Elasticsearch
        verify(mockIssueStatusSearchRepository, times(1)).save(testIssueStatus);
    }

    @Test
    @Transactional
    public void createIssueStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueStatusRepository.findAll().size();

        // Create the IssueStatus with an existing ID
        issueStatus.setId(1L);
        IssueStatusDTO issueStatusDTO = issueStatusMapper.toDto(issueStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueStatusMockMvc.perform(post("/api/issue-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueStatus in the database
        List<IssueStatus> issueStatusList = issueStatusRepository.findAll();
        assertThat(issueStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssueStatus in Elasticsearch
        verify(mockIssueStatusSearchRepository, times(0)).save(issueStatus);
    }

    @Test
    @Transactional
    public void getAllIssueStatuses() throws Exception {
        // Initialize the database
        issueStatusRepository.saveAndFlush(issueStatus);

        // Get all the issueStatusList
        restIssueStatusMockMvc.perform(get("/api/issue-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getIssueStatus() throws Exception {
        // Initialize the database
        issueStatusRepository.saveAndFlush(issueStatus);

        // Get the issueStatus
        restIssueStatusMockMvc.perform(get("/api/issue-statuses/{id}", issueStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issueStatus.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingIssueStatus() throws Exception {
        // Get the issueStatus
        restIssueStatusMockMvc.perform(get("/api/issue-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssueStatus() throws Exception {
        // Initialize the database
        issueStatusRepository.saveAndFlush(issueStatus);

        int databaseSizeBeforeUpdate = issueStatusRepository.findAll().size();

        // Update the issueStatus
        IssueStatus updatedIssueStatus = issueStatusRepository.findById(issueStatus.getId()).get();
        // Disconnect from session so that the updates on updatedIssueStatus are not directly saved in db
        em.detach(updatedIssueStatus);
        updatedIssueStatus
            .desc(UPDATED_DESC);
        IssueStatusDTO issueStatusDTO = issueStatusMapper.toDto(updatedIssueStatus);

        restIssueStatusMockMvc.perform(put("/api/issue-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueStatusDTO)))
            .andExpect(status().isOk());

        // Validate the IssueStatus in the database
        List<IssueStatus> issueStatusList = issueStatusRepository.findAll();
        assertThat(issueStatusList).hasSize(databaseSizeBeforeUpdate);
        IssueStatus testIssueStatus = issueStatusList.get(issueStatusList.size() - 1);
        assertThat(testIssueStatus.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the IssueStatus in Elasticsearch
        verify(mockIssueStatusSearchRepository, times(1)).save(testIssueStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingIssueStatus() throws Exception {
        int databaseSizeBeforeUpdate = issueStatusRepository.findAll().size();

        // Create the IssueStatus
        IssueStatusDTO issueStatusDTO = issueStatusMapper.toDto(issueStatus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueStatusMockMvc.perform(put("/api/issue-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueStatus in the database
        List<IssueStatus> issueStatusList = issueStatusRepository.findAll();
        assertThat(issueStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssueStatus in Elasticsearch
        verify(mockIssueStatusSearchRepository, times(0)).save(issueStatus);
    }

    @Test
    @Transactional
    public void deleteIssueStatus() throws Exception {
        // Initialize the database
        issueStatusRepository.saveAndFlush(issueStatus);

        int databaseSizeBeforeDelete = issueStatusRepository.findAll().size();

        // Get the issueStatus
        restIssueStatusMockMvc.perform(delete("/api/issue-statuses/{id}", issueStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IssueStatus> issueStatusList = issueStatusRepository.findAll();
        assertThat(issueStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssueStatus in Elasticsearch
        verify(mockIssueStatusSearchRepository, times(1)).deleteById(issueStatus.getId());
    }

    @Test
    @Transactional
    public void searchIssueStatus() throws Exception {
        // Initialize the database
        issueStatusRepository.saveAndFlush(issueStatus);
        when(mockIssueStatusSearchRepository.search(queryStringQuery("id:" + issueStatus.getId())))
            .thenReturn(Collections.singletonList(issueStatus));
        // Search the issueStatus
        restIssueStatusMockMvc.perform(get("/api/_search/issue-statuses?query=id:" + issueStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueStatus.class);
        IssueStatus issueStatus1 = new IssueStatus();
        issueStatus1.setId(1L);
        IssueStatus issueStatus2 = new IssueStatus();
        issueStatus2.setId(issueStatus1.getId());
        assertThat(issueStatus1).isEqualTo(issueStatus2);
        issueStatus2.setId(2L);
        assertThat(issueStatus1).isNotEqualTo(issueStatus2);
        issueStatus1.setId(null);
        assertThat(issueStatus1).isNotEqualTo(issueStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueStatusDTO.class);
        IssueStatusDTO issueStatusDTO1 = new IssueStatusDTO();
        issueStatusDTO1.setId(1L);
        IssueStatusDTO issueStatusDTO2 = new IssueStatusDTO();
        assertThat(issueStatusDTO1).isNotEqualTo(issueStatusDTO2);
        issueStatusDTO2.setId(issueStatusDTO1.getId());
        assertThat(issueStatusDTO1).isEqualTo(issueStatusDTO2);
        issueStatusDTO2.setId(2L);
        assertThat(issueStatusDTO1).isNotEqualTo(issueStatusDTO2);
        issueStatusDTO1.setId(null);
        assertThat(issueStatusDTO1).isNotEqualTo(issueStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueStatusMapper.fromId(null)).isNull();
    }
}
