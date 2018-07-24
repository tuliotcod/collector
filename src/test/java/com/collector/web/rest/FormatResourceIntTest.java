package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Format;
import com.collector.repository.FormatRepository;
import com.collector.repository.search.FormatSearchRepository;
import com.collector.service.FormatService;
import com.collector.service.dto.FormatDTO;
import com.collector.service.mapper.FormatMapper;
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
 * Test class for the FormatResource REST controller.
 *
 * @see FormatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class FormatResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private FormatRepository formatRepository;


    @Autowired
    private FormatMapper formatMapper;
    

    @Autowired
    private FormatService formatService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.FormatSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormatSearchRepository mockFormatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormatMockMvc;

    private Format format;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormatResource formatResource = new FormatResource(formatService);
        this.restFormatMockMvc = MockMvcBuilders.standaloneSetup(formatResource)
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
    public static Format createEntity(EntityManager em) {
        Format format = new Format()
            .desc(DEFAULT_DESC);
        return format;
    }

    @Before
    public void initTest() {
        format = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormat() throws Exception {
        int databaseSizeBeforeCreate = formatRepository.findAll().size();

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);
        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isCreated());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate + 1);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the Format in Elasticsearch
        verify(mockFormatSearchRepository, times(1)).save(testFormat);
    }

    @Test
    @Transactional
    public void createFormatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formatRepository.findAll().size();

        // Create the Format with an existing ID
        format.setId(1L);
        FormatDTO formatDTO = formatMapper.toDto(format);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate);

        // Validate the Format in Elasticsearch
        verify(mockFormatSearchRepository, times(0)).save(format);
    }

    @Test
    @Transactional
    public void getAllFormats() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList
        restFormatMockMvc.perform(get("/api/formats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get the format
        restFormatMockMvc.perform(get("/api/formats/{id}", format.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(format.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingFormat() throws Exception {
        // Get the format
        restFormatMockMvc.perform(get("/api/formats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Update the format
        Format updatedFormat = formatRepository.findById(format.getId()).get();
        // Disconnect from session so that the updates on updatedFormat are not directly saved in db
        em.detach(updatedFormat);
        updatedFormat
            .desc(UPDATED_DESC);
        FormatDTO formatDTO = formatMapper.toDto(updatedFormat);

        restFormatMockMvc.perform(put("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isOk());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the Format in Elasticsearch
        verify(mockFormatSearchRepository, times(1)).save(testFormat);
    }

    @Test
    @Transactional
    public void updateNonExistingFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormatMockMvc.perform(put("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Format in Elasticsearch
        verify(mockFormatSearchRepository, times(0)).save(format);
    }

    @Test
    @Transactional
    public void deleteFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeDelete = formatRepository.findAll().size();

        // Get the format
        restFormatMockMvc.perform(delete("/api/formats/{id}", format.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Format in Elasticsearch
        verify(mockFormatSearchRepository, times(1)).deleteById(format.getId());
    }

    @Test
    @Transactional
    public void searchFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);
        when(mockFormatSearchRepository.search(queryStringQuery("id:" + format.getId())))
            .thenReturn(Collections.singletonList(format));
        // Search the format
        restFormatMockMvc.perform(get("/api/_search/formats?query=id:" + format.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Format.class);
        Format format1 = new Format();
        format1.setId(1L);
        Format format2 = new Format();
        format2.setId(format1.getId());
        assertThat(format1).isEqualTo(format2);
        format2.setId(2L);
        assertThat(format1).isNotEqualTo(format2);
        format1.setId(null);
        assertThat(format1).isNotEqualTo(format2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormatDTO.class);
        FormatDTO formatDTO1 = new FormatDTO();
        formatDTO1.setId(1L);
        FormatDTO formatDTO2 = new FormatDTO();
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO2.setId(formatDTO1.getId());
        assertThat(formatDTO1).isEqualTo(formatDTO2);
        formatDTO2.setId(2L);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO1.setId(null);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(formatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(formatMapper.fromId(null)).isNull();
    }
}
