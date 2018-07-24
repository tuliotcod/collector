package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Contribution;
import com.collector.repository.ContributionRepository;
import com.collector.repository.search.ContributionSearchRepository;
import com.collector.service.ContributionService;
import com.collector.service.dto.ContributionDTO;
import com.collector.service.mapper.ContributionMapper;
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
 * Test class for the ContributionResource REST controller.
 *
 * @see ContributionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class ContributionResourceIntTest {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ContributionRepository contributionRepository;


    @Autowired
    private ContributionMapper contributionMapper;
    

    @Autowired
    private ContributionService contributionService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.ContributionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContributionSearchRepository mockContributionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContributionMockMvc;

    private Contribution contribution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributionResource contributionResource = new ContributionResource(contributionService);
        this.restContributionMockMvc = MockMvcBuilders.standaloneSetup(contributionResource)
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
    public static Contribution createEntity(EntityManager em) {
        Contribution contribution = new Contribution()
            .creationDate(DEFAULT_CREATION_DATE);
        return contribution;
    }

    @Before
    public void initTest() {
        contribution = createEntity(em);
    }

    @Test
    @Transactional
    public void createContribution() throws Exception {
        int databaseSizeBeforeCreate = contributionRepository.findAll().size();

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);
        restContributionMockMvc.perform(post("/api/contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionDTO)))
            .andExpect(status().isCreated());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeCreate + 1);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the Contribution in Elasticsearch
        verify(mockContributionSearchRepository, times(1)).save(testContribution);
    }

    @Test
    @Transactional
    public void createContributionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributionRepository.findAll().size();

        // Create the Contribution with an existing ID
        contribution.setId(1L);
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionMockMvc.perform(post("/api/contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Contribution in Elasticsearch
        verify(mockContributionSearchRepository, times(0)).save(contribution);
    }

    @Test
    @Transactional
    public void getAllContributions() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get all the contributionList
        restContributionMockMvc.perform(get("/api/contributions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get the contribution
        restContributionMockMvc.perform(get("/api/contributions/{id}", contribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contribution.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingContribution() throws Exception {
        // Get the contribution
        restContributionMockMvc.perform(get("/api/contributions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Update the contribution
        Contribution updatedContribution = contributionRepository.findById(contribution.getId()).get();
        // Disconnect from session so that the updates on updatedContribution are not directly saved in db
        em.detach(updatedContribution);
        updatedContribution
            .creationDate(UPDATED_CREATION_DATE);
        ContributionDTO contributionDTO = contributionMapper.toDto(updatedContribution);

        restContributionMockMvc.perform(put("/api/contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionDTO)))
            .andExpect(status().isOk());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the Contribution in Elasticsearch
        verify(mockContributionSearchRepository, times(1)).save(testContribution);
    }

    @Test
    @Transactional
    public void updateNonExistingContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContributionMockMvc.perform(put("/api/contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Contribution in Elasticsearch
        verify(mockContributionSearchRepository, times(0)).save(contribution);
    }

    @Test
    @Transactional
    public void deleteContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeDelete = contributionRepository.findAll().size();

        // Get the contribution
        restContributionMockMvc.perform(delete("/api/contributions/{id}", contribution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Contribution in Elasticsearch
        verify(mockContributionSearchRepository, times(1)).deleteById(contribution.getId());
    }

    @Test
    @Transactional
    public void searchContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);
        when(mockContributionSearchRepository.search(queryStringQuery("id:" + contribution.getId())))
            .thenReturn(Collections.singletonList(contribution));
        // Search the contribution
        restContributionMockMvc.perform(get("/api/_search/contributions?query=id:" + contribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contribution.class);
        Contribution contribution1 = new Contribution();
        contribution1.setId(1L);
        Contribution contribution2 = new Contribution();
        contribution2.setId(contribution1.getId());
        assertThat(contribution1).isEqualTo(contribution2);
        contribution2.setId(2L);
        assertThat(contribution1).isNotEqualTo(contribution2);
        contribution1.setId(null);
        assertThat(contribution1).isNotEqualTo(contribution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionDTO.class);
        ContributionDTO contributionDTO1 = new ContributionDTO();
        contributionDTO1.setId(1L);
        ContributionDTO contributionDTO2 = new ContributionDTO();
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
        contributionDTO2.setId(contributionDTO1.getId());
        assertThat(contributionDTO1).isEqualTo(contributionDTO2);
        contributionDTO2.setId(2L);
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
        contributionDTO1.setId(null);
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contributionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contributionMapper.fromId(null)).isNull();
    }
}
