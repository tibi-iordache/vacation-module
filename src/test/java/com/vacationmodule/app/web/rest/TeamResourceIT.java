package com.vacationmodule.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vacationmodule.app.IntegrationTest;
import com.vacationmodule.app.domain.Project;
import com.vacationmodule.app.domain.Team;
import com.vacationmodule.app.domain.User;
import com.vacationmodule.app.repository.TeamRepository;
import com.vacationmodule.app.service.TeamService;
import com.vacationmodule.app.service.criteria.TeamCriteria;
import com.vacationmodule.app.service.dto.TeamDTO;
import com.vacationmodule.app.service.mapper.TeamMapper;
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
 * Integration tests for the {@link TeamResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeamRepository teamRepository;

    @Mock
    private TeamRepository teamRepositoryMock;

    @Autowired
    private TeamMapper teamMapper;

    @Mock
    private TeamService teamServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamMockMvc;

    private Team team;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createEntity(EntityManager em) {
        Team team = new Team().name(DEFAULT_NAME);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        team.setProject(project);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        team.getUsers().add(user);
        return team;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createUpdatedEntity(EntityManager em) {
        Team team = new Team().name(UPDATED_NAME);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        team.setProject(project);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        team.getUsers().add(user);
        return team;
    }

    @BeforeEach
    public void initTest() {
        team = createEntity(em);
    }

    @Test
    @Transactional
    void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamsWithEagerRelationshipsIsEnabled() throws Exception {
        when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(teamRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTeamsByIdFiltering() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        Long id = team.getId();

        defaultTeamShouldBeFound("id.equals=" + id);
        defaultTeamShouldNotBeFound("id.notEquals=" + id);

        defaultTeamShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeamShouldNotBeFound("id.greaterThan=" + id);

        defaultTeamShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeamShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTeamsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name equals to DEFAULT_NAME
        defaultTeamShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the teamList where name equals to UPDATED_NAME
        defaultTeamShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeamsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTeamShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the teamList where name equals to UPDATED_NAME
        defaultTeamShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeamsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name is not null
        defaultTeamShouldBeFound("name.specified=true");

        // Get all the teamList where name is null
        defaultTeamShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTeamsByNameContainsSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name contains DEFAULT_NAME
        defaultTeamShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the teamList where name contains UPDATED_NAME
        defaultTeamShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeamsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name does not contain DEFAULT_NAME
        defaultTeamShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the teamList where name does not contain UPDATED_NAME
        defaultTeamShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeamsByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            teamRepository.saveAndFlush(team);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        team.setProject(project);
        teamRepository.saveAndFlush(team);
        Long projectId = project.getId();

        // Get all the teamList where project equals to projectId
        defaultTeamShouldBeFound("projectId.equals=" + projectId);

        // Get all the teamList where project equals to (projectId + 1)
        defaultTeamShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllTeamsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            teamRepository.saveAndFlush(team);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        team.addUser(user);
        teamRepository.saveAndFlush(team);
        Long userId = user.getId();

        // Get all the teamList where user equals to userId
        defaultTeamShouldBeFound("userId.equals=" + userId);

        // Get all the teamList where user equals to (userId + 1)
        defaultTeamShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamShouldBeFound(String filter) throws Exception {
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamShouldNotBeFound(String filter) throws Exception {
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
