package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Collection;
import com.collector.repository.CollectionRepository;
import com.collector.repository.search.CollectionSearchRepository;
import com.collector.service.CollectionService;
import com.collector.service.dto.CollectionDTO;
import com.collector.service.mapper.CollectionMapper;
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
 * Test class for the CollectionResource REST controller.
 *
 * @see CollectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class CollectionResourceIntTest {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private CollectionMapper collectionMapper;
    

    @Autowired
    private CollectionService collectionService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.CollectionSearchRepositoryMockConfiguration
     */
    @Autowired
    private CollectionSearchRepository mockCollectionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCollectionMockMvc;

    private Collection collection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollectionResource collectionResource = new CollectionResource(collectionService);
        this.restCollectionMockMvc = MockMvcBuilders.standaloneSetup(collectionResource)
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
    public static Collection createEntity(EntityManager em) {
        Collection collection = new Collection()
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return collection;
    }

    @Before
    public void initTest() {
        collection = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollection() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate + 1);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCollection.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Collection in Elasticsearch
        verify(mockCollectionSearchRepository, times(1)).save(testCollection);
    }

    @Test
    @Transactional
    public void createCollectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // Create the Collection with an existing ID
        collection.setId(1L);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Collection in Elasticsearch
        verify(mockCollectionSearchRepository, times(0)).save(collection);
    }

    @Test
    @Transactional
    public void getAllCollections() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList
        restCollectionMockMvc.perform(get("/api/collections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collection.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCollection() throws Exception {
        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection
        Collection updatedCollection = collectionRepository.findById(collection.getId()).get();
        // Disconnect from session so that the updates on updatedCollection are not directly saved in db
        em.detach(updatedCollection);
        updatedCollection
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        CollectionDTO collectionDTO = collectionMapper.toDto(updatedCollection);

        restCollectionMockMvc.perform(put("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCollection.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Collection in Elasticsearch
        verify(mockCollectionSearchRepository, times(1)).save(testCollection);
    }

    @Test
    @Transactional
    public void updateNonExistingCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCollectionMockMvc.perform(put("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Collection in Elasticsearch
        verify(mockCollectionSearchRepository, times(0)).save(collection);
    }

    @Test
    @Transactional
    public void deleteCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeDelete = collectionRepository.findAll().size();

        // Get the collection
        restCollectionMockMvc.perform(delete("/api/collections/{id}", collection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Collection in Elasticsearch
        verify(mockCollectionSearchRepository, times(1)).deleteById(collection.getId());
    }

    @Test
    @Transactional
    public void searchCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        when(mockCollectionSearchRepository.search(queryStringQuery("id:" + collection.getId())))
            .thenReturn(Collections.singletonList(collection));
        // Search the collection
        restCollectionMockMvc.perform(get("/api/_search/collections?query=id:" + collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collection.class);
        Collection collection1 = new Collection();
        collection1.setId(1L);
        Collection collection2 = new Collection();
        collection2.setId(collection1.getId());
        assertThat(collection1).isEqualTo(collection2);
        collection2.setId(2L);
        assertThat(collection1).isNotEqualTo(collection2);
        collection1.setId(null);
        assertThat(collection1).isNotEqualTo(collection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionDTO.class);
        CollectionDTO collectionDTO1 = new CollectionDTO();
        collectionDTO1.setId(1L);
        CollectionDTO collectionDTO2 = new CollectionDTO();
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
        collectionDTO2.setId(collectionDTO1.getId());
        assertThat(collectionDTO1).isEqualTo(collectionDTO2);
        collectionDTO2.setId(2L);
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
        collectionDTO1.setId(null);
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(collectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(collectionMapper.fromId(null)).isNull();
    }
}
