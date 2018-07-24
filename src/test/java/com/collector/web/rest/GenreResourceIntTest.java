package com.collector.web.rest;

import com.collector.CollectorApp;

import com.collector.domain.Genre;
import com.collector.repository.GenreRepository;
import com.collector.repository.search.GenreSearchRepository;
import com.collector.service.GenreService;
import com.collector.service.dto.GenreDTO;
import com.collector.service.mapper.GenreMapper;
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
 * Test class for the GenreResource REST controller.
 *
 * @see GenreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApp.class)
public class GenreResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private GenreRepository genreRepository;


    @Autowired
    private GenreMapper genreMapper;
    

    @Autowired
    private GenreService genreService;

    /**
     * This repository is mocked in the com.collector.repository.search test package.
     *
     * @see com.collector.repository.search.GenreSearchRepositoryMockConfiguration
     */
    @Autowired
    private GenreSearchRepository mockGenreSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGenreMockMvc;

    private Genre genre;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GenreResource genreResource = new GenreResource(genreService);
        this.restGenreMockMvc = MockMvcBuilders.standaloneSetup(genreResource)
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
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre()
            .desc(DEFAULT_DESC);
        return genre;
    }

    @Before
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    public void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);
        restGenreMockMvc.perform(post("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getDesc()).isEqualTo(DEFAULT_DESC);

        // Validate the Genre in Elasticsearch
        verify(mockGenreSearchRepository, times(1)).save(testGenre);
    }

    @Test
    @Transactional
    public void createGenreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre with an existing ID
        genre.setId(1L);
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenreMockMvc.perform(post("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate);

        // Validate the Genre in Elasticsearch
        verify(mockGenreSearchRepository, times(0)).save(genre);
    }

    @Test
    @Transactional
    public void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList
        restGenreMockMvc.perform(get("/api/genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }
    

    @Test
    @Transactional
    public void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findById(genre.getId()).get();
        // Disconnect from session so that the updates on updatedGenre are not directly saved in db
        em.detach(updatedGenre);
        updatedGenre
            .desc(UPDATED_DESC);
        GenreDTO genreDTO = genreMapper.toDto(updatedGenre);

        restGenreMockMvc.perform(put("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getDesc()).isEqualTo(UPDATED_DESC);

        // Validate the Genre in Elasticsearch
        verify(mockGenreSearchRepository, times(1)).save(testGenre);
    }

    @Test
    @Transactional
    public void updateNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Create the Genre
        GenreDTO genreDTO = genreMapper.toDto(genre);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGenreMockMvc.perform(put("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Genre in Elasticsearch
        verify(mockGenreSearchRepository, times(0)).save(genre);
    }

    @Test
    @Transactional
    public void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Get the genre
        restGenreMockMvc.perform(delete("/api/genres/{id}", genre.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Genre in Elasticsearch
        verify(mockGenreSearchRepository, times(1)).deleteById(genre.getId());
    }

    @Test
    @Transactional
    public void searchGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
        when(mockGenreSearchRepository.search(queryStringQuery("id:" + genre.getId())))
            .thenReturn(Collections.singletonList(genre));
        // Search the genre
        restGenreMockMvc.perform(get("/api/_search/genres?query=id:" + genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genre.class);
        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(genre1.getId());
        assertThat(genre1).isEqualTo(genre2);
        genre2.setId(2L);
        assertThat(genre1).isNotEqualTo(genre2);
        genre1.setId(null);
        assertThat(genre1).isNotEqualTo(genre2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenreDTO.class);
        GenreDTO genreDTO1 = new GenreDTO();
        genreDTO1.setId(1L);
        GenreDTO genreDTO2 = new GenreDTO();
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO2.setId(genreDTO1.getId());
        assertThat(genreDTO1).isEqualTo(genreDTO2);
        genreDTO2.setId(2L);
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO1.setId(null);
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(genreMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(genreMapper.fromId(null)).isNull();
    }
}
