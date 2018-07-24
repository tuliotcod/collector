package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.IssueInCollection;
import com.collector.repository.IssueInCollectionRepository;
import com.collector.repository.search.IssueInCollectionSearchRepository;
import com.collector.service.IssueInCollectionService;
import com.collector.service.dto.IssueInCollectionDTO;
import com.collector.service.mapper.IssueInCollectionMapper;
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
 * Test class for the IssueInCollectionResource REST controller.
 *
 * @see IssueInCollectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class IssueInCollectionResourceIntTest {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private IssueInCollectionRepository issueInCollectionRepository;


    @Autowired
    private IssueInCollectionMapper issueInCollectionMapper;
    

    @Autowired
    private IssueInCollectionService issueInCollectionService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.IssueInCollectionSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueInCollectionSearchRepository mockIssueInCollectionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueInCollectionMockMvc;

    private IssueInCollection issueInCollection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueInCollectionResource issueInCollectionResource = new IssueInCollectionResource(issueInCollectionService);
        this.restIssueInCollectionMockMvc = MockMvcBuilders.standaloneSetup(issueInCollectionResource)
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
    public static IssueInCollection createEntity(EntityManager em) {
        IssueInCollection issueInCollection = new IssueInCollection()
            .price(DEFAULT_PRICE)
            .amount(DEFAULT_AMOUNT)
            .notes(DEFAULT_NOTES)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return issueInCollection;
    }

    @Before
    public void initTest() {
        issueInCollection = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssueInCollection() throws Exception {
        int databaseSizeBeforeCreate = issueInCollectionRepository.findAll().size();

        // Create the IssueInCollection
        IssueInCollectionDTO issueInCollectionDTO = issueInCollectionMapper.toDto(issueInCollection);
        restIssueInCollectionMockMvc.perform(post("/api/issue-in-collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInCollectionDTO)))
            .andExpect(status().isCreated());

        // Validate the IssueInCollection in the database
        List<IssueInCollection> issueInCollectionList = issueInCollectionRepository.findAll();
        assertThat(issueInCollectionList).hasSize(databaseSizeBeforeCreate + 1);
        IssueInCollection testIssueInCollection = issueInCollectionList.get(issueInCollectionList.size() - 1);
        assertThat(testIssueInCollection.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testIssueInCollection.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testIssueInCollection.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testIssueInCollection.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testIssueInCollection.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the IssueInCollection in Elasticsearch
        verify(mockIssueInCollectionSearchRepository, times(1)).save(testIssueInCollection);
    }

    @Test
    @Transactional
    public void createIssueInCollectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueInCollectionRepository.findAll().size();

        // Create the IssueInCollection with an existing ID
        issueInCollection.setId(1L);
        IssueInCollectionDTO issueInCollectionDTO = issueInCollectionMapper.toDto(issueInCollection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueInCollectionMockMvc.perform(post("/api/issue-in-collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInCollectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueInCollection in the database
        List<IssueInCollection> issueInCollectionList = issueInCollectionRepository.findAll();
        assertThat(issueInCollectionList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssueInCollection in Elasticsearch
        verify(mockIssueInCollectionSearchRepository, times(0)).save(issueInCollection);
    }

    @Test
    @Transactional
    public void getAllIssueInCollections() throws Exception {
        // Initialize the database
        issueInCollectionRepository.saveAndFlush(issueInCollection);

        // Get all the issueInCollectionList
        restIssueInCollectionMockMvc.perform(get("/api/issue-in-collections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueInCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getIssueInCollection() throws Exception {
        // Initialize the database
        issueInCollectionRepository.saveAndFlush(issueInCollection);

        // Get the issueInCollection
        restIssueInCollectionMockMvc.perform(get("/api/issue-in-collections/{id}", issueInCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issueInCollection.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingIssueInCollection() throws Exception {
        // Get the issueInCollection
        restIssueInCollectionMockMvc.perform(get("/api/issue-in-collections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssueInCollection() throws Exception {
        // Initialize the database
        issueInCollectionRepository.saveAndFlush(issueInCollection);

        int databaseSizeBeforeUpdate = issueInCollectionRepository.findAll().size();

        // Update the issueInCollection
        IssueInCollection updatedIssueInCollection = issueInCollectionRepository.findById(issueInCollection.getId()).get();
        // Disconnect from session so that the updates on updatedIssueInCollection are not directly saved in db
        em.detach(updatedIssueInCollection);
        updatedIssueInCollection
            .price(UPDATED_PRICE)
            .amount(UPDATED_AMOUNT)
            .notes(UPDATED_NOTES)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        IssueInCollectionDTO issueInCollectionDTO = issueInCollectionMapper.toDto(updatedIssueInCollection);

        restIssueInCollectionMockMvc.perform(put("/api/issue-in-collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInCollectionDTO)))
            .andExpect(status().isOk());

        // Validate the IssueInCollection in the database
        List<IssueInCollection> issueInCollectionList = issueInCollectionRepository.findAll();
        assertThat(issueInCollectionList).hasSize(databaseSizeBeforeUpdate);
        IssueInCollection testIssueInCollection = issueInCollectionList.get(issueInCollectionList.size() - 1);
        assertThat(testIssueInCollection.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testIssueInCollection.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testIssueInCollection.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testIssueInCollection.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testIssueInCollection.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the IssueInCollection in Elasticsearch
        verify(mockIssueInCollectionSearchRepository, times(1)).save(testIssueInCollection);
    }

    @Test
    @Transactional
    public void updateNonExistingIssueInCollection() throws Exception {
        int databaseSizeBeforeUpdate = issueInCollectionRepository.findAll().size();

        // Create the IssueInCollection
        IssueInCollectionDTO issueInCollectionDTO = issueInCollectionMapper.toDto(issueInCollection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueInCollectionMockMvc.perform(put("/api/issue-in-collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInCollectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueInCollection in the database
        List<IssueInCollection> issueInCollectionList = issueInCollectionRepository.findAll();
        assertThat(issueInCollectionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssueInCollection in Elasticsearch
        verify(mockIssueInCollectionSearchRepository, times(0)).save(issueInCollection);
    }

    @Test
    @Transactional
    public void deleteIssueInCollection() throws Exception {
        // Initialize the database
        issueInCollectionRepository.saveAndFlush(issueInCollection);

        int databaseSizeBeforeDelete = issueInCollectionRepository.findAll().size();

        // Get the issueInCollection
        restIssueInCollectionMockMvc.perform(delete("/api/issue-in-collections/{id}", issueInCollection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IssueInCollection> issueInCollectionList = issueInCollectionRepository.findAll();
        assertThat(issueInCollectionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssueInCollection in Elasticsearch
        verify(mockIssueInCollectionSearchRepository, times(1)).deleteById(issueInCollection.getId());
    }

    @Test
    @Transactional
    public void searchIssueInCollection() throws Exception {
        // Initialize the database
        issueInCollectionRepository.saveAndFlush(issueInCollection);
        when(mockIssueInCollectionSearchRepository.search(queryStringQuery("id:" + issueInCollection.getId())))
            .thenReturn(Collections.singletonList(issueInCollection));
        // Search the issueInCollection
        restIssueInCollectionMockMvc.perform(get("/api/_search/issue-in-collections?query=id:" + issueInCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueInCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueInCollection.class);
        IssueInCollection issueInCollection1 = new IssueInCollection();
        issueInCollection1.setId(1L);
        IssueInCollection issueInCollection2 = new IssueInCollection();
        issueInCollection2.setId(issueInCollection1.getId());
        assertThat(issueInCollection1).isEqualTo(issueInCollection2);
        issueInCollection2.setId(2L);
        assertThat(issueInCollection1).isNotEqualTo(issueInCollection2);
        issueInCollection1.setId(null);
        assertThat(issueInCollection1).isNotEqualTo(issueInCollection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueInCollectionDTO.class);
        IssueInCollectionDTO issueInCollectionDTO1 = new IssueInCollectionDTO();
        issueInCollectionDTO1.setId(1L);
        IssueInCollectionDTO issueInCollectionDTO2 = new IssueInCollectionDTO();
        assertThat(issueInCollectionDTO1).isNotEqualTo(issueInCollectionDTO2);
        issueInCollectionDTO2.setId(issueInCollectionDTO1.getId());
        assertThat(issueInCollectionDTO1).isEqualTo(issueInCollectionDTO2);
        issueInCollectionDTO2.setId(2L);
        assertThat(issueInCollectionDTO1).isNotEqualTo(issueInCollectionDTO2);
        issueInCollectionDTO1.setId(null);
        assertThat(issueInCollectionDTO1).isNotEqualTo(issueInCollectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueInCollectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueInCollectionMapper.fromId(null)).isNull();
    }
}
