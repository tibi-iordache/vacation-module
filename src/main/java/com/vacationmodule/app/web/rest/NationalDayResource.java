package com.vacationmodule.app.web.rest;

import com.vacationmodule.app.repository.NationalDayRepository;
import com.vacationmodule.app.service.NationalDayQueryService;
import com.vacationmodule.app.service.NationalDayService;
import com.vacationmodule.app.service.criteria.NationalDayCriteria;
import com.vacationmodule.app.service.dto.NationalDayDTO;
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
 * REST controller for managing {@link com.vacationmodule.app.domain.NationalDay}.
 */
@RestController
@RequestMapping("/api")
public class NationalDayResource {

    private final Logger log = LoggerFactory.getLogger(NationalDayResource.class);

    private final NationalDayService nationalDayService;

    private final NationalDayRepository nationalDayRepository;

    private final NationalDayQueryService nationalDayQueryService;

    public NationalDayResource(
        NationalDayService nationalDayService,
        NationalDayRepository nationalDayRepository,
        NationalDayQueryService nationalDayQueryService
    ) {
        this.nationalDayService = nationalDayService;
        this.nationalDayRepository = nationalDayRepository;
        this.nationalDayQueryService = nationalDayQueryService;
    }

    /**
     * {@code GET  /national-days} : get all the nationalDays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nationalDays in body.
     */
    @GetMapping("/national-days")
    public ResponseEntity<List<NationalDayDTO>> getAllNationalDays(
        NationalDayCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get NationalDays by criteria: {}", criteria);
        Page<NationalDayDTO> page = nationalDayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /national-days/count} : count all the nationalDays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/national-days/count")
    public ResponseEntity<Long> countNationalDays(NationalDayCriteria criteria) {
        log.debug("REST request to count NationalDays by criteria: {}", criteria);
        return ResponseEntity.ok().body(nationalDayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /national-days/:id} : get the "id" nationalDay.
     *
     * @param id the id of the nationalDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nationalDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/national-days/{id}")
    public ResponseEntity<NationalDayDTO> getNationalDay(@PathVariable Long id) {
        log.debug("REST request to get NationalDay : {}", id);
        Optional<NationalDayDTO> nationalDayDTO = nationalDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nationalDayDTO);
    }
}
