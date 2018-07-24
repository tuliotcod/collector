package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Finishing;
import com.collector.repository.FinishingRepository;
import com.collector.repository.search.FinishingSearchRepository;
import com.collector.service.FinishingService;
import com.collector.service.dto.FinishingDTO;
import com.collector.service.mapper.FinishingMapper;
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
 * Test class for the FinishingResource REST controller.
 *
 * @see FinishingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class FinishingResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private FinishingRepository finishingRepository;


    @Autowired
    private FinishingMapper finishingMapper;
    

    @Autowired
    private FinishingService finishingService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.FinishingSearchRepositoryMockConfiguration
     */
    @Autowired
    private FinishingSearchRepository mockFinishingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFinishingMockMvc;

    private Finishing finishing;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FinishingResource finishingResource = new FinishingResource(finishingService);
        this.restFinishingMockMvc = MockMvcBuilders.standaloneSetup(finishingResource)
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
    public static Finishing createEntity(EntityManager em) {
        Finishing finishing = new Finishing()
            .desc(DEFAULT_DESC);
        return finishing;
    }

    @Before
    public void initTest() {
        finishing = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinishing() throws Exception {
        int databaseSizeBeforeCreate = finishingRepository.findAll().size();

        // Create the Finishing
        FinishingDTO finishingDTO = finishingMapper.toDto(finishing);
        restFinishingMockMvc.perform(post("/api/finishings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishingDTO)))
            .andExpect(status().isCreated());

        // Validate the Finishing in the database
        List<Finishing> finishingList = finishingRepository.findAll();
        assertThat(finishingList).hasSize(databaseSizeBeforeCreate + 1);
        Finishing testFinishing = finishingList.get(finishingList.size() - 1);
        assertThat(testFinishing.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the Finishing in Elasticsearch
        verify(mockFinishingSearchRepository, times(1)).save(testFinishing);
    }

    @Test
    @Transactional
    public void createFinishingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = finishingRepository.findAll().size();

        // Create the Finishing with an existing ID
        finishing.setId(1L);
        FinishingDTO finishingDTO = finishingMapper.toDto(finishing);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinishingMockMvc.perform(post("/api/finishings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Finishing in the database
        List<Finishing> finishingList = finishingRepository.findAll();
        assertThat(finishingList).hasSize(databaseSizeBeforeCreate);

        // Validate the Finishing in Elasticsearch
        verify(mockFinishingSearchRepository, times(0)).save(finishing);
    }

    @Test
    @Transactional
    public void getAllFinishings() throws Exception {
        // Initialize the database
        finishingRepository.saveAndFlush(finishing);

        // Get all the finishingList
        restFinishingMockMvc.perform(get("/api/finishings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishing.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getFinishing() throws Exception {
        // Initialize the database
        finishingRepository.saveAndFlush(finishing);

        // Get the finishing
        restFinishingMockMvc.perform(get("/api/finishings/{id}", finishing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finishing.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFinishing() throws Exception {
        // Get the finishing
        restFinishingMockMvc.perform(get("/api/finishings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinishing() throws Exception {
        // Initialize the database
        finishingRepository.saveAndFlush(finishing);

        int databaseSizeBeforeUpdate = finishingRepository.findAll().size();

        // Update the finishing
        Finishing updatedFinishing = finishingRepository.findById(finishing.getId()).get();
        // Disconnect from session so that the updates on updatedFinishing are not directly saved in db
        em.detach(updatedFinishing);
        updatedFinishing
            .desc(UPDATED_DESC);
        FinishingDTO finishingDTO = finishingMapper.toDto(updatedFinishing);

        restFinishingMockMvc.perform(put("/api/finishings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishingDTO)))
            .andExpect(status().isOk());

        // Validate the Finishing in the database
        List<Finishing> finishingList = finishingRepository.findAll();
        assertThat(finishingList).hasSize(databaseSizeBeforeUpdate);
        Finishing testFinishing = finishingList.get(finishingList.size() - 1);
        assertThat(testFinishing.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the Finishing in Elasticsearch
        verify(mockFinishingSearchRepository, times(1)).save(testFinishing);
    }

    @Test
    @Transactional
    public void updateNonExistingFinishing() throws Exception {
        int databaseSizeBeforeUpdate = finishingRepository.findAll().size();

        // Create the Finishing
        FinishingDTO finishingDTO = finishingMapper.toDto(finishing);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFinishingMockMvc.perform(put("/api/finishings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Finishing in the database
        List<Finishing> finishingList = finishingRepository.findAll();
        assertThat(finishingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Finishing in Elasticsearch
        verify(mockFinishingSearchRepository, times(0)).save(finishing);
    }

    @Test
    @Transactional
    public void deleteFinishing() throws Exception {
        // Initialize the database
        finishingRepository.saveAndFlush(finishing);

        int databaseSizeBeforeDelete = finishingRepository.findAll().size();

        // Get the finishing
        restFinishingMockMvc.perform(delete("/api/finishings/{id}", finishing.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Finishing> finishingList = finishingRepository.findAll();
        assertThat(finishingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Finishing in Elasticsearch
        verify(mockFinishingSearchRepository, times(1)).deleteById(finishing.getId());
    }

    @Test
    @Transactional
    public void searchFinishing() throws Exception {
        // Initialize the database
        finishingRepository.saveAndFlush(finishing);
        when(mockFinishingSearchRepository.search(queryStringQuery("id:" + finishing.getId())))
            .thenReturn(Collections.singletonList(finishing));
        // Search the finishing
        restFinishingMockMvc.perform(get("/api/_search/finishings?query=id:" + finishing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishing.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Finishing.class);
        Finishing finishing1 = new Finishing();
        finishing1.setId(1L);
        Finishing finishing2 = new Finishing();
        finishing2.setId(finishing1.getId());
        assertThat(finishing1).isEqualTo(finishing2);
        finishing2.setId(2L);
        assertThat(finishing1).isNotEqualTo(finishing2);
        finishing1.setId(null);
        assertThat(finishing1).isNotEqualTo(finishing2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishingDTO.class);
        FinishingDTO finishingDTO1 = new FinishingDTO();
        finishingDTO1.setId(1L);
        FinishingDTO finishingDTO2 = new FinishingDTO();
        assertThat(finishingDTO1).isNotEqualTo(finishingDTO2);
        finishingDTO2.setId(finishingDTO1.getId());
        assertThat(finishingDTO1).isEqualTo(finishingDTO2);
        finishingDTO2.setId(2L);
        assertThat(finishingDTO1).isNotEqualTo(finishingDTO2);
        finishingDTO1.setId(null);
        assertThat(finishingDTO1).isNotEqualTo(finishingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(finishingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(finishingMapper.fromId(null)).isNull();
    }
}
