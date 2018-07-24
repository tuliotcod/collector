package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Title;
import com.collector.repository.TitleRepository;
import com.collector.repository.search.TitleSearchRepository;
import com.collector.service.TitleService;
import com.collector.service.dto.TitleDTO;
import com.collector.service.mapper.TitleMapper;
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
 * Test class for the TitleResource REST controller.
 *
 * @see TitleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class TitleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TitleRepository titleRepository;


    @Autowired
    private TitleMapper titleMapper;
    

    @Autowired
    private TitleService titleService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.TitleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TitleSearchRepository mockTitleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTitleMockMvc;

    private Title title;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TitleResource titleResource = new TitleResource(titleService);
        this.restTitleMockMvc = MockMvcBuilders.standaloneSetup(titleResource)
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
    public static Title createEntity(EntityManager em) {
        Title title = new Title()
            .name(DEFAULT_NAME)
            .serie(DEFAULT_SERIE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .info(DEFAULT_INFO)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return title;
    }

    @Before
    public void initTest() {
        title = createEntity(em);
    }

    @Test
    @Transactional
    public void createTitle() throws Exception {
        int databaseSizeBeforeCreate = titleRepository.findAll().size();

        // Create the Title
        TitleDTO titleDTO = titleMapper.toDto(title);
        restTitleMockMvc.perform(post("/api/titles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
            .andExpect(status().isCreated());

        // Validate the Title in the database
        List<Title> titleList = titleRepository.findAll();
        assertThat(titleList).hasSize(databaseSizeBeforeCreate + 1);
        Title testTitle = titleList.get(titleList.size() - 1);
        assertThat(testTitle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTitle.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testTitle.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTitle.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTitle.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testTitle.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTitle.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Title in Elasticsearch
        verify(mockTitleSearchRepository, times(1)).save(testTitle);
    }

    @Test
    @Transactional
    public void createTitleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = titleRepository.findAll().size();

        // Create the Title with an existing ID
        title.setId(1L);
        TitleDTO titleDTO = titleMapper.toDto(title);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTitleMockMvc.perform(post("/api/titles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Title in the database
        List<Title> titleList = titleRepository.findAll();
        assertThat(titleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Title in Elasticsearch
        verify(mockTitleSearchRepository, times(0)).save(title);
    }

    @Test
    @Transactional
    public void getAllTitles() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        // Get all the titleList
        restTitleMockMvc.perform(get("/api/titles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(title.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getTitle() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        // Get the title
        restTitleMockMvc.perform(get("/api/titles/{id}", title.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(title.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTitle() throws Exception {
        // Get the title
        restTitleMockMvc.perform(get("/api/titles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTitle() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        int databaseSizeBeforeUpdate = titleRepository.findAll().size();

        // Update the title
        Title updatedTitle = titleRepository.findById(title.getId()).get();
        // Disconnect from session so that the updates on updatedTitle are not directly saved in db
        em.detach(updatedTitle);
        updatedTitle
            .name(UPDATED_NAME)
            .serie(UPDATED_SERIE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .info(UPDATED_INFO)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        TitleDTO titleDTO = titleMapper.toDto(updatedTitle);

        restTitleMockMvc.perform(put("/api/titles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
            .andExpect(status().isOk());

        // Validate the Title in the database
        List<Title> titleList = titleRepository.findAll();
        assertThat(titleList).hasSize(databaseSizeBeforeUpdate);
        Title testTitle = titleList.get(titleList.size() - 1);
        assertThat(testTitle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTitle.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testTitle.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTitle.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTitle.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testTitle.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTitle.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Title in Elasticsearch
        verify(mockTitleSearchRepository, times(1)).save(testTitle);
    }

    @Test
    @Transactional
    public void updateNonExistingTitle() throws Exception {
        int databaseSizeBeforeUpdate = titleRepository.findAll().size();

        // Create the Title
        TitleDTO titleDTO = titleMapper.toDto(title);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTitleMockMvc.perform(put("/api/titles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(titleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Title in the database
        List<Title> titleList = titleRepository.findAll();
        assertThat(titleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Title in Elasticsearch
        verify(mockTitleSearchRepository, times(0)).save(title);
    }

    @Test
    @Transactional
    public void deleteTitle() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);

        int databaseSizeBeforeDelete = titleRepository.findAll().size();

        // Get the title
        restTitleMockMvc.perform(delete("/api/titles/{id}", title.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Title> titleList = titleRepository.findAll();
        assertThat(titleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Title in Elasticsearch
        verify(mockTitleSearchRepository, times(1)).deleteById(title.getId());
    }

    @Test
    @Transactional
    public void searchTitle() throws Exception {
        // Initialize the database
        titleRepository.saveAndFlush(title);
        when(mockTitleSearchRepository.search(queryStringQuery("id:" + title.getId())))
            .thenReturn(Collections.singletonList(title));
        // Search the title
        restTitleMockMvc.perform(get("/api/_search/titles?query=id:" + title.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(title.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Title.class);
        Title title1 = new Title();
        title1.setId(1L);
        Title title2 = new Title();
        title2.setId(title1.getId());
        assertThat(title1).isEqualTo(title2);
        title2.setId(2L);
        assertThat(title1).isNotEqualTo(title2);
        title1.setId(null);
        assertThat(title1).isNotEqualTo(title2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TitleDTO.class);
        TitleDTO titleDTO1 = new TitleDTO();
        titleDTO1.setId(1L);
        TitleDTO titleDTO2 = new TitleDTO();
        assertThat(titleDTO1).isNotEqualTo(titleDTO2);
        titleDTO2.setId(titleDTO1.getId());
        assertThat(titleDTO1).isEqualTo(titleDTO2);
        titleDTO2.setId(2L);
        assertThat(titleDTO1).isNotEqualTo(titleDTO2);
        titleDTO1.setId(null);
        assertThat(titleDTO1).isNotEqualTo(titleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(titleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(titleMapper.fromId(null)).isNull();
    }
}
