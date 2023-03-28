package com.vacationmodule.app.repository;

import com.vacationmodule.app.domain.VacationRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VacationRequestRepositoryWithBagRelationshipsImpl implements VacationRequestRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VacationRequest> fetchBagRelationships(Optional<VacationRequest> vacationRequest) {
        return vacationRequest.map(this::fetchApprobedBies);
    }

    @Override
    public Page<VacationRequest> fetchBagRelationships(Page<VacationRequest> vacationRequests) {
        return new PageImpl<>(
            fetchBagRelationships(vacationRequests.getContent()),
            vacationRequests.getPageable(),
            vacationRequests.getTotalElements()
        );
    }

    @Override
    public List<VacationRequest> fetchBagRelationships(List<VacationRequest> vacationRequests) {
        return Optional.of(vacationRequests).map(this::fetchApprobedBies).orElse(Collections.emptyList());
    }

    VacationRequest fetchApprobedBies(VacationRequest result) {
        return entityManager
            .createQuery(
                "select vacationRequest from VacationRequest vacationRequest left join fetch vacationRequest.approbedBies where vacationRequest is :vacationRequest",
                VacationRequest.class
            )
            .setParameter("vacationRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<VacationRequest> fetchApprobedBies(List<VacationRequest> vacationRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vacationRequests.size()).forEach(index -> order.put(vacationRequests.get(index).getId(), index));
        List<VacationRequest> result = entityManager
            .createQuery(
                "select distinct vacationRequest from VacationRequest vacationRequest left join fetch vacationRequest.approbedBies where vacationRequest in :vacationRequests",
                VacationRequest.class
            )
            .setParameter("vacationRequests", vacationRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
