package com.ciwise.mudskipper.web.rest;

import com.ciwise.mudskipper.Application;
import com.ciwise.mudskipper.domain.Genacctentry;
import com.ciwise.mudskipper.repository.GenacctentryRepository;
import com.ciwise.mudskipper.repository.search.GenacctentrySearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GenacctentryResource REST controller.
 *
 * @see GenacctentryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GenacctentryResourceIntTest {

    private static final String DEFAULT_ENTRYTEXT = "AAAAA";
    private static final String UPDATED_ENTRYTEXT = "BBBBB";

    private static final LocalDate DEFAULT_ENTRYDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRYDATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_DEBIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT = new BigDecimal(2);

    @Inject
    private GenacctentryRepository genacctentryRepository;

    @Inject
    private GenacctentrySearchRepository genacctentrySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGenacctentryMockMvc;

    private Genacctentry genacctentry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenacctentryResource genacctentryResource = new GenacctentryResource();
        ReflectionTestUtils.setField(genacctentryResource, "genacctentrySearchRepository", genacctentrySearchRepository);
        ReflectionTestUtils.setField(genacctentryResource, "genacctentryRepository", genacctentryRepository);
        this.restGenacctentryMockMvc = MockMvcBuilders.standaloneSetup(genacctentryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        genacctentry = new Genacctentry();
        genacctentry.setEntrytext(DEFAULT_ENTRYTEXT);
        genacctentry.setEntrydate(DEFAULT_ENTRYDATE);
        genacctentry.setDebit(DEFAULT_DEBIT);
        genacctentry.setCredit(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    public void createGenacctentry() throws Exception {
        int databaseSizeBeforeCreate = genacctentryRepository.findAll().size();

        // Create the Genacctentry

        restGenacctentryMockMvc.perform(post("/api/genacctentrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacctentry)))
                .andExpect(status().isCreated());

        // Validate the Genacctentry in the database
        List<Genacctentry> genacctentrys = genacctentryRepository.findAll();
        assertThat(genacctentrys).hasSize(databaseSizeBeforeCreate + 1);
        Genacctentry testGenacctentry = genacctentrys.get(genacctentrys.size() - 1);
        assertThat(testGenacctentry.getEntrytext()).isEqualTo(DEFAULT_ENTRYTEXT);
        assertThat(testGenacctentry.getEntrydate()).isEqualTo(DEFAULT_ENTRYDATE);
        assertThat(testGenacctentry.getDebit()).isEqualTo(DEFAULT_DEBIT);
        assertThat(testGenacctentry.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    public void checkEntrytextIsRequired() throws Exception {
        int databaseSizeBeforeTest = genacctentryRepository.findAll().size();
        // set the field null
        genacctentry.setEntrytext(null);

        // Create the Genacctentry, which fails.

        restGenacctentryMockMvc.perform(post("/api/genacctentrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacctentry)))
                .andExpect(status().isBadRequest());

        List<Genacctentry> genacctentrys = genacctentryRepository.findAll();
        assertThat(genacctentrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntrydateIsRequired() throws Exception {
        int databaseSizeBeforeTest = genacctentryRepository.findAll().size();
        // set the field null
        genacctentry.setEntrydate(null);

        // Create the Genacctentry, which fails.

        restGenacctentryMockMvc.perform(post("/api/genacctentrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacctentry)))
                .andExpect(status().isBadRequest());

        List<Genacctentry> genacctentrys = genacctentryRepository.findAll();
        assertThat(genacctentrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenacctentrys() throws Exception {
        // Initialize the database
        genacctentryRepository.saveAndFlush(genacctentry);

        // Get all the genacctentrys
        restGenacctentryMockMvc.perform(get("/api/genacctentrys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genacctentry.getId().intValue())))
                .andExpect(jsonPath("$.[*].entrytext").value(hasItem(DEFAULT_ENTRYTEXT.toString())))
                .andExpect(jsonPath("$.[*].entrydate").value(hasItem(DEFAULT_ENTRYDATE.toString())))
                .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.intValue())))
                .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.intValue())));
    }

    @Test
    @Transactional
    public void getGenacctentry() throws Exception {
        // Initialize the database
        genacctentryRepository.saveAndFlush(genacctentry);

        // Get the genacctentry
        restGenacctentryMockMvc.perform(get("/api/genacctentrys/{id}", genacctentry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(genacctentry.getId().intValue()))
            .andExpect(jsonPath("$.entrytext").value(DEFAULT_ENTRYTEXT.toString()))
            .andExpect(jsonPath("$.entrydate").value(DEFAULT_ENTRYDATE.toString()))
            .andExpect(jsonPath("$.debit").value(DEFAULT_DEBIT.intValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGenacctentry() throws Exception {
        // Get the genacctentry
        restGenacctentryMockMvc.perform(get("/api/genacctentrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenacctentry() throws Exception {
        // Initialize the database
        genacctentryRepository.saveAndFlush(genacctentry);

		int databaseSizeBeforeUpdate = genacctentryRepository.findAll().size();

        // Update the genacctentry
        genacctentry.setEntrytext(UPDATED_ENTRYTEXT);
        genacctentry.setEntrydate(UPDATED_ENTRYDATE);
        genacctentry.setDebit(UPDATED_DEBIT);
        genacctentry.setCredit(UPDATED_CREDIT);

        restGenacctentryMockMvc.perform(put("/api/genacctentrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genacctentry)))
                .andExpect(status().isOk());

        // Validate the Genacctentry in the database
        List<Genacctentry> genacctentrys = genacctentryRepository.findAll();
        assertThat(genacctentrys).hasSize(databaseSizeBeforeUpdate);
        Genacctentry testGenacctentry = genacctentrys.get(genacctentrys.size() - 1);
        assertThat(testGenacctentry.getEntrytext()).isEqualTo(UPDATED_ENTRYTEXT);
        assertThat(testGenacctentry.getEntrydate()).isEqualTo(UPDATED_ENTRYDATE);
        assertThat(testGenacctentry.getDebit()).isEqualTo(UPDATED_DEBIT);
        assertThat(testGenacctentry.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void deleteGenacctentry() throws Exception {
        // Initialize the database
        genacctentryRepository.saveAndFlush(genacctentry);

		int databaseSizeBeforeDelete = genacctentryRepository.findAll().size();

        // Get the genacctentry
        restGenacctentryMockMvc.perform(delete("/api/genacctentrys/{id}", genacctentry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genacctentry> genacctentrys = genacctentryRepository.findAll();
        assertThat(genacctentrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
