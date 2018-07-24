package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.IssueInWishlist;
import com.collector.repository.IssueInWishlistRepository;
import com.collector.repository.search.IssueInWishlistSearchRepository;
import com.collector.service.IssueInWishlistService;
import com.collector.service.dto.IssueInWishlistDTO;
import com.collector.service.mapper.IssueInWishlistMapper;
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
 * Test class for the IssueInWishlistResource REST controller.
 *
 * @see IssueInWishlistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class IssueInWishlistResourceIntTest {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private IssueInWishlistRepository issueInWishlistRepository;


    @Autowired
    private IssueInWishlistMapper issueInWishlistMapper;
    

    @Autowired
    private IssueInWishlistService issueInWishlistService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.IssueInWishlistSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueInWishlistSearchRepository mockIssueInWishlistSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueInWishlistMockMvc;

    private IssueInWishlist issueInWishlist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueInWishlistResource issueInWishlistResource = new IssueInWishlistResource(issueInWishlistService);
        this.restIssueInWishlistMockMvc = MockMvcBuilders.standaloneSetup(issueInWishlistResource)
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
    public static IssueInWishlist createEntity(EntityManager em) {
        IssueInWishlist issueInWishlist = new IssueInWishlist()
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return issueInWishlist;
    }

    @Before
    public void initTest() {
        issueInWishlist = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssueInWishlist() throws Exception {
        int databaseSizeBeforeCreate = issueInWishlistRepository.findAll().size();

        // Create the IssueInWishlist
        IssueInWishlistDTO issueInWishlistDTO = issueInWishlistMapper.toDto(issueInWishlist);
        restIssueInWishlistMockMvc.perform(post("/api/issue-in-wishlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInWishlistDTO)))
            .andExpect(status().isCreated());

        // Validate the IssueInWishlist in the database
        List<IssueInWishlist> issueInWishlistList = issueInWishlistRepository.findAll();
        assertThat(issueInWishlistList).hasSize(databaseSizeBeforeCreate + 1);
        IssueInWishlist testIssueInWishlist = issueInWishlistList.get(issueInWishlistList.size() - 1);
        assertThat(testIssueInWishlist.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testIssueInWishlist.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the IssueInWishlist in Elasticsearch
        verify(mockIssueInWishlistSearchRepository, times(1)).save(testIssueInWishlist);
    }

    @Test
    @Transactional
    public void createIssueInWishlistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueInWishlistRepository.findAll().size();

        // Create the IssueInWishlist with an existing ID
        issueInWishlist.setId(1L);
        IssueInWishlistDTO issueInWishlistDTO = issueInWishlistMapper.toDto(issueInWishlist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueInWishlistMockMvc.perform(post("/api/issue-in-wishlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInWishlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueInWishlist in the database
        List<IssueInWishlist> issueInWishlistList = issueInWishlistRepository.findAll();
        assertThat(issueInWishlistList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssueInWishlist in Elasticsearch
        verify(mockIssueInWishlistSearchRepository, times(0)).save(issueInWishlist);
    }

    @Test
    @Transactional
    public void getAllIssueInWishlists() throws Exception {
        // Initialize the database
        issueInWishlistRepository.saveAndFlush(issueInWishlist);

        // Get all the issueInWishlistList
        restIssueInWishlistMockMvc.perform(get("/api/issue-in-wishlists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueInWishlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getIssueInWishlist() throws Exception {
        // Initialize the database
        issueInWishlistRepository.saveAndFlush(issueInWishlist);

        // Get the issueInWishlist
        restIssueInWishlistMockMvc.perform(get("/api/issue-in-wishlists/{id}", issueInWishlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issueInWishlist.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingIssueInWishlist() throws Exception {
        // Get the issueInWishlist
        restIssueInWishlistMockMvc.perform(get("/api/issue-in-wishlists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssueInWishlist() throws Exception {
        // Initialize the database
        issueInWishlistRepository.saveAndFlush(issueInWishlist);

        int databaseSizeBeforeUpdate = issueInWishlistRepository.findAll().size();

        // Update the issueInWishlist
        IssueInWishlist updatedIssueInWishlist = issueInWishlistRepository.findById(issueInWishlist.getId()).get();
        // Disconnect from session so that the updates on updatedIssueInWishlist are not directly saved in db
        em.detach(updatedIssueInWishlist);
        updatedIssueInWishlist
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        IssueInWishlistDTO issueInWishlistDTO = issueInWishlistMapper.toDto(updatedIssueInWishlist);

        restIssueInWishlistMockMvc.perform(put("/api/issue-in-wishlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInWishlistDTO)))
            .andExpect(status().isOk());

        // Validate the IssueInWishlist in the database
        List<IssueInWishlist> issueInWishlistList = issueInWishlistRepository.findAll();
        assertThat(issueInWishlistList).hasSize(databaseSizeBeforeUpdate);
        IssueInWishlist testIssueInWishlist = issueInWishlistList.get(issueInWishlistList.size() - 1);
        assertThat(testIssueInWishlist.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testIssueInWishlist.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the IssueInWishlist in Elasticsearch
        verify(mockIssueInWishlistSearchRepository, times(1)).save(testIssueInWishlist);
    }

    @Test
    @Transactional
    public void updateNonExistingIssueInWishlist() throws Exception {
        int databaseSizeBeforeUpdate = issueInWishlistRepository.findAll().size();

        // Create the IssueInWishlist
        IssueInWishlistDTO issueInWishlistDTO = issueInWishlistMapper.toDto(issueInWishlist);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssueInWishlistMockMvc.perform(put("/api/issue-in-wishlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueInWishlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueInWishlist in the database
        List<IssueInWishlist> issueInWishlistList = issueInWishlistRepository.findAll();
        assertThat(issueInWishlistList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssueInWishlist in Elasticsearch
        verify(mockIssueInWishlistSearchRepository, times(0)).save(issueInWishlist);
    }

    @Test
    @Transactional
    public void deleteIssueInWishlist() throws Exception {
        // Initialize the database
        issueInWishlistRepository.saveAndFlush(issueInWishlist);

        int databaseSizeBeforeDelete = issueInWishlistRepository.findAll().size();

        // Get the issueInWishlist
        restIssueInWishlistMockMvc.perform(delete("/api/issue-in-wishlists/{id}", issueInWishlist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IssueInWishlist> issueInWishlistList = issueInWishlistRepository.findAll();
        assertThat(issueInWishlistList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssueInWishlist in Elasticsearch
        verify(mockIssueInWishlistSearchRepository, times(1)).deleteById(issueInWishlist.getId());
    }

    @Test
    @Transactional
    public void searchIssueInWishlist() throws Exception {
        // Initialize the database
        issueInWishlistRepository.saveAndFlush(issueInWishlist);
        when(mockIssueInWishlistSearchRepository.search(queryStringQuery("id:" + issueInWishlist.getId())))
            .thenReturn(Collections.singletonList(issueInWishlist));
        // Search the issueInWishlist
        restIssueInWishlistMockMvc.perform(get("/api/_search/issue-in-wishlists?query=id:" + issueInWishlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueInWishlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueInWishlist.class);
        IssueInWishlist issueInWishlist1 = new IssueInWishlist();
        issueInWishlist1.setId(1L);
        IssueInWishlist issueInWishlist2 = new IssueInWishlist();
        issueInWishlist2.setId(issueInWishlist1.getId());
        assertThat(issueInWishlist1).isEqualTo(issueInWishlist2);
        issueInWishlist2.setId(2L);
        assertThat(issueInWishlist1).isNotEqualTo(issueInWishlist2);
        issueInWishlist1.setId(null);
        assertThat(issueInWishlist1).isNotEqualTo(issueInWishlist2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueInWishlistDTO.class);
        IssueInWishlistDTO issueInWishlistDTO1 = new IssueInWishlistDTO();
        issueInWishlistDTO1.setId(1L);
        IssueInWishlistDTO issueInWishlistDTO2 = new IssueInWishlistDTO();
        assertThat(issueInWishlistDTO1).isNotEqualTo(issueInWishlistDTO2);
        issueInWishlistDTO2.setId(issueInWishlistDTO1.getId());
        assertThat(issueInWishlistDTO1).isEqualTo(issueInWishlistDTO2);
        issueInWishlistDTO2.setId(2L);
        assertThat(issueInWishlistDTO1).isNotEqualTo(issueInWishlistDTO2);
        issueInWishlistDTO1.setId(null);
        assertThat(issueInWishlistDTO1).isNotEqualTo(issueInWishlistDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueInWishlistMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueInWishlistMapper.fromId(null)).isNull();
    }
}
