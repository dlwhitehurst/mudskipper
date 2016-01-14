package com.ciwise.mudskipper.web.rest;

import com.ciwise.mudskipper.Application;
import com.ciwise.mudskipper.domain.Genaccount;
import com.ciwise.mudskipper.repository.GenaccountRepository;
import com.ciwise.mudskipper.repository.search.GenaccountSearchRepository;

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
 * Test class for the GenaccountResource REST controller.
 *
 * @see GenaccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenaccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_ACCTNO = 1;
    private static final Integer UPDATED_ACCTNO = 2;

    @Inject
    private GenaccountRepository genaccountRepository;

    @Inject
    private GenaccountSearchRepository genaccountSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGenaccountMockMvc;

    private Genaccount genaccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenaccountResource genaccountResource = new GenaccountResource();
        ReflectionTestUtils.setField(genaccountResource, "genaccountSearchRepository", genaccountSearchRepository);
        ReflectionTestUtils.setField(genaccountResource, "genaccountRepository", genaccountRepository);
        this.restGenaccountMockMvc = MockMvcBuilders.standaloneSetup(genaccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        genaccount = new Genaccount();
        genaccount.setName(DEFAULT_NAME);
        genaccount.setAcctno(DEFAULT_ACCTNO);
    }

    @Test
    @Transactional
    public void createGenaccount() throws Exception {
        int databaseSizeBeforeCreate = genaccountRepository.findAll().size();

        // Create the Genaccount

        restGenaccountMockMvc.perform(post("/api/genaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genaccount)))
                .andExpect(status().isCreated());

        // Validate the Genaccount in the database
        List<Genaccount> genaccounts = genaccountRepository.findAll();
        assertThat(genaccounts).hasSize(databaseSizeBeforeCreate + 1);
        Genaccount testGenaccount = genaccounts.get(genaccounts.size() - 1);
        assertThat(testGenaccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGenaccount.getAcctno()).isEqualTo(DEFAULT_ACCTNO);
    }

    @Test
    @Transactional
    public void getAllGenaccounts() throws Exception {
        // Initialize the database
        genaccountRepository.saveAndFlush(genaccount);

        // Get all the genaccounts
        restGenaccountMockMvc.perform(get("/api/genaccounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genaccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].acctno").value(hasItem(DEFAULT_ACCTNO)));
    }

    @Test
    @Transactional
    public void getGenaccount() throws Exception {
        // Initialize the database
        genaccountRepository.saveAndFlush(genaccount);

        // Get the genaccount
        restGenaccountMockMvc.perform(get("/api/genaccounts/{id}", genaccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genaccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acctno").value(DEFAULT_ACCTNO));
    }

    @Test
    @Transactional
    public void getNonExistingGenaccount() throws Exception {
        // Get the genaccount
        restGenaccountMockMvc.perform(get("/api/genaccounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenaccount() throws Exception {
        // Initialize the database
        genaccountRepository.saveAndFlush(genaccount);

		int databaseSizeBeforeUpdate = genaccountRepository.findAll().size();

        // Update the genaccount
        genaccount.setName(UPDATED_NAME);
        genaccount.setAcctno(UPDATED_ACCTNO);

        restGenaccountMockMvc.perform(put("/api/genaccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genaccount)))
                .andExpect(status().isOk());

        // Validate the Genaccount in the database
        List<Genaccount> genaccounts = genaccountRepository.findAll();
        assertThat(genaccounts).hasSize(databaseSizeBeforeUpdate);
        Genaccount testGenaccount = genaccounts.get(genaccounts.size() - 1);
        assertThat(testGenaccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGenaccount.getAcctno()).isEqualTo(UPDATED_ACCTNO);
    }

    @Test
    @Transactional
    public void deleteGenaccount() throws Exception {
        // Initialize the database
        genaccountRepository.saveAndFlush(genaccount);

		int databaseSizeBeforeDelete = genaccountRepository.findAll().size();

        // Get the genaccount
        restGenaccountMockMvc.perform(delete("/api/genaccounts/{id}", genaccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genaccount> genaccounts = genaccountRepository.findAll();
        assertThat(genaccounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
