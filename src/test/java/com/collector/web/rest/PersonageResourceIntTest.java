package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Personage;
import com.collector.repository.PersonageRepository;
import com.collector.repository.search.PersonageSearchRepository;
import com.collector.service.PersonageService;
import com.collector.service.dto.PersonageDTO;
import com.collector.service.mapper.PersonageMapper;
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
 * Test class for the PersonageResource REST controller.
 *
 * @see PersonageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class PersonageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PersonageRepository personageRepository;


    @Autowired
    private PersonageMapper personageMapper;
    

    @Autowired
    private PersonageService personageService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.PersonageSearchRepositoryMockConfiguration
     */
    @Autowired
    private PersonageSearchRepository mockPersonageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPersonageMockMvc;

    private Personage personage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonageResource personageResource = new PersonageResource(personageService);
        this.restPersonageMockMvc = MockMvcBuilders.standaloneSetup(personageResource)
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
    public static Personage createEntity(EntityManager em) {
        Personage personage = new Personage()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .codeName(DEFAULT_CODE_NAME)
            .originalName(DEFAULT_ORIGINAL_NAME)
            .bio(DEFAULT_BIO)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return personage;
    }

    @Before
    public void initTest() {
        personage = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonage() throws Exception {
        int databaseSizeBeforeCreate = personageRepository.findAll().size();

        // Create the Personage
        PersonageDTO personageDTO = personageMapper.toDto(personage);
        restPersonageMockMvc.perform(post("/api/personages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personageDTO)))
            .andExpect(status().isCreated());

        // Validate the Personage in the database
        List<Personage> personageList = personageRepository.findAll();
        assertThat(personageList).hasSize(databaseSizeBeforeCreate + 1);
        Personage testPersonage = personageList.get(personageList.size() - 1);
        assertThat(testPersonage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonage.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPersonage.getCodeName()).isEqualTo(DEFAULT_CODE_NAME);
        assertThat(testPersonage.getOriginalName()).isEqualTo(DEFAULT_ORIGINAL_NAME);
        assertThat(testPersonage.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testPersonage.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPersonage.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Personage in Elasticsearch
        verify(mockPersonageSearchRepository, times(1)).save(testPersonage);
    }

    @Test
    @Transactional
    public void createPersonageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personageRepository.findAll().size();

        // Create the Personage with an existing ID
        personage.setId(1L);
        PersonageDTO personageDTO = personageMapper.toDto(personage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonageMockMvc.perform(post("/api/personages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personage in the database
        List<Personage> personageList = personageRepository.findAll();
        assertThat(personageList).hasSize(databaseSizeBeforeCreate);

        // Validate the Personage in Elasticsearch
        verify(mockPersonageSearchRepository, times(0)).save(personage);
    }

    @Test
    @Transactional
    public void getAllPersonages() throws Exception {
        // Initialize the database
        personageRepository.saveAndFlush(personage);

        // Get all the personageList
        restPersonageMockMvc.perform(get("/api/personages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].codeName").value(hasItem(DEFAULT_CODE_NAME.toString())))
            .andExpect(jsonPath("$.[*].originalName").value(hasItem(DEFAULT_ORIGINAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getPersonage() throws Exception {
        // Initialize the database
        personageRepository.saveAndFlush(personage);

        // Get the personage
        restPersonageMockMvc.perform(get("/api/personages/{id}", personage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.codeName").value(DEFAULT_CODE_NAME.toString()))
            .andExpect(jsonPath("$.originalName").value(DEFAULT_ORIGINAL_NAME.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingPersonage() throws Exception {
        // Get the personage
        restPersonageMockMvc.perform(get("/api/personages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonage() throws Exception {
        // Initialize the database
        personageRepository.saveAndFlush(personage);

        int databaseSizeBeforeUpdate = personageRepository.findAll().size();

        // Update the personage
        Personage updatedPersonage = personageRepository.findById(personage.getId()).get();
        // Disconnect from session so that the updates on updatedPersonage are not directly saved in db
        em.detach(updatedPersonage);
        updatedPersonage
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .codeName(UPDATED_CODE_NAME)
            .originalName(UPDATED_ORIGINAL_NAME)
            .bio(UPDATED_BIO)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        PersonageDTO personageDTO = personageMapper.toDto(updatedPersonage);

        restPersonageMockMvc.perform(put("/api/personages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personageDTO)))
            .andExpect(status().isOk());

        // Validate the Personage in the database
        List<Personage> personageList = personageRepository.findAll();
        assertThat(personageList).hasSize(databaseSizeBeforeUpdate);
        Personage testPersonage = personageList.get(personageList.size() - 1);
        assertThat(testPersonage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonage.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPersonage.getCodeName()).isEqualTo(UPDATED_CODE_NAME);
        assertThat(testPersonage.getOriginalName()).isEqualTo(UPDATED_ORIGINAL_NAME);
        assertThat(testPersonage.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testPersonage.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPersonage.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Personage in Elasticsearch
        verify(mockPersonageSearchRepository, times(1)).save(testPersonage);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonage() throws Exception {
        int databaseSizeBeforeUpdate = personageRepository.findAll().size();

        // Create the Personage
        PersonageDTO personageDTO = personageMapper.toDto(personage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonageMockMvc.perform(put("/api/personages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personage in the database
        List<Personage> personageList = personageRepository.findAll();
        assertThat(personageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Personage in Elasticsearch
        verify(mockPersonageSearchRepository, times(0)).save(personage);
    }

    @Test
    @Transactional
    public void deletePersonage() throws Exception {
        // Initialize the database
        personageRepository.saveAndFlush(personage);

        int databaseSizeBeforeDelete = personageRepository.findAll().size();

        // Get the personage
        restPersonageMockMvc.perform(delete("/api/personages/{id}", personage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Personage> personageList = personageRepository.findAll();
        assertThat(personageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Personage in Elasticsearch
        verify(mockPersonageSearchRepository, times(1)).deleteById(personage.getId());
    }

    @Test
    @Transactional
    public void searchPersonage() throws Exception {
        // Initialize the database
        personageRepository.saveAndFlush(personage);
        when(mockPersonageSearchRepository.search(queryStringQuery("id:" + personage.getId())))
            .thenReturn(Collections.singletonList(personage));
        // Search the personage
        restPersonageMockMvc.perform(get("/api/_search/personages?query=id:" + personage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].codeName").value(hasItem(DEFAULT_CODE_NAME.toString())))
            .andExpect(jsonPath("$.[*].originalName").value(hasItem(DEFAULT_ORIGINAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personage.class);
        Personage personage1 = new Personage();
        personage1.setId(1L);
        Personage personage2 = new Personage();
        personage2.setId(personage1.getId());
        assertThat(personage1).isEqualTo(personage2);
        personage2.setId(2L);
        assertThat(personage1).isNotEqualTo(personage2);
        personage1.setId(null);
        assertThat(personage1).isNotEqualTo(personage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonageDTO.class);
        PersonageDTO personageDTO1 = new PersonageDTO();
        personageDTO1.setId(1L);
        PersonageDTO personageDTO2 = new PersonageDTO();
        assertThat(personageDTO1).isNotEqualTo(personageDTO2);
        personageDTO2.setId(personageDTO1.getId());
        assertThat(personageDTO1).isEqualTo(personageDTO2);
        personageDTO2.setId(2L);
        assertThat(personageDTO1).isNotEqualTo(personageDTO2);
        personageDTO1.setId(null);
        assertThat(personageDTO1).isNotEqualTo(personageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(personageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(personageMapper.fromId(null)).isNull();
    }
}
