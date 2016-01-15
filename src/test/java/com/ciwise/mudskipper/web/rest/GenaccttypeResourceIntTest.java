package com.ciwise.mudskipper.web.rest;

import com.ciwise.mudskipper.Application;
import com.ciwise.mudskipper.domain.Genaccttype;
import com.ciwise.mudskipper.repository.GenaccttypeRepository;
import com.ciwise.mudskipper.repository.search.GenaccttypeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GenaccttypeResource REST controller.
 *
 * @see GenaccttypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenaccttypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private GenaccttypeRepository genaccttypeRepository;

    @Inject
    private GenaccttypeSearchRepository genaccttypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGenaccttypeMockMvc;

    private Genaccttype genaccttype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenaccttypeResource genaccttypeResource = new GenaccttypeResource();
        ReflectionTestUtils.setField(genaccttypeResource, "genaccttypeSearchRepository", genaccttypeSearchRepository);
        ReflectionTestUtils.setField(genaccttypeResource, "genaccttypeRepository", genaccttypeRepository);
        this.restGenaccttypeMockMvc = MockMvcBuilders.standaloneSetup(genaccttypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        genaccttype = new Genaccttype();
        genaccttype.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGenaccttype() throws Exception {
        int databaseSizeBeforeCreate = genaccttypeRepository.findAll().size();

        // Create the Genaccttype

        restGenaccttypeMockMvc.perform(post("/api/genaccttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genaccttype)))
                .andExpect(status().isCreated());

        // Validate the Genaccttype in the database
        List<Genaccttype> genaccttypes = genaccttypeRepository.findAll();
        assertThat(genaccttypes).hasSize(databaseSizeBeforeCreate + 1);
        Genaccttype testGenaccttype = genaccttypes.get(genaccttypes.size() - 1);
        assertThat(testGenaccttype.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = genaccttypeRepository.findAll().size();
        // set the field null
        genaccttype.setName(null);

        // Create the Genaccttype, which fails.

        restGenaccttypeMockMvc.perform(post("/api/genaccttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genaccttype)))
                .andExpect(status().isBadRequest());

        List<Genaccttype> genaccttypes = genaccttypeRepository.findAll();
        assertThat(genaccttypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenaccttypes() throws Exception {
        // Initialize the database
        genaccttypeRepository.saveAndFlush(genaccttype);

        // Get all the genaccttypes
        restGenaccttypeMockMvc.perform(get("/api/genaccttypes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genaccttype.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGenaccttype() throws Exception {
        // Initialize the database
        genaccttypeRepository.saveAndFlush(genaccttype);

        // Get the genaccttype
        restGenaccttypeMockMvc.perform(get("/api/genaccttypes/{id}", genaccttype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genaccttype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGenaccttype() throws Exception {
        // Get the genaccttype
        restGenaccttypeMockMvc.perform(get("/api/genaccttypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenaccttype() throws Exception {
        // Initialize the database
        genaccttypeRepository.saveAndFlush(genaccttype);

		int databaseSizeBeforeUpdate = genaccttypeRepository.findAll().size();

        // Update the genaccttype
        genaccttype.setName(UPDATED_NAME);

        restGenaccttypeMockMvc.perform(put("/api/genaccttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genaccttype)))
                .andExpect(status().isOk());

        // Validate the Genaccttype in the database
        List<Genaccttype> genaccttypes = genaccttypeRepository.findAll();
        assertThat(genaccttypes).hasSize(databaseSizeBeforeUpdate);
        Genaccttype testGenaccttype = genaccttypes.get(genaccttypes.size() - 1);
        assertThat(testGenaccttype.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteGenaccttype() throws Exception {
        // Initialize the database
        genaccttypeRepository.saveAndFlush(genaccttype);

		int databaseSizeBeforeDelete = genaccttypeRepository.findAll().size();

        // Get the genaccttype
        restGenaccttypeMockMvc.perform(delete("/api/genaccttypes/{id}", genaccttype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genaccttype> genaccttypes = genaccttypeRepository.findAll();
        assertThat(genaccttypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
