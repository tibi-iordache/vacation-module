package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.*; // for static metamodels
import com.vacationmodule.app.domain.VacationRequest;
import com.vacationmodule.app.repository.VacationRequestRepository;
import com.vacationmodule.app.service.criteria.VacationRequestCriteria;
import com.vacationmodule.app.service.dto.VacationRequestDTO;
import com.vacationmodule.app.service.mapper.VacationRequestMapper;
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
 * Service for executing complex queries for {@link VacationRequest} entities in the database.
 * The main input is a {@link VacationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VacationRequestDTO} or a {@link Page} of {@link VacationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VacationRequestQueryService extends QueryService<VacationRequest> {

    private final Logger log = LoggerFactory.getLogger(VacationRequestQueryService.class);

    private final VacationRequestRepository vacationRequestRepository;

    private final VacationRequestMapper vacationRequestMapper;

    public VacationRequestQueryService(VacationRequestRepository vacationRequestRepository, VacationRequestMapper vacationRequestMapper) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.vacationRequestMapper = vacationRequestMapper;
    }

    /**
     * Return a {@link List} of {@link VacationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> findByCriteria(VacationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VacationRequest> specification = createSpecification(criteria);
        return vacationRequestMapper.toDto(vacationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VacationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VacationRequestDTO> findByCriteria(VacationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VacationRequest> specification = createSpecification(criteria);
        return vacationRequestRepository.findAll(specification, page).map(vacationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VacationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VacationRequest> specification = createSpecification(criteria);
        return vacationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link VacationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VacationRequest> createSpecification(VacationRequestCriteria criteria) {
        Specification<VacationRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VacationRequest_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), VacationRequest_.description));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), VacationRequest_.date));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(VacationRequest_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getApprobedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApprobedById(),
                            root -> root.join(VacationRequest_.approbedBies, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
