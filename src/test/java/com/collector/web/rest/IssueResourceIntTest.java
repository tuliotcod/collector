package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Issue;
import com.collector.repository.IssueRepository;
import com.collector.repository.search.IssueSearchRepository;
import com.collector.service.IssueService;
import com.collector.service.dto.IssueDTO;
import com.collector.service.mapper.IssueMapper;
import com.collector.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the IssueResource REST controller.
 *
 * @see IssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class IssueResourceIntTest {

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COVER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COVER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_COVER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COVER_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final Integer DEFAULT_PAGES = 1;
    private static final Integer UPDATED_PAGES = 2;

    private static final Boolean DEFAULT_SAME_FORMAT_ALL_ISSUES = false;
    private static final Boolean UPDATED_SAME_FORMAT_ALL_ISSUES = true;

    private static final BigDecimal DEFAULT_COVER_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COVER_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private IssueRepository issueRepository;


    @Autowired
    private IssueMapper issueMapper;
    

    @Autowired
    private IssueService issueService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.IssueSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueSearchRepository mockIssueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueMockMvc;

    private Issue issue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueResource issueResource = new IssueResource(issueService);
        this.restIssueMockMvc = MockMvcBuilders.standaloneSetup(issueResource)
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
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .number(DEFAULT_NUMBER)
            .info(DEFAULT_INFO)
            .cover(DEFAULT_COVER)
            .coverContentType(DEFAULT_COVER_CONTENT_TYPE)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .day(DEFAULT_DAY)
            .pages(DEFAULT_PAGES)
            .sameFormatAllIssues(DEFAULT_SAME_FORMAT_ALL_ISSUES)
            .coverPrice(DEFAULT_COVER_PRICE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return issue;
    }

    @Before
    public void initTest() {
        issue = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testIssue.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testIssue.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testIssue.getCoverContentType()).isEqualTo(DEFAULT_COVER_CONTENT_TYPE);
        assertThat(testIssue.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testIssue.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testIssue.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testIssue.getPages()).isEqualTo(DEFAULT_PAGES);
        assertThat(testIssue.isSameFormatAllIssues()).isEqualTo(DEFAULT_SAME_FORMAT_ALL_ISSUES);
        assertThat(testIssue.getCoverPrice()).isEqualTo(DEFAULT_COVER_PRICE);
        assertThat(testIssue.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testIssue.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).save(testIssue);
    }

    @Test
    @Transactional
    public void createIssueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(0)).save(issue);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList
        restIssueMockMvc.perform(get("/api/issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].coverContentType").value(hasItem(DEFAULT_COVER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER))))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].sameFormatAllIssues").value(hasItem(DEFAULT_SAME_FORMAT_ALL_ISSUES.booleanValue())))
            .andExpect(jsonPath("$.[*].coverPrice").value(hasItem(DEFAULT_COVER_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.coverContentType").value(DEFAULT_COVER_CONTENT_TYPE))
            .andExpect(jsonPath("$.cover").value(Base64Utils.encodeToString(DEFAULT_COVER)))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.pages").value(DEFAULT_PAGES))
            .andExpect(jsonPath("$.sameFormatAllIssues").value(DEFAULT_SAME_FORMAT_ALL_ISSUES.booleanValue()))
            .andExpect(jsonPath("$.coverPrice").value(DEFAULT_COVER_PRICE.intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findById(issue.getId()).get();
        // Disconnect from session so that the updates on updatedIssue are not directly saved in db
        em.detach(updatedIssue);
        updatedIssue
            .number(UPDATED_NUMBER)
            .info(UPDATED_INFO)
            .cover(UPDATED_COVER)
            .coverContentType(UPDATED_COVER_CONTENT_TYPE)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .day(UPDATED_DAY)
            .pages(UPDATED_PAGES)
            .sameFormatAllIssues(UPDATED_SAME_FORMAT_ALL_ISSUES)
            .coverPrice(UPDATED_COVER_PRICE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testIssue.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testIssue.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testIssue.getCoverContentType()).isEqualTo(UPDATED_COVER_CONTENT_TYPE);
        assertThat(testIssue.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testIssue.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testIssue.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testIssue.getPages()).isEqualTo(UPDATED_PAGES);
        assertThat(testIssue.isSameFormatAllIssues()).isEqualTo(UPDATED_SAME_FORMAT_ALL_ISSUES);
        assertThat(testIssue.getCoverPrice()).isEqualTo(UPDATED_COVER_PRICE);
        assertThat(testIssue.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testIssue.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).save(testIssue);
    }

    @Test
    @Transactional
    public void updateNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(0)).save(issue);
    }

    @Test
    @Transactional
    public void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Get the issue
        restIssueMockMvc.perform(delete("/api/issues/{id}", issue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).deleteById(issue.getId());
    }

    @Test
    @Transactional
    public void searchIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        when(mockIssueSearchRepository.search(queryStringQuery("id:" + issue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(issue), PageRequest.of(0, 1), 1));
        // Search the issue
        restIssueMockMvc.perform(get("/api/_search/issues?query=id:" + issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].coverContentType").value(hasItem(DEFAULT_COVER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER))))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].sameFormatAllIssues").value(hasItem(DEFAULT_SAME_FORMAT_ALL_ISSUES.booleanValue())))
            .andExpect(jsonPath("$.[*].coverPrice").value(hasItem(DEFAULT_COVER_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Issue.class);
        Issue issue1 = new Issue();
        issue1.setId(1L);
        Issue issue2 = new Issue();
        issue2.setId(issue1.getId());
        assertThat(issue1).isEqualTo(issue2);
        issue2.setId(2L);
        assertThat(issue1).isNotEqualTo(issue2);
        issue1.setId(null);
        assertThat(issue1).isNotEqualTo(issue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueDTO.class);
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setId(1L);
        IssueDTO issueDTO2 = new IssueDTO();
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO2.setId(issueDTO1.getId());
        assertThat(issueDTO1).isEqualTo(issueDTO2);
        issueDTO2.setId(2L);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO1.setId(null);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueMapper.fromId(null)).isNull();
    }
}
