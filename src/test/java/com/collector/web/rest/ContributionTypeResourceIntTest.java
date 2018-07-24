package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.ContributionType;
import com.collector.repository.ContributionTypeRepository;
import com.collector.repository.search.ContributionTypeSearchRepository;
import com.collector.service.ContributionTypeService;
import com.collector.service.dto.ContributionTypeDTO;
import com.collector.service.mapper.ContributionTypeMapper;
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
 * Test class for the ContributionTypeResource REST controller.
 *
 * @see ContributionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class ContributionTypeResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private ContributionTypeRepository contributionTypeRepository;


    @Autowired
    private ContributionTypeMapper contributionTypeMapper;
    

    @Autowired
    private ContributionTypeService contributionTypeService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.ContributionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContributionTypeSearchRepository mockContributionTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContributionTypeMockMvc;

    private ContributionType contributionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributionTypeResource contributionTypeResource = new ContributionTypeResource(contributionTypeService);
        this.restContributionTypeMockMvc = MockMvcBuilders.standaloneSetup(contributionTypeResource)
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
    public static ContributionType createEntity(EntityManager em) {
        ContributionType contributionType = new ContributionType()
            .desc(DEFAULT_DESC);
        return contributionType;
    }

    @Before
    public void initTest() {
        contributionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContributionType() throws Exception {
        int databaseSizeBeforeCreate = contributionTypeRepository.findAll().size();

        // Create the ContributionType
        ContributionTypeDTO contributionTypeDTO = contributionTypeMapper.toDto(contributionType);
        restContributionTypeMockMvc.perform(post("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContributionType testContributionType = contributionTypeList.get(contributionTypeList.size() - 1);
        assertThat(testContributionType.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the ContributionType in Elasticsearch
        verify(mockContributionTypeSearchRepository, times(1)).save(testContributionType);
    }

    @Test
    @Transactional
    public void createContributionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributionTypeRepository.findAll().size();

        // Create the ContributionType with an existing ID
        contributionType.setId(1L);
        ContributionTypeDTO contributionTypeDTO = contributionTypeMapper.toDto(contributionType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionTypeMockMvc.perform(post("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContributionType in Elasticsearch
        verify(mockContributionTypeSearchRepository, times(0)).save(contributionType);
    }

    @Test
    @Transactional
    public void getAllContributionTypes() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList
        restContributionTypeMockMvc.perform(get("/api/contribution-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getContributionType() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get the contributionType
        restContributionTypeMockMvc.perform(get("/api/contribution-types/{id}", contributionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contributionType.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingContributionType() throws Exception {
        // Get the contributionType
        restContributionTypeMockMvc.perform(get("/api/contribution-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContributionType() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        int databaseSizeBeforeUpdate = contributionTypeRepository.findAll().size();

        // Update the contributionType
        ContributionType updatedContributionType = contributionTypeRepository.findById(contributionType.getId()).get();
        // Disconnect from session so that the updates on updatedContributionType are not directly saved in db
        em.detach(updatedContributionType);
        updatedContributionType
            .desc(UPDATED_DESC);
        ContributionTypeDTO contributionTypeDTO = contributionTypeMapper.toDto(updatedContributionType);

        restContributionTypeMockMvc.perform(put("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionTypeDTO)))
            .andExpect(status().isOk());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeUpdate);
        ContributionType testContributionType = contributionTypeList.get(contributionTypeList.size() - 1);
        assertThat(testContributionType.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the ContributionType in Elasticsearch
        verify(mockContributionTypeSearchRepository, times(1)).save(testContributionType);
    }

    @Test
    @Transactional
    public void updateNonExistingContributionType() throws Exception {
        int databaseSizeBeforeUpdate = contributionTypeRepository.findAll().size();

        // Create the ContributionType
        ContributionTypeDTO contributionTypeDTO = contributionTypeMapper.toDto(contributionType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContributionTypeMockMvc.perform(put("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContributionType in Elasticsearch
        verify(mockContributionTypeSearchRepository, times(0)).save(contributionType);
    }

    @Test
    @Transactional
    public void deleteContributionType() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        int databaseSizeBeforeDelete = contributionTypeRepository.findAll().size();

        // Get the contributionType
        restContributionTypeMockMvc.perform(delete("/api/contribution-types/{id}", contributionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContributionType in Elasticsearch
        verify(mockContributionTypeSearchRepository, times(1)).deleteById(contributionType.getId());
    }

    @Test
    @Transactional
    public void searchContributionType() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);
        when(mockContributionTypeSearchRepository.search(queryStringQuery("id:" + contributionType.getId())))
            .thenReturn(Collections.singletonList(contributionType));
        // Search the contributionType
        restContributionTypeMockMvc.perform(get("/api/_search/contribution-types?query=id:" + contributionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionType.class);
        ContributionType contributionType1 = new ContributionType();
        contributionType1.setId(1L);
        ContributionType contributionType2 = new ContributionType();
        contributionType2.setId(contributionType1.getId());
        assertThat(contributionType1).isEqualTo(contributionType2);
        contributionType2.setId(2L);
        assertThat(contributionType1).isNotEqualTo(contributionType2);
        contributionType1.setId(null);
        assertThat(contributionType1).isNotEqualTo(contributionType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionTypeDTO.class);
        ContributionTypeDTO contributionTypeDTO1 = new ContributionTypeDTO();
        contributionTypeDTO1.setId(1L);
        ContributionTypeDTO contributionTypeDTO2 = new ContributionTypeDTO();
        assertThat(contributionTypeDTO1).isNotEqualTo(contributionTypeDTO2);
        contributionTypeDTO2.setId(contributionTypeDTO1.getId());
        assertThat(contributionTypeDTO1).isEqualTo(contributionTypeDTO2);
        contributionTypeDTO2.setId(2L);
        assertThat(contributionTypeDTO1).isNotEqualTo(contributionTypeDTO2);
        contributionTypeDTO1.setId(null);
        assertThat(contributionTypeDTO1).isNotEqualTo(contributionTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contributionTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contributionTypeMapper.fromId(null)).isNull();
    }
}
