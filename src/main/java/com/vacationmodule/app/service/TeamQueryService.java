package com.vacationmodule.app.service;

import com.vacationmodule.app.domain.*; // for static metamodels
import com.vacationmodule.app.domain.Team;
import com.vacationmodule.app.repository.TeamRepository;
import com.vacationmodule.app.service.criteria.TeamCriteria;
import com.vacationmodule.app.service.dto.TeamDTO;
import com.vacationmodule.app.service.mapper.TeamMapper;
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
 * Service for executing complex queries for {@link Team} entities in the database.
 * The main input is a {@link TeamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamDTO} or a {@link Page} of {@link TeamDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamQueryService extends QueryService<Team> {

    private final Logger log = LoggerFactory.getLogger(TeamQueryService.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    public TeamQueryService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    /**
     * Return a {@link List} of {@link TeamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamDTO> findByCriteria(TeamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Team> specification = createSpecification(criteria);
        return teamMapper.toDto(teamRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamDTO> findByCriteria(TeamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Team> specification = createSpecification(criteria);
        return teamRepository.findAll(specification, page).map(teamMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeamCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Team> specification = createSpecification(criteria);
        return teamRepository.count(specification);
    }

    /**
     * Function to convert {@link TeamCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Team> createSpecification(TeamCriteria criteria) {
        Specification<Team> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Team_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Team_.name));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(Team_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Team_.users, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
