package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.History;
import com.collector.repository.HistoryRepository;
import com.collector.repository.search.HistorySearchRepository;
import com.collector.service.HistoryService;
import com.collector.service.dto.HistoryDTO;
import com.collector.service.mapper.HistoryMapper;
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
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class HistoryResourceIntTest {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGES = 1;
    private static final Integer UPDATED_PAGES = 2;

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_PART = 1;
    private static final Integer UPDATED_PART = 2;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private HistoryRepository historyRepository;


    @Autowired
    private HistoryMapper historyMapper;
    

    @Autowired
    private HistoryService historyService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.HistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private HistorySearchRepository mockHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoryMockMvc;

    private History history;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoryResource historyResource = new HistoryResource(historyService);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
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
    public static History createEntity(EntityManager em) {
        History history = new History()
            .order(DEFAULT_ORDER)
            .name(DEFAULT_NAME)
            .pages(DEFAULT_PAGES)
            .desc(DEFAULT_DESC)
            .part(DEFAULT_PART)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return history;
    }

    @Before
    public void initTest() {
        history = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHistory.getPages()).isEqualTo(DEFAULT_PAGES);
        assertThat(testHistory.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testHistory.getPart()).isEqualTo(DEFAULT_PART);
        assertThat(testHistory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testHistory.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).save(testHistory);
    }

    @Test
    @Transactional
    public void createHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History with an existing ID
        history.setId(1L);
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(0)).save(history);
    }

    @Test
    @Transactional
    public void getAllHistories() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].part").value(hasItem(DEFAULT_PART)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pages").value(DEFAULT_PAGES))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.part").value(DEFAULT_PART))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        History updatedHistory = historyRepository.findById(history.getId()).get();
        // Disconnect from session so that the updates on updatedHistory are not directly saved in db
        em.detach(updatedHistory);
        updatedHistory
            .order(UPDATED_ORDER)
            .name(UPDATED_NAME)
            .pages(UPDATED_PAGES)
            .desc(UPDATED_DESC)
            .part(UPDATED_PART)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        HistoryDTO historyDTO = historyMapper.toDto(updatedHistory);

        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHistory.getPages()).isEqualTo(UPDATED_PAGES);
        assertThat(testHistory.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testHistory.getPart()).isEqualTo(UPDATED_PART);
        assertThat(testHistory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testHistory.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).save(testHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(0)).save(history);
    }

    @Test
    @Transactional
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/histories/{id}", history.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the History in Elasticsearch
        verify(mockHistorySearchRepository, times(1)).deleteById(history.getId());
    }

    @Test
    @Transactional
    public void searchHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);
        when(mockHistorySearchRepository.search(queryStringQuery("id:" + history.getId())))
            .thenReturn(Collections.singletonList(history));
        // Search the history
        restHistoryMockMvc.perform(get("/api/_search/histories?query=id:" + history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].part").value(hasItem(DEFAULT_PART)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(History.class);
        History history1 = new History();
        history1.setId(1L);
        History history2 = new History();
        history2.setId(history1.getId());
        assertThat(history1).isEqualTo(history2);
        history2.setId(2L);
        assertThat(history1).isNotEqualTo(history2);
        history1.setId(null);
        assertThat(history1).isNotEqualTo(history2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryDTO.class);
        HistoryDTO historyDTO1 = new HistoryDTO();
        historyDTO1.setId(1L);
        HistoryDTO historyDTO2 = new HistoryDTO();
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO2.setId(historyDTO1.getId());
        assertThat(historyDTO1).isEqualTo(historyDTO2);
        historyDTO2.setId(2L);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO1.setId(null);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historyMapper.fromId(null)).isNull();
    }
}
