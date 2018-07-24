package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Collaborator;
import com.collector.repository.CollaboratorRepository;
import com.collector.repository.search.CollaboratorSearchRepository;
import com.collector.service.CollaboratorService;
import com.collector.service.dto.CollaboratorDTO;
import com.collector.service.mapper.CollaboratorMapper;
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
 * Test class for the CollaboratorResource REST controller.
 *
 * @see CollaboratorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class CollaboratorResourceIntTest {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CollaboratorRepository collaboratorRepository;


    @Autowired
    private CollaboratorMapper collaboratorMapper;
    

    @Autowired
    private CollaboratorService collaboratorService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.CollaboratorSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollaboratorSearchRepository mockCollaboratorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCollaboratorMockMvc;

    private Collaborator collaborator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollaboratorResource collaboratorResource = new CollaboratorResource(collaboratorService);
        this.restCollaboratorMockMvc = MockMvcBuilders.standaloneSetup(collaboratorResource)
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
    public static Collaborator createEntity(EntityManager em) {
        Collaborator collaborator = new Collaborator()
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return collaborator;
    }

    @Before
    public void initTest() {
        collaborator = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollaborator() throws Exception {
        int databaseSizeBeforeCreate = collaboratorRepository.findAll().size();

        // Create the Collaborator
        CollaboratorDTO collaboratorDTO = collaboratorMapper.toDto(collaborator);
        restCollaboratorMockMvc.perform(post("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboratorDTO)))
            .andExpect(status().isCreated());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeCreate + 1);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCollaborator.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Collaborator in Elasticsearch
        verify(mockCollaboratorSearchRepository, times(1)).save(testCollaborator);
    }

    @Test
    @Transactional
    public void createCollaboratorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collaboratorRepository.findAll().size();

        // Create the Collaborator with an existing ID
        collaborator.setId(1L);
        CollaboratorDTO collaboratorDTO = collaboratorMapper.toDto(collaborator);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollaboratorMockMvc.perform(post("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboratorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Collaborator in Elasticsearch
        verify(mockCollaboratorSearchRepository, times(0)).save(collaborator);
    }

    @Test
    @Transactional
    public void getAllCollaborators() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList
        restCollaboratorMockMvc.perform(get("/api/collaborators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborator.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get the collaborator
        restCollaboratorMockMvc.perform(get("/api/collaborators/{id}", collaborator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collaborator.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCollaborator() throws Exception {
        // Get the collaborator
        restCollaboratorMockMvc.perform(get("/api/collaborators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Update the collaborator
        Collaborator updatedCollaborator = collaboratorRepository.findById(collaborator.getId()).get();
        // Disconnect from session so that the updates on updatedCollaborator are not directly saved in db
        em.detach(updatedCollaborator);
        updatedCollaborator
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        CollaboratorDTO collaboratorDTO = collaboratorMapper.toDto(updatedCollaborator);

        restCollaboratorMockMvc.perform(put("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboratorDTO)))
            .andExpect(status().isOk());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCollaborator.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Collaborator in Elasticsearch
        verify(mockCollaboratorSearchRepository, times(1)).save(testCollaborator);
    }

    @Test
    @Transactional
    public void updateNonExistingCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Create the Collaborator
        CollaboratorDTO collaboratorDTO = collaboratorMapper.toDto(collaborator);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCollaboratorMockMvc.perform(put("/api/collaborators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboratorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Collaborator in Elasticsearch
        verify(mockCollaboratorSearchRepository, times(0)).save(collaborator);
    }

    @Test
    @Transactional
    public void deleteCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeDelete = collaboratorRepository.findAll().size();

        // Get the collaborator
        restCollaboratorMockMvc.perform(delete("/api/collaborators/{id}", collaborator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Collaborator in Elasticsearch
        verify(mockCollaboratorSearchRepository, times(1)).deleteById(collaborator.getId());
    }

    @Test
    @Transactional
    public void searchCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);
        when(mockCollaboratorSearchRepository.search(queryStringQuery("id:" + collaborator.getId())))
            .thenReturn(Collections.singletonList(collaborator));
        // Search the collaborator
        restCollaboratorMockMvc.perform(get("/api/_search/collaborators?query=id:" + collaborator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborator.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collaborator.class);
        Collaborator collaborator1 = new Collaborator();
        collaborator1.setId(1L);
        Collaborator collaborator2 = new Collaborator();
        collaborator2.setId(collaborator1.getId());
        assertThat(collaborator1).isEqualTo(collaborator2);
        collaborator2.setId(2L);
        assertThat(collaborator1).isNotEqualTo(collaborator2);
        collaborator1.setId(null);
        assertThat(collaborator1).isNotEqualTo(collaborator2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollaboratorDTO.class);
        CollaboratorDTO collaboratorDTO1 = new CollaboratorDTO();
        collaboratorDTO1.setId(1L);
        CollaboratorDTO collaboratorDTO2 = new CollaboratorDTO();
        assertThat(collaboratorDTO1).isNotEqualTo(collaboratorDTO2);
        collaboratorDTO2.setId(collaboratorDTO1.getId());
        assertThat(collaboratorDTO1).isEqualTo(collaboratorDTO2);
        collaboratorDTO2.setId(2L);
        assertThat(collaboratorDTO1).isNotEqualTo(collaboratorDTO2);
        collaboratorDTO1.setId(null);
        assertThat(collaboratorDTO1).isNotEqualTo(collaboratorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(collaboratorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(collaboratorMapper.fromId(null)).isNull();
    }
}
