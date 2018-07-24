package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Artist;
import com.collector.repository.ArtistRepository;
import com.collector.repository.search.ArtistSearchRepository;
import com.collector.service.ArtistService;
import com.collector.service.dto.ArtistDTO;
import com.collector.service.mapper.ArtistMapper;
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
 * Test class for the ArtistResource REST controller.
 *
 * @see ArtistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class ArtistResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_OF_DEATH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_DEATH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ArtistRepository artistRepository;


    @Autowired
    private ArtistMapper artistMapper;
    

    @Autowired
    private ArtistService artistService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.ArtistSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArtistSearchRepository mockArtistSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtistMockMvc;

    private Artist artist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtistResource artistResource = new ArtistResource(artistService);
        this.restArtistMockMvc = MockMvcBuilders.standaloneSetup(artistResource)
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
    public static Artist createEntity(EntityManager em) {
        Artist artist = new Artist()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .nickname(DEFAULT_NICKNAME)
            .birthday(DEFAULT_BIRTHDAY)
            .dateOfDeath(DEFAULT_DATE_OF_DEATH)
            .bio(DEFAULT_BIO)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .creationDate(DEFAULT_CREATION_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return artist;
    }

    @Before
    public void initTest() {
        artist = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);
        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArtist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testArtist.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testArtist.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testArtist.getDateOfDeath()).isEqualTo(DEFAULT_DATE_OF_DEATH);
        assertThat(testArtist.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testArtist.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testArtist.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testArtist.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testArtist.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Artist in Elasticsearch
        verify(mockArtistSearchRepository, times(1)).save(testArtist);
    }

    @Test
    @Transactional
    public void createArtistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist with an existing ID
        artist.setId(1L);
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate);

        // Validate the Artist in Elasticsearch
        verify(mockArtistSearchRepository, times(0)).save(artist);
    }

    @Test
    @Transactional
    public void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].dateOfDeath").value(hasItem(DEFAULT_DATE_OF_DEATH.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.dateOfDeath").value(DEFAULT_DATE_OF_DEATH.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        Artist updatedArtist = artistRepository.findById(artist.getId()).get();
        // Disconnect from session so that the updates on updatedArtist are not directly saved in db
        em.detach(updatedArtist);
        updatedArtist
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nickname(UPDATED_NICKNAME)
            .birthday(UPDATED_BIRTHDAY)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .bio(UPDATED_BIO)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        ArtistDTO artistDTO = artistMapper.toDto(updatedArtist);

        restArtistMockMvc.perform(put("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArtist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testArtist.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testArtist.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testArtist.getDateOfDeath()).isEqualTo(UPDATED_DATE_OF_DEATH);
        assertThat(testArtist.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testArtist.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testArtist.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testArtist.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testArtist.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Artist in Elasticsearch
        verify(mockArtistSearchRepository, times(1)).save(testArtist);
    }

    @Test
    @Transactional
    public void updateNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Create the Artist
        ArtistDTO artistDTO = artistMapper.toDto(artist);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArtistMockMvc.perform(put("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Artist in Elasticsearch
        verify(mockArtistSearchRepository, times(0)).save(artist);
    }

    @Test
    @Transactional
    public void deleteArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Get the artist
        restArtistMockMvc.perform(delete("/api/artists/{id}", artist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Artist in Elasticsearch
        verify(mockArtistSearchRepository, times(1)).deleteById(artist.getId());
    }

    @Test
    @Transactional
    public void searchArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);
        when(mockArtistSearchRepository.search(queryStringQuery("id:" + artist.getId())))
            .thenReturn(Collections.singletonList(artist));
        // Search the artist
        restArtistMockMvc.perform(get("/api/_search/artists?query=id:" + artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].dateOfDeath").value(hasItem(DEFAULT_DATE_OF_DEATH.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artist.class);
        Artist artist1 = new Artist();
        artist1.setId(1L);
        Artist artist2 = new Artist();
        artist2.setId(artist1.getId());
        assertThat(artist1).isEqualTo(artist2);
        artist2.setId(2L);
        assertThat(artist1).isNotEqualTo(artist2);
        artist1.setId(null);
        assertThat(artist1).isNotEqualTo(artist2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtistDTO.class);
        ArtistDTO artistDTO1 = new ArtistDTO();
        artistDTO1.setId(1L);
        ArtistDTO artistDTO2 = new ArtistDTO();
        assertThat(artistDTO1).isNotEqualTo(artistDTO2);
        artistDTO2.setId(artistDTO1.getId());
        assertThat(artistDTO1).isEqualTo(artistDTO2);
        artistDTO2.setId(2L);
        assertThat(artistDTO1).isNotEqualTo(artistDTO2);
        artistDTO1.setId(null);
        assertThat(artistDTO1).isNotEqualTo(artistDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(artistMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(artistMapper.fromId(null)).isNull();
    }
}
