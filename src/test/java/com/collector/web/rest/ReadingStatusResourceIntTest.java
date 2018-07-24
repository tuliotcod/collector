package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.ReadingStatus;
import com.collector.repository.ReadingStatusRepository;
import com.collector.repository.search.ReadingStatusSearchRepository;
import com.collector.service.ReadingStatusService;
import com.collector.service.dto.ReadingStatusDTO;
import com.collector.service.mapper.ReadingStatusMapper;
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
 * Test class for the ReadingStatusResource REST controller.
 *
 * @see ReadingStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class ReadingStatusResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private ReadingStatusRepository readingStatusRepository;


    @Autowired
    private ReadingStatusMapper readingStatusMapper;
    

    @Autowired
    private ReadingStatusService readingStatusService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.ReadingStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReadingStatusSearchRepository mockReadingStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReadingStatusMockMvc;

    private ReadingStatus readingStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReadingStatusResource readingStatusResource = new ReadingStatusResource(readingStatusService);
        this.restReadingStatusMockMvc = MockMvcBuilders.standaloneSetup(readingStatusResource)
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
    public static ReadingStatus createEntity(EntityManager em) {
        ReadingStatus readingStatus = new ReadingStatus()
            .desc(DEFAULT_DESC);
        return readingStatus;
    }

    @Before
    public void initTest() {
        readingStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createReadingStatus() throws Exception {
        int databaseSizeBeforeCreate = readingStatusRepository.findAll().size();

        // Create the ReadingStatus
        ReadingStatusDTO readingStatusDTO = readingStatusMapper.toDto(readingStatus);
        restReadingStatusMockMvc.perform(post("/api/reading-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readingStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the ReadingStatus in the database
        List<ReadingStatus> readingStatusList = readingStatusRepository.findAll();
        assertThat(readingStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ReadingStatus testReadingStatus = readingStatusList.get(readingStatusList.size() - 1);
        assertThat(testReadingStatus.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the ReadingStatus in Elasticsearch
        verify(mockReadingStatusSearchRepository, times(1)).save(testReadingStatus);
    }

    @Test
    @Transactional
    public void createReadingStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = readingStatusRepository.findAll().size();

        // Create the ReadingStatus with an existing ID
        readingStatus.setId(1L);
        ReadingStatusDTO readingStatusDTO = readingStatusMapper.toDto(readingStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReadingStatusMockMvc.perform(post("/api/reading-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readingStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReadingStatus in the database
        List<ReadingStatus> readingStatusList = readingStatusRepository.findAll();
        assertThat(readingStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReadingStatus in Elasticsearch
        verify(mockReadingStatusSearchRepository, times(0)).save(readingStatus);
    }

    @Test
    @Transactional
    public void getAllReadingStatuses() throws Exception {
        // Initialize the database
        readingStatusRepository.saveAndFlush(readingStatus);

        // Get all the readingStatusList
        restReadingStatusMockMvc.perform(get("/api/reading-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getReadingStatus() throws Exception {
        // Initialize the database
        readingStatusRepository.saveAndFlush(readingStatus);

        // Get the readingStatus
        restReadingStatusMockMvc.perform(get("/api/reading-statuses/{id}", readingStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(readingStatus.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingReadingStatus() throws Exception {
        // Get the readingStatus
        restReadingStatusMockMvc.perform(get("/api/reading-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReadingStatus() throws Exception {
        // Initialize the database
        readingStatusRepository.saveAndFlush(readingStatus);

        int databaseSizeBeforeUpdate = readingStatusRepository.findAll().size();

        // Update the readingStatus
        ReadingStatus updatedReadingStatus = readingStatusRepository.findById(readingStatus.getId()).get();
        // Disconnect from session so that the updates on updatedReadingStatus are not directly saved in db
        em.detach(updatedReadingStatus);
        updatedReadingStatus
            .desc(UPDATED_DESC);
        ReadingStatusDTO readingStatusDTO = readingStatusMapper.toDto(updatedReadingStatus);

        restReadingStatusMockMvc.perform(put("/api/reading-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readingStatusDTO)))
            .andExpect(status().isOk());

        // Validate the ReadingStatus in the database
        List<ReadingStatus> readingStatusList = readingStatusRepository.findAll();
        assertThat(readingStatusList).hasSize(databaseSizeBeforeUpdate);
        ReadingStatus testReadingStatus = readingStatusList.get(readingStatusList.size() - 1);
        assertThat(testReadingStatus.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the ReadingStatus in Elasticsearch
        verify(mockReadingStatusSearchRepository, times(1)).save(testReadingStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingReadingStatus() throws Exception {
        int databaseSizeBeforeUpdate = readingStatusRepository.findAll().size();

        // Create the ReadingStatus
        ReadingStatusDTO readingStatusDTO = readingStatusMapper.toDto(readingStatus);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReadingStatusMockMvc.perform(put("/api/reading-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readingStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReadingStatus in the database
        List<ReadingStatus> readingStatusList = readingStatusRepository.findAll();
        assertThat(readingStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReadingStatus in Elasticsearch
        verify(mockReadingStatusSearchRepository, times(0)).save(readingStatus);
    }

    @Test
    @Transactional
    public void deleteReadingStatus() throws Exception {
        // Initialize the database
        readingStatusRepository.saveAndFlush(readingStatus);

        int databaseSizeBeforeDelete = readingStatusRepository.findAll().size();

        // Get the readingStatus
        restReadingStatusMockMvc.perform(delete("/api/reading-statuses/{id}", readingStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReadingStatus> readingStatusList = readingStatusRepository.findAll();
        assertThat(readingStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReadingStatus in Elasticsearch
        verify(mockReadingStatusSearchRepository, times(1)).deleteById(readingStatus.getId());
    }

    @Test
    @Transactional
    public void searchReadingStatus() throws Exception {
        // Initialize the database
        readingStatusRepository.saveAndFlush(readingStatus);
        when(mockReadingStatusSearchRepository.search(queryStringQuery("id:" + readingStatus.getId())))
            .thenReturn(Collections.singletonList(readingStatus));
        // Search the readingStatus
        restReadingStatusMockMvc.perform(get("/api/_search/reading-statuses?query=id:" + readingStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadingStatus.class);
        ReadingStatus readingStatus1 = new ReadingStatus();
        readingStatus1.setId(1L);
        ReadingStatus readingStatus2 = new ReadingStatus();
        readingStatus2.setId(readingStatus1.getId());
        assertThat(readingStatus1).isEqualTo(readingStatus2);
        readingStatus2.setId(2L);
        assertThat(readingStatus1).isNotEqualTo(readingStatus2);
        readingStatus1.setId(null);
        assertThat(readingStatus1).isNotEqualTo(readingStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadingStatusDTO.class);
        ReadingStatusDTO readingStatusDTO1 = new ReadingStatusDTO();
        readingStatusDTO1.setId(1L);
        ReadingStatusDTO readingStatusDTO2 = new ReadingStatusDTO();
        assertThat(readingStatusDTO1).isNotEqualTo(readingStatusDTO2);
        readingStatusDTO2.setId(readingStatusDTO1.getId());
        assertThat(readingStatusDTO1).isEqualTo(readingStatusDTO2);
        readingStatusDTO2.setId(2L);
        assertThat(readingStatusDTO1).isNotEqualTo(readingStatusDTO2);
        readingStatusDTO1.setId(null);
        assertThat(readingStatusDTO1).isNotEqualTo(readingStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(readingStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(readingStatusMapper.fromId(null)).isNull();
    }
}
