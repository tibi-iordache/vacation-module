package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.NationalDay;
import com.vacationmodule.app.domain.VacationRequest;
import com.vacationmodule.app.exception.NationalDayException;
import com.vacationmodule.app.exception.VacationDaysNumberExceededException;
import com.vacationmodule.app.exception.VacationRequestDuplicateException;
import com.vacationmodule.app.exception.WeekdayException;
import com.vacationmodule.app.repository.VacationRequestRepository;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import com.vacationmodule.app.service.mapper.VacationRequestMapper;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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

    private final NationalDayService nationalDayService;

    private final Integer MAX_VACATIONS_PER_YEAR = 25;

    public VacationRequestService(
        VacationRequestRepository vacationRequestRepository,
        VacationRequestMapper vacationRequestMapper,
        NationalDayService nationalDayService
    ) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.vacationRequestMapper = vacationRequestMapper;
        this.nationalDayService = nationalDayService;
    }

    /**
     * Save a vacationRequest.
     *
     * @param vacationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public VacationRequestDTO save(VacationRequestDTO vacationRequestDTO)
        throws NationalDayException, VacationDaysNumberExceededException, VacationRequestDuplicateException, WeekdayException {
        log.debug("Request to save VacationRequest : {}", vacationRequestDTO);
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);

        checkVacationDaysLeftForThisYear(vacationRequest);
        checkForNationalDay(vacationRequest);
        checkVacationRequestAlreadyExists(vacationRequest);
        checkForWeekday(vacationRequest);

        vacationRequest = vacationRequestRepository.save(vacationRequest);
        return vacationRequestMapper.toDto(vacationRequest);
    }

    /**
     * Update a vacationRequest.
     *
     * @param vacationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public VacationRequestDTO update(VacationRequestDTO vacationRequestDTO)
        throws NationalDayException, VacationRequestDuplicateException, WeekdayException {
        log.debug("Request to update VacationRequest : {}", vacationRequestDTO);
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);

        checkForNationalDay(vacationRequest);
        checkVacationRequestAlreadyExists(vacationRequest);
        checkForWeekday(vacationRequest);

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

    public Integer getAvailableVacationRequestsForYear(Long userId, Integer year) {
        List<VacationRequest> userVacations = vacationRequestRepository.findAllByUser_Id(userId);

        Integer vacationsThisYear = userVacations
            .stream()
            .filter(userVacationRequest -> userVacationRequest.getDate().atZone(ZoneId.systemDefault()).getYear() == year)
            .toList()
            .size();

        return MAX_VACATIONS_PER_YEAR - vacationsThisYear;
    }

    private void checkVacationDaysLeftForThisYear(VacationRequest vacationRequest) throws VacationDaysNumberExceededException {
        ZonedDateTime zonedDateTime = vacationRequest.getDate().atZone(ZoneId.systemDefault());

        int year = zonedDateTime.getYear();

        List<VacationRequest> userVacations = vacationRequestRepository.findAllByUser_Id(vacationRequest.getUser().getId());

        int vacationsThisYear = userVacations
            .stream()
            .filter(userVacationRequest -> userVacationRequest.getDate().atZone(ZoneId.systemDefault()).getYear() == year)
            .toList()
            .size();

        if (vacationsThisYear == MAX_VACATIONS_PER_YEAR) {
            throw new VacationDaysNumberExceededException();
        }
    }

    private void checkForNationalDay(VacationRequest vacationRequest) throws NationalDayException {
        List<NationalDay> nationalDayList = nationalDayService.findAllNotPageable();

        for (var nationalDay : nationalDayList) {
            ZonedDateTime zonedDateTime = vacationRequest.getDate().atZone(ZoneId.systemDefault());

            Integer month = zonedDateTime.getMonthValue();
            Integer day = zonedDateTime.getDayOfMonth();
            if (nationalDay.getMonth().equals(month) && nationalDay.getDay().equals(day)) {
                throw new NationalDayException();
            }
        }
    }

    private void checkVacationRequestAlreadyExists(VacationRequest vacationRequest) throws VacationRequestDuplicateException {
        ZonedDateTime zonedDateTime = vacationRequest.getDate().atZone(ZoneId.systemDefault());
        Integer requestDay = zonedDateTime.getDayOfMonth();
        Integer requestMonth = zonedDateTime.getMonthValue();
        int requestYear = zonedDateTime.getYear();

        List<VacationRequest> userVacations = vacationRequestRepository.findAllByUser_Id(vacationRequest.getUser().getId());

        for (var userVacation : userVacations) {
            ZonedDateTime vacationZonedDateTime = userVacation.getDate().atZone(ZoneId.systemDefault());

            Integer vacationMonth = vacationZonedDateTime.getMonthValue();
            Integer vacationDay = vacationZonedDateTime.getDayOfMonth();
            int vacationYear = vacationZonedDateTime.getYear();

            if (requestDay.equals(vacationDay) && requestMonth.equals(vacationMonth) && requestYear == vacationYear) {
                throw new VacationRequestDuplicateException();
            }
        }
    }

    private void checkForWeekday(VacationRequest vacationRequest) throws WeekdayException {
        ZonedDateTime zonedDateTime = vacationRequest.getDate().atZone(ZoneId.systemDefault());

        DayOfWeek dayOfWeek = zonedDateTime.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new WeekdayException();
        }
    }
}
