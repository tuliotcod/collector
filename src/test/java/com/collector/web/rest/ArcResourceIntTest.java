package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Arc;
import com.collector.repository.ArcRepository;
import com.collector.repository.search.ArcSearchRepository;
import com.collector.service.ArcService;
import com.collector.service.dto.ArcDTO;
import com.collector.service.mapper.ArcMapper;
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
 * Test class for the ArcResource REST controller.
 *
 * @see ArcResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class ArcResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ArcRepository arcRepository;


    @Autowired
    private ArcMapper arcMapper;
    

    @Autowired
    private ArcService arcService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.ArcSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArcSearchRepository mockArcSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArcMockMvc;

    private Arc arc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArcResource arcResource = new ArcResource(arcService);
        this.restArcMockMvc = MockMvcBuilders.standaloneSetup(arcResource)
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
    public static Arc createEntity(EntityManager em) {
        Arc arc = new Arc()
            .name(DEFAULT_NAME)
            .info(DEFAULT_INFO)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return arc;
    }

    @Before
    public void initTest() {
        arc = createEntity(em);
    }

    @Test
    @Transactional
    public void createArc() throws Exception {
        int databaseSizeBeforeCreate = arcRepository.findAll().size();

        // Create the Arc
        ArcDTO arcDTO = arcMapper.toDto(arc);
        restArcMockMvc.perform(post("/api/arcs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arcDTO)))
            .andExpect(status().isCreated());

        // Validate the Arc in the database
        List<Arc> arcList = arcRepository.findAll();
        assertThat(arcList).hasSize(databaseSizeBeforeCreate + 1);
        Arc testArc = arcList.get(arcList.size() - 1);
        assertThat(testArc.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArc.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testArc.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testArc.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Arc in Elasticsearch
        verify(mockArcSearchRepository, times(1)).save(testArc);
    }

    @Test
    @Transactional
    public void createArcWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = arcRepository.findAll().size();

        // Create the Arc with an existing ID
        arc.setId(1L);
        ArcDTO arcDTO = arcMapper.toDto(arc);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArcMockMvc.perform(post("/api/arcs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arcDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arc in the database
        List<Arc> arcList = arcRepository.findAll();
        assertThat(arcList).hasSize(databaseSizeBeforeCreate);

        // Validate the Arc in Elasticsearch
        verify(mockArcSearchRepository, times(0)).save(arc);
    }

    @Test
    @Transactional
    public void getAllArcs() throws Exception {
        // Initialize the database
        arcRepository.saveAndFlush(arc);

        // Get all the arcList
        restArcMockMvc.perform(get("/api/arcs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arc.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getArc() throws Exception {
        // Initialize the database
        arcRepository.saveAndFlush(arc);

        // Get the arc
        restArcMockMvc.perform(get("/api/arcs/{id}", arc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(arc.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingArc() throws Exception {
        // Get the arc
        restArcMockMvc.perform(get("/api/arcs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArc() throws Exception {
        // Initialize the database
        arcRepository.saveAndFlush(arc);

        int databaseSizeBeforeUpdate = arcRepository.findAll().size();

        // Update the arc
        Arc updatedArc = arcRepository.findById(arc.getId()).get();
        // Disconnect from session so that the updates on updatedArc are not directly saved in db
        em.detach(updatedArc);
        updatedArc
            .name(UPDATED_NAME)
            .info(UPDATED_INFO)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        ArcDTO arcDTO = arcMapper.toDto(updatedArc);

        restArcMockMvc.perform(put("/api/arcs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arcDTO)))
            .andExpect(status().isOk());

        // Validate the Arc in the database
        List<Arc> arcList = arcRepository.findAll();
        assertThat(arcList).hasSize(databaseSizeBeforeUpdate);
        Arc testArc = arcList.get(arcList.size() - 1);
        assertThat(testArc.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArc.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testArc.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testArc.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Arc in Elasticsearch
        verify(mockArcSearchRepository, times(1)).save(testArc);
    }

    @Test
    @Transactional
    public void updateNonExistingArc() throws Exception {
        int databaseSizeBeforeUpdate = arcRepository.findAll().size();

        // Create the Arc
        ArcDTO arcDTO = arcMapper.toDto(arc);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArcMockMvc.perform(put("/api/arcs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arcDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arc in the database
        List<Arc> arcList = arcRepository.findAll();
        assertThat(arcList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Arc in Elasticsearch
        verify(mockArcSearchRepository, times(0)).save(arc);
    }

    @Test
    @Transactional
    public void deleteArc() throws Exception {
        // Initialize the database
        arcRepository.saveAndFlush(arc);

        int databaseSizeBeforeDelete = arcRepository.findAll().size();

        // Get the arc
        restArcMockMvc.perform(delete("/api/arcs/{id}", arc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Arc> arcList = arcRepository.findAll();
        assertThat(arcList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Arc in Elasticsearch
        verify(mockArcSearchRepository, times(1)).deleteById(arc.getId());
    }

    @Test
    @Transactional
    public void searchArc() throws Exception {
        // Initialize the database
        arcRepository.saveAndFlush(arc);
        when(mockArcSearchRepository.search(queryStringQuery("id:" + arc.getId())))
            .thenReturn(Collections.singletonList(arc));
        // Search the arc
        restArcMockMvc.perform(get("/api/_search/arcs?query=id:" + arc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arc.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arc.class);
        Arc arc1 = new Arc();
        arc1.setId(1L);
        Arc arc2 = new Arc();
        arc2.setId(arc1.getId());
        assertThat(arc1).isEqualTo(arc2);
        arc2.setId(2L);
        assertThat(arc1).isNotEqualTo(arc2);
        arc1.setId(null);
        assertThat(arc1).isNotEqualTo(arc2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArcDTO.class);
        ArcDTO arcDTO1 = new ArcDTO();
        arcDTO1.setId(1L);
        ArcDTO arcDTO2 = new ArcDTO();
        assertThat(arcDTO1).isNotEqualTo(arcDTO2);
        arcDTO2.setId(arcDTO1.getId());
        assertThat(arcDTO1).isEqualTo(arcDTO2);
        arcDTO2.setId(2L);
        assertThat(arcDTO1).isNotEqualTo(arcDTO2);
        arcDTO1.setId(null);
        assertThat(arcDTO1).isNotEqualTo(arcDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(arcMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(arcMapper.fromId(null)).isNull();
    }
}
