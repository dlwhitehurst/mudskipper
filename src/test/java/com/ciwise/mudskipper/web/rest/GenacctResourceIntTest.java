package com.ciwise.mudskipper.web.rest;

import com.ciwise.mudskipper.Application;
import com.ciwise.mudskipper.domain.Genacct;
import com.ciwise.mudskipper.repository.GenacctRepository;
import com.ciwise.mudskipper.repository.search.GenacctSearchRepository;

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
 * Test class for the GenacctResource REST controller.
 *
 * @see GenacctResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenacctResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final Integer DEFAULT_ACCTNO = 1;
    private static final Integer UPDATED_ACCTNO = 2;

    @Inject
    private GenacctRepository genacctRepository;

    @Inject
    private GenacctSearchRepository genacctSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGenacctMockMvc;

    private Genacct genacct;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenacctResource genacctResource = new GenacctResource();
        ReflectionTestUtils.setField(genacctResource, "genacctSearchRepository", genacctSearchRepository);
        ReflectionTestUtils.setField(genacctResource, "genacctRepository", genacctRepository);
        this.restGenacctMockMvc = MockMvcBuilders.standaloneSetup(genacctResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        genacct = new Genacct();
        genacct.setName(DEFAULT_NAME);
        genacct.setType(DEFAULT_TYPE);
        genacct.setAcctno(DEFAULT_ACCTNO);
    }

    @Test
    @Transactional
    public void createGenacct() throws Exception {
        int databaseSizeBeforeCreate = genacctRepository.findAll().size();

        // Create the Genacct

        restGenacctMockMvc.perform(post("/api/genaccts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacct)))
                .andExpect(status().isCreated());

        // Validate the Genacct in the database
        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeCreate + 1);
        Genacct testGenacct = genaccts.get(genaccts.size() - 1);
        assertThat(testGenacct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGenacct.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGenacct.getAcctno()).isEqualTo(DEFAULT_ACCTNO);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = genacctRepository.findAll().size();
        // set the field null
        genacct.setName(null);

        // Create the Genacct, which fails.

        restGenacctMockMvc.perform(post("/api/genaccts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacct)))
                .andExpect(status().isBadRequest());

        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genacctRepository.findAll().size();
        // set the field null
        genacct.setType(null);

        // Create the Genacct, which fails.

        restGenacctMockMvc.perform(post("/api/genaccts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacct)))
                .andExpect(status().isBadRequest());

        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcctnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = genacctRepository.findAll().size();
        // set the field null
        genacct.setAcctno(null);

        // Create the Genacct, which fails.

        restGenacctMockMvc.perform(post("/api/genaccts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacct)))
                .andExpect(status().isBadRequest());

        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenaccts() throws Exception {
        // Initialize the database
        genacctRepository.saveAndFlush(genacct);

        // Get all the genaccts
        restGenacctMockMvc.perform(get("/api/genaccts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genacct.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].acctno").value(hasItem(DEFAULT_ACCTNO)));
    }

    @Test
    @Transactional
    public void getGenacct() throws Exception {
        // Initialize the database
        genacctRepository.saveAndFlush(genacct);

        // Get the genacct
        restGenacctMockMvc.perform(get("/api/genaccts/{id}", genacct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genacct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.acctno").value(DEFAULT_ACCTNO));
    }

    @Test
    @Transactional
    public void getNonExistingGenacct() throws Exception {
        // Get the genacct
        restGenacctMockMvc.perform(get("/api/genaccts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenacct() throws Exception {
        // Initialize the database
        genacctRepository.saveAndFlush(genacct);

		int databaseSizeBeforeUpdate = genacctRepository.findAll().size();

        // Update the genacct
        genacct.setName(UPDATED_NAME);
        genacct.setType(UPDATED_TYPE);
        genacct.setAcctno(UPDATED_ACCTNO);

        restGenacctMockMvc.perform(put("/api/genaccts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacct)))
                .andExpect(status().isOk());

        // Validate the Genacct in the database
        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeUpdate);
        Genacct testGenacct = genaccts.get(genaccts.size() - 1);
        assertThat(testGenacct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenacct.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGenacct.getAcctno()).isEqualTo(UPDATED_ACCTNO);
    }

    @Test
    @Transactional
    public void deleteGenacct() throws Exception {
        // Initialize the database
        genacctRepository.saveAndFlush(genacct);

		int databaseSizeBeforeDelete = genacctRepository.findAll().size();

        // Get the genacct
        restGenacctMockMvc.perform(delete("/api/genaccts/{id}", genacct.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genacct> genaccts = genacctRepository.findAll();
        assertThat(genaccts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
