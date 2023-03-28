package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.*; // for static metamodels
import com.vacationmodule.app.domain.NationalDay;
import com.vacationmodule.app.repository.NationalDayRepository;
import com.vacationmodule.app.service.criteria.NationalDayCriteria;
import com.vacationmodule.app.service.dto.NationalDayDTO;
import com.vacationmodule.app.service.mapper.NationalDayMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link NationalDay} entities in the database.
 * The main input is a {@link NationalDayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NationalDayDTO} or a {@link Page} of {@link NationalDayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NationalDayQueryService extends QueryService<NationalDay> {

    private final Logger log = LoggerFactory.getLogger(NationalDayQueryService.class);

    private final NationalDayRepository nationalDayRepository;

    private final NationalDayMapper nationalDayMapper;

    public NationalDayQueryService(NationalDayRepository nationalDayRepository, NationalDayMapper nationalDayMapper) {
        this.nationalDayRepository = nationalDayRepository;
        this.nationalDayMapper = nationalDayMapper;
    }

    /**
     * Return a {@link List} of {@link NationalDayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NationalDayDTO> findByCriteria(NationalDayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NationalDay> specification = createSpecification(criteria);
        return nationalDayMapper.toDto(nationalDayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NationalDayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NationalDayDTO> findByCriteria(NationalDayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NationalDay> specification = createSpecification(criteria);
        return nationalDayRepository.findAll(specification, page).map(nationalDayMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NationalDayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NationalDay> specification = createSpecification(criteria);
        return nationalDayRepository.count(specification);
    }

    /**
     * Function to convert {@link NationalDayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NationalDay> createSpecification(NationalDayCriteria criteria) {
        Specification<NationalDay> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NationalDay_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NationalDay_.name));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), NationalDay_.day));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), NationalDay_.month));
            }
        }
        return specification;
    }
}
