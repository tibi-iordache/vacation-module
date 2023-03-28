package com.vacationmodule.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vacationmodule.app.IntegrationTest;
import com.vacationmodule.app.domain.User;
import com.vacationmodule.app.domain.VacationRequest;
import com.vacationmodule.app.repository.VacationRequestRepository;
import com.vacationmodule.app.service.VacationRequestService;
import com.vacationmodule.app.service.criteria.VacationRequestCriteria;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import com.vacationmodule.app.service.mapper.VacationRequestMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VacationRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VacationRequestResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vacation-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Mock
    private VacationRequestRepository vacationRequestRepositoryMock;

    @Autowired
    private VacationRequestMapper vacationRequestMapper;

    @Mock
    private VacationRequestService vacationRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacationRequestMockMvc;

    private VacationRequest vacationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacationRequest createEntity(EntityManager em) {
        VacationRequest vacationRequest = new VacationRequest().description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        vacationRequest.setUser(user);
        // Add required entity
        vacationRequest.getApprobedBies().add(user);
        return vacationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VacationRequest createUpdatedEntity(EntityManager em) {
        VacationRequest vacationRequest = new VacationRequest().description(UPDATED_DESCRIPTION).date(UPDATED_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        vacationRequest.setUser(user);
        // Add required entity
        vacationRequest.getApprobedBies().add(user);
        return vacationRequest;
    }

    @BeforeEach
    public void initTest() {
        vacationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void getAllVacationRequests() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVacationRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vacationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVacationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vacationRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVacationRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vacationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVacationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vacationRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVacationRequest() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get the vacationRequest
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, vacationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacationRequest.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getVacationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        Long id = vacationRequest.getId();

        defaultVacationRequestShouldBeFound("id.equals=" + id);
        defaultVacationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultVacationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVacationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultVacationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVacationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where description equals to DEFAULT_DESCRIPTION
        defaultVacationRequestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the vacationRequestList where description equals to UPDATED_DESCRIPTION
        defaultVacationRequestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVacationRequestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the vacationRequestList where description equals to UPDATED_DESCRIPTION
        defaultVacationRequestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where description is not null
        defaultVacationRequestShouldBeFound("description.specified=true");

        // Get all the vacationRequestList where description is null
        defaultVacationRequestShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where description contains DEFAULT_DESCRIPTION
        defaultVacationRequestShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the vacationRequestList where description contains UPDATED_DESCRIPTION
        defaultVacationRequestShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where description does not contain DEFAULT_DESCRIPTION
        defaultVacationRequestShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the vacationRequestList where description does not contain UPDATED_DESCRIPTION
        defaultVacationRequestShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where date equals to DEFAULT_DATE
        defaultVacationRequestShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the vacationRequestList where date equals to UPDATED_DATE
        defaultVacationRequestShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where date in DEFAULT_DATE or UPDATED_DATE
        defaultVacationRequestShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the vacationRequestList where date equals to UPDATED_DATE
        defaultVacationRequestShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllVacationRequestsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacationRequestRepository.saveAndFlush(vacationRequest);

        // Get all the vacationRequestList where date is not null
        defaultVacationRequestShouldBeFound("date.specified=true");

        // Get all the vacationRequestList where date is null
        defaultVacationRequestShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllVacationRequestsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            vacationRequestRepository.saveAndFlush(vacationRequest);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        vacationRequest.setUser(user);
        vacationRequestRepository.saveAndFlush(vacationRequest);
        Long userId = user.getId();

        // Get all the vacationRequestList where user equals to userId
        defaultVacationRequestShouldBeFound("userId.equals=" + userId);

        // Get all the vacationRequestList where user equals to (userId + 1)
        defaultVacationRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllVacationRequestsByApprobedByIsEqualToSomething() throws Exception {
        User approbedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            vacationRequestRepository.saveAndFlush(vacationRequest);
            approbedBy = UserResourceIT.createEntity(em);
        } else {
            approbedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(approbedBy);
        em.flush();
        vacationRequest.addApprobedBy(approbedBy);
        vacationRequestRepository.saveAndFlush(vacationRequest);
        Long approbedById = approbedBy.getId();

        // Get all the vacationRequestList where approbedBy equals to approbedById
        defaultVacationRequestShouldBeFound("approbedById.equals=" + approbedById);

        // Get all the vacationRequestList where approbedBy equals to (approbedById + 1)
        defaultVacationRequestShouldNotBeFound("approbedById.equals=" + (approbedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVacationRequestShouldBeFound(String filter) throws Exception {
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVacationRequestShouldNotBeFound(String filter) throws Exception {
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVacationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVacationRequest() throws Exception {
        // Get the vacationRequest
        restVacationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
