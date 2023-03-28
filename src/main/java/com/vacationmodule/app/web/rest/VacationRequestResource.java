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
}
