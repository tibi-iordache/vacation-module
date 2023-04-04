package com.vacationmodule.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vacationmodule.app.IntegrationTest;
import com.vacationmodule.app.domain.NationalDay;
import com.vacationmodule.app.repository.NationalDayRepository;
import com.vacationmodule.app.service.criteria.NationalDayCriteria;
import com.vacationmodule.app.service.dto.NationalDayDTO;
import com.vacationmodule.app.service.mapper.NationalDayMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NationalDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NationalDayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;
    private static final Integer SMALLER_DAY = 1 - 1;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;
    private static final Integer SMALLER_MONTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/national-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NationalDayRepository nationalDayRepository;

    @Autowired
    private NationalDayMapper nationalDayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNationalDayMockMvc;

    private NationalDay nationalDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NationalDay createEntity(EntityManager em) {
        NationalDay nationalDay = new NationalDay().name(DEFAULT_NAME).day(DEFAULT_DAY).month(DEFAULT_MONTH);
        return nationalDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NationalDay createUpdatedEntity(EntityManager em) {
        NationalDay nationalDay = new NationalDay().name(UPDATED_NAME).day(UPDATED_DAY).month(UPDATED_MONTH);
        return nationalDay;
    }

    @BeforeEach
    public void initTest() {
        nationalDay = createEntity(em);
    }

    @Test
    @Transactional
    void createNationalDay() throws Exception {
        int databaseSizeBeforeCreate = nationalDayRepository.findAll().size();
        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);
        restNationalDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeCreate + 1);
        NationalDay testNationalDay = nationalDayList.get(nationalDayList.size() - 1);
        assertThat(testNationalDay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNationalDay.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testNationalDay.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    void createNationalDayWithExistingId() throws Exception {
        // Create the NationalDay with an existing ID
        nationalDay.setId(1L);
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        int databaseSizeBeforeCreate = nationalDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalDayRepository.findAll().size();
        // set the field null
        nationalDay.setName(null);

        // Create the NationalDay, which fails.
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        restNationalDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalDayRepository.findAll().size();
        // set the field null
        nationalDay.setDay(null);

        // Create the NationalDay, which fails.
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        restNationalDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalDayRepository.findAll().size();
        // set the field null
        nationalDay.setMonth(null);

        // Create the NationalDay, which fails.
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        restNationalDayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNationalDays() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationalDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    void getNationalDay() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get the nationalDay
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL_ID, nationalDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nationalDay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    void getNationalDaysByIdFiltering() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        Long id = nationalDay.getId();

        defaultNationalDayShouldBeFound("id.equals=" + id);
        defaultNationalDayShouldNotBeFound("id.notEquals=" + id);

        defaultNationalDayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNationalDayShouldNotBeFound("id.greaterThan=" + id);

        defaultNationalDayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNationalDayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNationalDaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where name equals to DEFAULT_NAME
        defaultNationalDayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the nationalDayList where name equals to UPDATED_NAME
        defaultNationalDayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalDaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNationalDayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the nationalDayList where name equals to UPDATED_NAME
        defaultNationalDayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalDaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where name is not null
        defaultNationalDayShouldBeFound("name.specified=true");

        // Get all the nationalDayList where name is null
        defaultNationalDayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNationalDaysByNameContainsSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where name contains DEFAULT_NAME
        defaultNationalDayShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the nationalDayList where name contains UPDATED_NAME
        defaultNationalDayShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalDaysByNameNotContainsSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where name does not contain DEFAULT_NAME
        defaultNationalDayShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the nationalDayList where name does not contain UPDATED_NAME
        defaultNationalDayShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day equals to DEFAULT_DAY
        defaultNationalDayShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the nationalDayList where day equals to UPDATED_DAY
        defaultNationalDayShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsInShouldWork() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day in DEFAULT_DAY or UPDATED_DAY
        defaultNationalDayShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the nationalDayList where day equals to UPDATED_DAY
        defaultNationalDayShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day is not null
        defaultNationalDayShouldBeFound("day.specified=true");

        // Get all the nationalDayList where day is null
        defaultNationalDayShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day is greater than or equal to DEFAULT_DAY
        defaultNationalDayShouldBeFound("day.greaterThanOrEqual=" + DEFAULT_DAY);

        // Get all the nationalDayList where day is greater than or equal to UPDATED_DAY
        defaultNationalDayShouldNotBeFound("day.greaterThanOrEqual=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day is less than or equal to DEFAULT_DAY
        defaultNationalDayShouldBeFound("day.lessThanOrEqual=" + DEFAULT_DAY);

        // Get all the nationalDayList where day is less than or equal to SMALLER_DAY
        defaultNationalDayShouldNotBeFound("day.lessThanOrEqual=" + SMALLER_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day is less than DEFAULT_DAY
        defaultNationalDayShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the nationalDayList where day is less than UPDATED_DAY
        defaultNationalDayShouldBeFound("day.lessThan=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where day is greater than DEFAULT_DAY
        defaultNationalDayShouldNotBeFound("day.greaterThan=" + DEFAULT_DAY);

        // Get all the nationalDayList where day is greater than SMALLER_DAY
        defaultNationalDayShouldBeFound("day.greaterThan=" + SMALLER_DAY);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month equals to DEFAULT_MONTH
        defaultNationalDayShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the nationalDayList where month equals to UPDATED_MONTH
        defaultNationalDayShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultNationalDayShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the nationalDayList where month equals to UPDATED_MONTH
        defaultNationalDayShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month is not null
        defaultNationalDayShouldBeFound("month.specified=true");

        // Get all the nationalDayList where month is null
        defaultNationalDayShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month is greater than or equal to DEFAULT_MONTH
        defaultNationalDayShouldBeFound("month.greaterThanOrEqual=" + DEFAULT_MONTH);

        // Get all the nationalDayList where month is greater than or equal to UPDATED_MONTH
        defaultNationalDayShouldNotBeFound("month.greaterThanOrEqual=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month is less than or equal to DEFAULT_MONTH
        defaultNationalDayShouldBeFound("month.lessThanOrEqual=" + DEFAULT_MONTH);

        // Get all the nationalDayList where month is less than or equal to SMALLER_MONTH
        defaultNationalDayShouldNotBeFound("month.lessThanOrEqual=" + SMALLER_MONTH);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month is less than DEFAULT_MONTH
        defaultNationalDayShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the nationalDayList where month is less than UPDATED_MONTH
        defaultNationalDayShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllNationalDaysByMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        // Get all the nationalDayList where month is greater than DEFAULT_MONTH
        defaultNationalDayShouldNotBeFound("month.greaterThan=" + DEFAULT_MONTH);

        // Get all the nationalDayList where month is greater than SMALLER_MONTH
        defaultNationalDayShouldBeFound("month.greaterThan=" + SMALLER_MONTH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNationalDayShouldBeFound(String filter) throws Exception {
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationalDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));

        // Check, that the count call also returns 1
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNationalDayShouldNotBeFound(String filter) throws Exception {
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNationalDayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNationalDay() throws Exception {
        // Get the nationalDay
        restNationalDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNationalDay() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();

        // Update the nationalDay
        NationalDay updatedNationalDay = nationalDayRepository.findById(nationalDay.getId()).get();
        // Disconnect from session so that the updates on updatedNationalDay are not directly saved in db
        em.detach(updatedNationalDay);
        updatedNationalDay.name(UPDATED_NAME).day(UPDATED_DAY).month(UPDATED_MONTH);
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(updatedNationalDay);

        restNationalDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isOk());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
        NationalDay testNationalDay = nationalDayList.get(nationalDayList.size() - 1);
        assertThat(testNationalDay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNationalDay.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testNationalDay.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void putNonExistingNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalDayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalDayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNationalDayWithPatch() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();

        // Update the nationalDay using partial update
        NationalDay partialUpdatedNationalDay = new NationalDay();
        partialUpdatedNationalDay.setId(nationalDay.getId());

        partialUpdatedNationalDay.day(UPDATED_DAY).month(UPDATED_MONTH);

        restNationalDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationalDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationalDay))
            )
            .andExpect(status().isOk());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
        NationalDay testNationalDay = nationalDayList.get(nationalDayList.size() - 1);
        assertThat(testNationalDay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNationalDay.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testNationalDay.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void fullUpdateNationalDayWithPatch() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();

        // Update the nationalDay using partial update
        NationalDay partialUpdatedNationalDay = new NationalDay();
        partialUpdatedNationalDay.setId(nationalDay.getId());

        partialUpdatedNationalDay.name(UPDATED_NAME).day(UPDATED_DAY).month(UPDATED_MONTH);

        restNationalDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationalDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationalDay))
            )
            .andExpect(status().isOk());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
        NationalDay testNationalDay = nationalDayList.get(nationalDayList.size() - 1);
        assertThat(testNationalDay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNationalDay.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testNationalDay.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void patchNonExistingNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nationalDayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNationalDay() throws Exception {
        int databaseSizeBeforeUpdate = nationalDayRepository.findAll().size();
        nationalDay.setId(count.incrementAndGet());

        // Create the NationalDay
        NationalDayDTO nationalDayDTO = nationalDayMapper.toDto(nationalDay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nationalDayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NationalDay in the database
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNationalDay() throws Exception {
        // Initialize the database
        nationalDayRepository.saveAndFlush(nationalDay);

        int databaseSizeBeforeDelete = nationalDayRepository.findAll().size();

        // Delete the nationalDay
        restNationalDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, nationalDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NationalDay> nationalDayList = nationalDayRepository.findAll();
        assertThat(nationalDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
