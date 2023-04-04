package com.vacationmodule.app.web.rest;

import com.vacationmodule.app.repository.VacationRequestRepository;
import com.vacationmodule.app.service.VacationRequestQueryService;
import com.vacationmodule.app.service.VacationRequestService;
import com.vacationmodule.app.service.criteria.VacationRequestCriteria;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import com.vacationmodule.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.vacationmodule.app.domain.VacationRequest}.
 */
@RestController
@RequestMapping("/api")
public class VacationRequestResource {

    private final Logger log = LoggerFactory.getLogger(VacationRequestResource.class);

    private static final String ENTITY_NAME = "vacationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacationRequestService vacationRequestService;

    private final VacationRequestRepository vacationRequestRepository;

    private final VacationRequestQueryService vacationRequestQueryService;

    public VacationRequestResource(
        VacationRequestService vacationRequestService,
        VacationRequestRepository vacationRequestRepository,
        VacationRequestQueryService vacationRequestQueryService
    ) {
        this.vacationRequestService = vacationRequestService;
        this.vacationRequestRepository = vacationRequestRepository;
        this.vacationRequestQueryService = vacationRequestQueryService;
    }

    /**
     * {@code POST  /vacation-requests} : Create a new vacationRequest.
     *
     * @param vacationRequestDTO the vacationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacationRequestDTO, or with status {@code 400 (Bad Request)} if the vacationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacation-requests")
    public ResponseEntity<VacationRequestDTO> createVacationRequest(@Valid @RequestBody VacationRequestDTO vacationRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save VacationRequest : {}", vacationRequestDTO);
        if (vacationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new vacationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacationRequestDTO result = vacationRequestService.save(vacationRequestDTO);
        return ResponseEntity
            .created(new URI("/api/vacation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacation-requests/:id} : Updates an existing vacationRequest.
     *
     * @param id the id of the vacationRequestDTO to save.
     * @param vacationRequestDTO the vacationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the vacationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacation-requests/{id}")
    public ResponseEntity<VacationRequestDTO> updateVacationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VacationRequestDTO vacationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VacationRequest : {}, {}", id, vacationRequestDTO);
        if (vacationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vacationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vacationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VacationRequestDTO result = vacationRequestService.update(vacationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vacationRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vacation-requests/:id} : Partial updates given fields of an existing vacationRequest, field will ignore if it is null
     *
     * @param id the id of the vacationRequestDTO to save.
     * @param vacationRequestDTO the vacationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the vacationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vacationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vacationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vacation-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VacationRequestDTO> partialUpdateVacationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VacationRequestDTO vacationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VacationRequest partially : {}, {}", id, vacationRequestDTO);
        if (vacationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vacationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vacationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VacationRequestDTO> result = vacationRequestService.partialUpdate(vacationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vacationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vacation-requests} : get all the vacationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacationRequests in body.
     */
    @GetMapping("/vacation-requests")
    public ResponseEntity<List<VacationRequestDTO>> getAllVacationRequests(
        VacationRequestCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get VacationRequests by criteria: {}", criteria);
        Page<VacationRequestDTO> page = vacationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacation-requests/count} : count all the vacationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vacation-requests/count")
    public ResponseEntity<Long> countVacationRequests(VacationRequestCriteria criteria) {
        log.debug("REST request to count VacationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(vacationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vacation-requests/:id} : get the "id" vacationRequest.
     *
     * @param id the id of the vacationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacation-requests/{id}")
    public ResponseEntity<VacationRequestDTO> getVacationRequest(@PathVariable Long id) {
        log.debug("REST request to get VacationRequest : {}", id);
        Optional<VacationRequestDTO> vacationRequestDTO = vacationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacationRequestDTO);
    }

    /**
     * {@code DELETE  /vacation-requests/:id} : delete the "id" vacationRequest.
     *
     * @param id the id of the vacationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacation-requests/{id}")
    public ResponseEntity<Void> deleteVacationRequest(@PathVariable Long id) {
        log.debug("REST request to delete VacationRequest : {}", id);
        vacationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
