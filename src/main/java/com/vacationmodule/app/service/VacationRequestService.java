package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.VacationRequest;
import com.vacationmodule.app.repository.VacationRequestRepository;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import com.vacationmodule.app.service.mapper.VacationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VacationRequest}.
 */
@Service
@Transactional
public class VacationRequestService {

    private final Logger log = LoggerFactory.getLogger(VacationRequestService.class);

    private final VacationRequestRepository vacationRequestRepository;

    private final VacationRequestMapper vacationRequestMapper;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, VacationRequestMapper vacationRequestMapper) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.vacationRequestMapper = vacationRequestMapper;
    }

    /**
     * Save a vacationRequest.
     *
     * @param vacationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public VacationRequestDTO save(VacationRequestDTO vacationRequestDTO) {
        log.debug("Request to save VacationRequest : {}", vacationRequestDTO);
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        vacationRequest = vacationRequestRepository.save(vacationRequest);
        return vacationRequestMapper.toDto(vacationRequest);
    }

    /**
     * Update a vacationRequest.
     *
     * @param vacationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public VacationRequestDTO update(VacationRequestDTO vacationRequestDTO) {
        log.debug("Request to update VacationRequest : {}", vacationRequestDTO);
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        vacationRequest = vacationRequestRepository.save(vacationRequest);
        return vacationRequestMapper.toDto(vacationRequest);
    }

    /**
     * Partially update a vacationRequest.
     *
     * @param vacationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VacationRequestDTO> partialUpdate(VacationRequestDTO vacationRequestDTO) {
        log.debug("Request to partially update VacationRequest : {}", vacationRequestDTO);

        return vacationRequestRepository
            .findById(vacationRequestDTO.getId())
            .map(existingVacationRequest -> {
                vacationRequestMapper.partialUpdate(existingVacationRequest, vacationRequestDTO);

                return existingVacationRequest;
            })
            .map(vacationRequestRepository::save)
            .map(vacationRequestMapper::toDto);
    }

    /**
     * Get all the vacationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VacationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VacationRequests");
        return vacationRequestRepository.findAll(pageable).map(vacationRequestMapper::toDto);
    }

    /**
     * Get all the vacationRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VacationRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vacationRequestRepository.findAllWithEagerRelationships(pageable).map(vacationRequestMapper::toDto);
    }

    /**
     * Get one vacationRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VacationRequestDTO> findOne(Long id) {
        log.debug("Request to get VacationRequest : {}", id);
        return vacationRequestRepository.findOneWithEagerRelationships(id).map(vacationRequestMapper::toDto);
    }

    /**
     * Delete the vacationRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VacationRequest : {}", id);
        vacationRequestRepository.deleteById(id);
    }
}
