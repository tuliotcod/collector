package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.CollectorUser;
import com.collector.repository.CollectorUserRepository;
import com.collector.repository.search.CollectorUserSearchRepository;
import com.collector.service.CollectorUserService;
import com.collector.service.dto.CollectorUserDTO;
import com.collector.service.mapper.CollectorUserMapper;
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
 * Test class for the CollectorUserResource REST controller.
 *
 * @see CollectorUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class CollectorUserResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_GENDER = 1;
    private static final Integer UPDATED_GENDER = 2;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CollectorUserRepository collectorUserRepository;


    @Autowired
    private CollectorUserMapper collectorUserMapper;
    

    @Autowired
    private CollectorUserService collectorUserService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.CollectorUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollectorUserSearchRepository mockCollectorUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCollectorUserMockMvc;

    private CollectorUser collectorUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollectorUserResource collectorUserResource = new CollectorUserResource(collectorUserService);
        this.restCollectorUserMockMvc = MockMvcBuilders.standaloneSetup(collectorUserResource)
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
    public static CollectorUser createEntity(EntityManager em) {
        CollectorUser collectorUser = new CollectorUser()
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .gender(DEFAULT_GENDER)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return collectorUser;
    }

    @Before
    public void initTest() {
        collectorUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectorUser() throws Exception {
        int databaseSizeBeforeCreate = collectorUserRepository.findAll().size();

        // Create the CollectorUser
        CollectorUserDTO collectorUserDTO = collectorUserMapper.toDto(collectorUser);
        restCollectorUserMockMvc.perform(post("/api/collector-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectorUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CollectorUser in the database
        List<CollectorUser> collectorUserList = collectorUserRepository.findAll();
        assertThat(collectorUserList).hasSize(databaseSizeBeforeCreate + 1);
        CollectorUser testCollectorUser = collectorUserList.get(collectorUserList.size() - 1);
        assertThat(testCollectorUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testCollectorUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCollectorUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCollectorUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollectorUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCollectorUser.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testCollectorUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCollectorUser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCollectorUser.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the CollectorUser in Elasticsearch
        verify(mockCollectorUserSearchRepository, times(1)).save(testCollectorUser);
    }

    @Test
    @Transactional
    public void createCollectorUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectorUserRepository.findAll().size();

        // Create the CollectorUser with an existing ID
        collectorUser.setId(1L);
        CollectorUserDTO collectorUserDTO = collectorUserMapper.toDto(collectorUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectorUserMockMvc.perform(post("/api/collector-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectorUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CollectorUser in the database
        List<CollectorUser> collectorUserList = collectorUserRepository.findAll();
        assertThat(collectorUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the CollectorUser in Elasticsearch
        verify(mockCollectorUserSearchRepository, times(0)).save(collectorUser);
    }

    @Test
    @Transactional
    public void getAllCollectorUsers() throws Exception {
        // Initialize the database
        collectorUserRepository.saveAndFlush(collectorUser);

        // Get all the collectorUserList
        restCollectorUserMockMvc.perform(get("/api/collector-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectorUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getCollectorUser() throws Exception {
        // Initialize the database
        collectorUserRepository.saveAndFlush(collectorUser);

        // Get the collectorUser
        restCollectorUserMockMvc.perform(get("/api/collector-users/{id}", collectorUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collectorUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCollectorUser() throws Exception {
        // Get the collectorUser
        restCollectorUserMockMvc.perform(get("/api/collector-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectorUser() throws Exception {
        // Initialize the database
        collectorUserRepository.saveAndFlush(collectorUser);

        int databaseSizeBeforeUpdate = collectorUserRepository.findAll().size();

        // Update the collectorUser
        CollectorUser updatedCollectorUser = collectorUserRepository.findById(collectorUser.getId()).get();
        // Disconnect from session so that the updates on updatedCollectorUser are not directly saved in db
        em.detach(updatedCollectorUser);
        updatedCollectorUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .gender(UPDATED_GENDER)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        CollectorUserDTO collectorUserDTO = collectorUserMapper.toDto(updatedCollectorUser);

        restCollectorUserMockMvc.perform(put("/api/collector-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectorUserDTO)))
            .andExpect(status().isOk());

        // Validate the CollectorUser in the database
        List<CollectorUser> collectorUserList = collectorUserRepository.findAll();
        assertThat(collectorUserList).hasSize(databaseSizeBeforeUpdate);
        CollectorUser testCollectorUser = collectorUserList.get(collectorUserList.size() - 1);
        assertThat(testCollectorUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testCollectorUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCollectorUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCollectorUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollectorUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCollectorUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testCollectorUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCollectorUser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCollectorUser.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the CollectorUser in Elasticsearch
        verify(mockCollectorUserSearchRepository, times(1)).save(testCollectorUser);
    }

    @Test
    @Transactional
    public void updateNonExistingCollectorUser() throws Exception {
        int databaseSizeBeforeUpdate = collectorUserRepository.findAll().size();

        // Create the CollectorUser
        CollectorUserDTO collectorUserDTO = collectorUserMapper.toDto(collectorUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCollectorUserMockMvc.perform(put("/api/collector-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectorUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CollectorUser in the database
        List<CollectorUser> collectorUserList = collectorUserRepository.findAll();
        assertThat(collectorUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CollectorUser in Elasticsearch
        verify(mockCollectorUserSearchRepository, times(0)).save(collectorUser);
    }

    @Test
    @Transactional
    public void deleteCollectorUser() throws Exception {
        // Initialize the database
        collectorUserRepository.saveAndFlush(collectorUser);

        int databaseSizeBeforeDelete = collectorUserRepository.findAll().size();

        // Get the collectorUser
        restCollectorUserMockMvc.perform(delete("/api/collector-users/{id}", collectorUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CollectorUser> collectorUserList = collectorUserRepository.findAll();
        assertThat(collectorUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CollectorUser in Elasticsearch
        verify(mockCollectorUserSearchRepository, times(1)).deleteById(collectorUser.getId());
    }

    @Test
    @Transactional
    public void searchCollectorUser() throws Exception {
        // Initialize the database
        collectorUserRepository.saveAndFlush(collectorUser);
        when(mockCollectorUserSearchRepository.search(queryStringQuery("id:" + collectorUser.getId())))
            .thenReturn(Collections.singletonList(collectorUser));
        // Search the collectorUser
        restCollectorUserMockMvc.perform(get("/api/_search/collector-users?query=id:" + collectorUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectorUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectorUser.class);
        CollectorUser collectorUser1 = new CollectorUser();
        collectorUser1.setId(1L);
        CollectorUser collectorUser2 = new CollectorUser();
        collectorUser2.setId(collectorUser1.getId());
        assertThat(collectorUser1).isEqualTo(collectorUser2);
        collectorUser2.setId(2L);
        assertThat(collectorUser1).isNotEqualTo(collectorUser2);
        collectorUser1.setId(null);
        assertThat(collectorUser1).isNotEqualTo(collectorUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectorUserDTO.class);
        CollectorUserDTO collectorUserDTO1 = new CollectorUserDTO();
        collectorUserDTO1.setId(1L);
        CollectorUserDTO collectorUserDTO2 = new CollectorUserDTO();
        assertThat(collectorUserDTO1).isNotEqualTo(collectorUserDTO2);
        collectorUserDTO2.setId(collectorUserDTO1.getId());
        assertThat(collectorUserDTO1).isEqualTo(collectorUserDTO2);
        collectorUserDTO2.setId(2L);
        assertThat(collectorUserDTO1).isNotEqualTo(collectorUserDTO2);
        collectorUserDTO1.setId(null);
        assertThat(collectorUserDTO1).isNotEqualTo(collectorUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(collectorUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(collectorUserMapper.fromId(null)).isNull();
    }
}
