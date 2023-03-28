package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.NationalDay;
import com.vacationmodule.app.repository.NationalDayRepository;
import com.vacationmodule.app.service.dto.NationalDayDTO;
import com.vacationmodule.app.service.mapper.NationalDayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NationalDay}.
 */
@Service
@Transactional
public class NationalDayService {

    private final Logger log = LoggerFactory.getLogger(NationalDayService.class);

    private final NationalDayRepository nationalDayRepository;

    private final NationalDayMapper nationalDayMapper;

    public NationalDayService(NationalDayRepository nationalDayRepository, NationalDayMapper nationalDayMapper) {
        this.nationalDayRepository = nationalDayRepository;
        this.nationalDayMapper = nationalDayMapper;
    }

    /**
     * Save a nationalDay.
     *
     * @param nationalDayDTO the entity to save.
     * @return the persisted entity.
     */
    public NationalDayDTO save(NationalDayDTO nationalDayDTO) {
        log.debug("Request to save NationalDay : {}", nationalDayDTO);
        NationalDay nationalDay = nationalDayMapper.toEntity(nationalDayDTO);
        nationalDay = nationalDayRepository.save(nationalDay);
        return nationalDayMapper.toDto(nationalDay);
    }

    /**
     * Update a nationalDay.
     *
     * @param nationalDayDTO the entity to save.
     * @return the persisted entity.
     */
    public NationalDayDTO update(NationalDayDTO nationalDayDTO) {
        log.debug("Request to update NationalDay : {}", nationalDayDTO);
        NationalDay nationalDay = nationalDayMapper.toEntity(nationalDayDTO);
        nationalDay = nationalDayRepository.save(nationalDay);
        return nationalDayMapper.toDto(nationalDay);
    }

    /**
     * Partially update a nationalDay.
     *
     * @param nationalDayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NationalDayDTO> partialUpdate(NationalDayDTO nationalDayDTO) {
        log.debug("Request to partially update NationalDay : {}", nationalDayDTO);

        return nationalDayRepository
            .findById(nationalDayDTO.getId())
            .map(existingNationalDay -> {
                nationalDayMapper.partialUpdate(existingNationalDay, nationalDayDTO);

                return existingNationalDay;
            })
            .map(nationalDayRepository::save)
            .map(nationalDayMapper::toDto);
    }

    /**
     * Get all the nationalDays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NationalDayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NationalDays");
        return nationalDayRepository.findAll(pageable).map(nationalDayMapper::toDto);
    }

    /**
     * Get one nationalDay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NationalDayDTO> findOne(Long id) {
        log.debug("Request to get NationalDay : {}", id);
        return nationalDayRepository.findById(id).map(nationalDayMapper::toDto);
    }

    /**
     * Delete the nationalDay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NationalDay : {}", id);
        nationalDayRepository.deleteById(id);
    }
}
