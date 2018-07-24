package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Licensor;
import com.collector.repository.LicensorRepository;
import com.collector.repository.search.LicensorSearchRepository;
import com.collector.service.LicensorService;
import com.collector.service.dto.LicensorDTO;
import com.collector.service.mapper.LicensorMapper;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the LicensorResource REST controller.
 *
 * @see LicensorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class LicensorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private LicensorRepository licensorRepository;


    @Autowired
    private LicensorMapper licensorMapper;
    

    @Autowired
    private LicensorService licensorService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.LicensorSearchRepositoryMockConfiguration
     */
    @Autowired
    private LicensorSearchRepository mockLicensorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLicensorMockMvc;

    private Licensor licensor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LicensorResource licensorResource = new LicensorResource(licensorService);
        this.restLicensorMockMvc = MockMvcBuilders.standaloneSetup(licensorResource)
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
    public static Licensor createEntity(EntityManager em) {
        Licensor licensor = new Licensor()
            .name(DEFAULT_NAME)
            .website(DEFAULT_WEBSITE)
            .info(DEFAULT_INFO)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return licensor;
    }

    @Before
    public void initTest() {
        licensor = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicensor() throws Exception {
        int databaseSizeBeforeCreate = licensorRepository.findAll().size();

        // Create the Licensor
        LicensorDTO licensorDTO = licensorMapper.toDto(licensor);
        restLicensorMockMvc.perform(post("/api/licensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licensorDTO)))
            .andExpect(status().isCreated());

        // Validate the Licensor in the database
        List<Licensor> licensorList = licensorRepository.findAll();
        assertThat(licensorList).hasSize(databaseSizeBeforeCreate + 1);
        Licensor testLicensor = licensorList.get(licensorList.size() - 1);
        assertThat(testLicensor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLicensor.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testLicensor.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testLicensor.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLicensor.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testLicensor.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testLicensor.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Licensor in Elasticsearch
        verify(mockLicensorSearchRepository, times(1)).save(testLicensor);
    }

    @Test
    @Transactional
    public void createLicensorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = licensorRepository.findAll().size();

        // Create the Licensor with an existing ID
        licensor.setId(1L);
        LicensorDTO licensorDTO = licensorMapper.toDto(licensor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicensorMockMvc.perform(post("/api/licensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Licensor in the database
        List<Licensor> licensorList = licensorRepository.findAll();
        assertThat(licensorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Licensor in Elasticsearch
        verify(mockLicensorSearchRepository, times(0)).save(licensor);
    }

    @Test
    @Transactional
    public void getAllLicensors() throws Exception {
        // Initialize the database
        licensorRepository.saveAndFlush(licensor);

        // Get all the licensorList
        restLicensorMockMvc.perform(get("/api/licensors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getLicensor() throws Exception {
        // Initialize the database
        licensorRepository.saveAndFlush(licensor);

        // Get the licensor
        restLicensorMockMvc.perform(get("/api/licensors/{id}", licensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(licensor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLicensor() throws Exception {
        // Get the licensor
        restLicensorMockMvc.perform(get("/api/licensors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicensor() throws Exception {
        // Initialize the database
        licensorRepository.saveAndFlush(licensor);

        int databaseSizeBeforeUpdate = licensorRepository.findAll().size();

        // Update the licensor
        Licensor updatedLicensor = licensorRepository.findById(licensor.getId()).get();
        // Disconnect from session so that the updates on updatedLicensor are not directly saved in db
        em.detach(updatedLicensor);
        updatedLicensor
            .name(UPDATED_NAME)
            .website(UPDATED_WEBSITE)
            .info(UPDATED_INFO)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        LicensorDTO licensorDTO = licensorMapper.toDto(updatedLicensor);

        restLicensorMockMvc.perform(put("/api/licensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licensorDTO)))
            .andExpect(status().isOk());

        // Validate the Licensor in the database
        List<Licensor> licensorList = licensorRepository.findAll();
        assertThat(licensorList).hasSize(databaseSizeBeforeUpdate);
        Licensor testLicensor = licensorList.get(licensorList.size() - 1);
        assertThat(testLicensor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLicensor.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testLicensor.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testLicensor.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLicensor.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testLicensor.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testLicensor.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Licensor in Elasticsearch
        verify(mockLicensorSearchRepository, times(1)).save(testLicensor);
    }

    @Test
    @Transactional
    public void updateNonExistingLicensor() throws Exception {
        int databaseSizeBeforeUpdate = licensorRepository.findAll().size();

        // Create the Licensor
        LicensorDTO licensorDTO = licensorMapper.toDto(licensor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLicensorMockMvc.perform(put("/api/licensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Licensor in the database
        List<Licensor> licensorList = licensorRepository.findAll();
        assertThat(licensorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Licensor in Elasticsearch
        verify(mockLicensorSearchRepository, times(0)).save(licensor);
    }

    @Test
    @Transactional
    public void deleteLicensor() throws Exception {
        // Initialize the database
        licensorRepository.saveAndFlush(licensor);

        int databaseSizeBeforeDelete = licensorRepository.findAll().size();

        // Get the licensor
        restLicensorMockMvc.perform(delete("/api/licensors/{id}", licensor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Licensor> licensorList = licensorRepository.findAll();
        assertThat(licensorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Licensor in Elasticsearch
        verify(mockLicensorSearchRepository, times(1)).deleteById(licensor.getId());
    }

    @Test
    @Transactional
    public void searchLicensor() throws Exception {
        // Initialize the database
        licensorRepository.saveAndFlush(licensor);
        when(mockLicensorSearchRepository.search(queryStringQuery("id:" + licensor.getId())))
            .thenReturn(Collections.singletonList(licensor));
        // Search the licensor
        restLicensorMockMvc.perform(get("/api/_search/licensors?query=id:" + licensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Licensor.class);
        Licensor licensor1 = new Licensor();
        licensor1.setId(1L);
        Licensor licensor2 = new Licensor();
        licensor2.setId(licensor1.getId());
        assertThat(licensor1).isEqualTo(licensor2);
        licensor2.setId(2L);
        assertThat(licensor1).isNotEqualTo(licensor2);
        licensor1.setId(null);
        assertThat(licensor1).isNotEqualTo(licensor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LicensorDTO.class);
        LicensorDTO licensorDTO1 = new LicensorDTO();
        licensorDTO1.setId(1L);
        LicensorDTO licensorDTO2 = new LicensorDTO();
        assertThat(licensorDTO1).isNotEqualTo(licensorDTO2);
        licensorDTO2.setId(licensorDTO1.getId());
        assertThat(licensorDTO1).isEqualTo(licensorDTO2);
        licensorDTO2.setId(2L);
        assertThat(licensorDTO1).isNotEqualTo(licensorDTO2);
        licensorDTO1.setId(null);
        assertThat(licensorDTO1).isNotEqualTo(licensorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(licensorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(licensorMapper.fromId(null)).isNull();
    }
}
