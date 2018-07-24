package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Publisher;
import com.collector.repository.PublisherRepository;
import com.collector.repository.search.PublisherSearchRepository;
import com.collector.service.PublisherService;
import com.collector.service.dto.PublisherDTO;
import com.collector.service.mapper.PublisherMapper;
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
 * Test class for the PublisherResource REST controller.
 *
 * @see PublisherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class PublisherResourceIntTest {

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
    private PublisherRepository publisherRepository;


    @Autowired
    private PublisherMapper publisherMapper;
    

    @Autowired
    private PublisherService publisherService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.PublisherSearchRepositoryMockConfiguration
     */
    @Autowired
    private PublisherSearchRepository mockPublisherSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PublisherResource publisherResource = new PublisherResource(publisherService);
        this.restPublisherMockMvc = MockMvcBuilders.standaloneSetup(publisherResource)
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
    public static Publisher createEntity(EntityManager em) {
        Publisher publisher = new Publisher()
            .name(DEFAULT_NAME)
            .website(DEFAULT_WEBSITE)
            .info(DEFAULT_INFO)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return publisher;
    }

    @Before
    public void initTest() {
        publisher = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublisher() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // Create the Publisher
        PublisherDTO publisherDTO = publisherMapper.toDto(publisher);
        restPublisherMockMvc.perform(post("/api/publishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publisherDTO)))
            .andExpect(status().isCreated());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate + 1);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublisher.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testPublisher.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testPublisher.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPublisher.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPublisher.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPublisher.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Publisher in Elasticsearch
        verify(mockPublisherSearchRepository, times(1)).save(testPublisher);
    }

    @Test
    @Transactional
    public void createPublisherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // Create the Publisher with an existing ID
        publisher.setId(1L);
        PublisherDTO publisherDTO = publisherMapper.toDto(publisher);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublisherMockMvc.perform(post("/api/publishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publisherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate);

        // Validate the Publisher in Elasticsearch
        verify(mockPublisherSearchRepository, times(0)).save(publisher);
    }

    @Test
    @Transactional
    public void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList
        restPublisherMockMvc.perform(get("/api/publishers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
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
    public void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
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
    public void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher
        Publisher updatedPublisher = publisherRepository.findById(publisher.getId()).get();
        // Disconnect from session so that the updates on updatedPublisher are not directly saved in db
        em.detach(updatedPublisher);
        updatedPublisher
            .name(UPDATED_NAME)
            .website(UPDATED_WEBSITE)
            .info(UPDATED_INFO)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        PublisherDTO publisherDTO = publisherMapper.toDto(updatedPublisher);

        restPublisherMockMvc.perform(put("/api/publishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publisherDTO)))
            .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublisher.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testPublisher.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testPublisher.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPublisher.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPublisher.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPublisher.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Publisher in Elasticsearch
        verify(mockPublisherSearchRepository, times(1)).save(testPublisher);
    }

    @Test
    @Transactional
    public void updateNonExistingPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Create the Publisher
        PublisherDTO publisherDTO = publisherMapper.toDto(publisher);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPublisherMockMvc.perform(put("/api/publishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publisherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Publisher in Elasticsearch
        verify(mockPublisherSearchRepository, times(0)).save(publisher);
    }

    @Test
    @Transactional
    public void deletePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeDelete = publisherRepository.findAll().size();

        // Get the publisher
        restPublisherMockMvc.perform(delete("/api/publishers/{id}", publisher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Publisher in Elasticsearch
        verify(mockPublisherSearchRepository, times(1)).deleteById(publisher.getId());
    }

    @Test
    @Transactional
    public void searchPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);
        when(mockPublisherSearchRepository.search(queryStringQuery("id:" + publisher.getId())))
            .thenReturn(Collections.singletonList(publisher));
        // Search the publisher
        restPublisherMockMvc.perform(get("/api/_search/publishers?query=id:" + publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
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
        TestUtil.equalsVerifier(Publisher.class);
        Publisher publisher1 = new Publisher();
        publisher1.setId(1L);
        Publisher publisher2 = new Publisher();
        publisher2.setId(publisher1.getId());
        assertThat(publisher1).isEqualTo(publisher2);
        publisher2.setId(2L);
        assertThat(publisher1).isNotEqualTo(publisher2);
        publisher1.setId(null);
        assertThat(publisher1).isNotEqualTo(publisher2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublisherDTO.class);
        PublisherDTO publisherDTO1 = new PublisherDTO();
        publisherDTO1.setId(1L);
        PublisherDTO publisherDTO2 = new PublisherDTO();
        assertThat(publisherDTO1).isNotEqualTo(publisherDTO2);
        publisherDTO2.setId(publisherDTO1.getId());
        assertThat(publisherDTO1).isEqualTo(publisherDTO2);
        publisherDTO2.setId(2L);
        assertThat(publisherDTO1).isNotEqualTo(publisherDTO2);
        publisherDTO1.setId(null);
        assertThat(publisherDTO1).isNotEqualTo(publisherDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(publisherMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(publisherMapper.fromId(null)).isNull();
    }
}
