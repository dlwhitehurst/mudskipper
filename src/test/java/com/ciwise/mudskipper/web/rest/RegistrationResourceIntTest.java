package com.ciwise.mudskipper.web.rest;

import com.ciwise.mudskipper.Application;
import com.ciwise.mudskipper.domain.Registration;
import com.ciwise.mudskipper.repository.RegistrationRepository;
import com.ciwise.mudskipper.repository.search.RegistrationSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RegistrationResource REST controller.
 *
 * @see RegistrationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RegistrationResourceIntTest {

    private static final String DEFAULT_ENTITY = "AAAAA";
    private static final String UPDATED_ENTITY = "BBBBB";
    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RegistrationRepository registrationRepository;

    @Inject
    private RegistrationSearchRepository registrationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRegistrationMockMvc;

    private Registration registration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistrationResource registrationResource = new RegistrationResource();
        ReflectionTestUtils.setField(registrationResource, "registrationSearchRepository", registrationSearchRepository);
        ReflectionTestUtils.setField(registrationResource, "registrationRepository", registrationRepository);
        this.restRegistrationMockMvc = MockMvcBuilders.standaloneSetup(registrationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        registration = new Registration();
        registration.setEntity(DEFAULT_ENTITY);
        registration.setUsername(DEFAULT_USERNAME);
        registration.setPassword(DEFAULT_PASSWORD);
        registration.setEmail(DEFAULT_EMAIL);
        registration.setCreated(DEFAULT_CREATED);
        registration.setModified(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    public void createRegistration() throws Exception {
        int databaseSizeBeforeCreate = registrationRepository.findAll().size();

        // Create the Registration

        restRegistrationMockMvc.perform(post("/api/registrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(registration)))
                .andExpect(status().isCreated());

        // Validate the Registration in the database
        List<Registration> registrations = registrationRepository.findAll();
        assertThat(registrations).hasSize(databaseSizeBeforeCreate + 1);
        Registration testRegistration = registrations.get(registrations.size() - 1);
        assertThat(testRegistration.getEntity()).isEqualTo(DEFAULT_ENTITY);
        assertThat(testRegistration.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testRegistration.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testRegistration.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRegistration.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRegistration.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    public void getAllRegistrations() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get all the registrations
        restRegistrationMockMvc.perform(get("/api/registrations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(registration.getId().intValue())))
                .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
                .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    public void getRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", registration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(registration.getId().intValue()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_ENTITY.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistration() throws Exception {
        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

		int databaseSizeBeforeUpdate = registrationRepository.findAll().size();

        // Update the registration
        registration.setEntity(UPDATED_ENTITY);
        registration.setUsername(UPDATED_USERNAME);
        registration.setPassword(UPDATED_PASSWORD);
        registration.setEmail(UPDATED_EMAIL);
        registration.setCreated(UPDATED_CREATED);
        registration.setModified(UPDATED_MODIFIED);

        restRegistrationMockMvc.perform(put("/api/registrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(registration)))
                .andExpect(status().isOk());

        // Validate the Registration in the database
        List<Registration> registrations = registrationRepository.findAll();
        assertThat(registrations).hasSize(databaseSizeBeforeUpdate);
        Registration testRegistration = registrations.get(registrations.size() - 1);
        assertThat(testRegistration.getEntity()).isEqualTo(UPDATED_ENTITY);
        assertThat(testRegistration.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testRegistration.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testRegistration.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRegistration.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRegistration.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    public void deleteRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

		int databaseSizeBeforeDelete = registrationRepository.findAll().size();

        // Get the registration
        restRegistrationMockMvc.perform(delete("/api/registrations/{id}", registration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Registration> registrations = registrationRepository.findAll();
        assertThat(registrations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
