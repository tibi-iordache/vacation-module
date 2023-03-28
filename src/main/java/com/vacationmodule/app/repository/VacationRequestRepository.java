package com.vacationmodule.app.repository;

import com.vacationmodule.app.domain.VacationRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VacationRequest entity.
 *
 * When extending this class, extend VacationRequestRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface VacationRequestRepository
    extends VacationRequestRepositoryWithBagRelationships, JpaRepository<VacationRequest, Long>, JpaSpecificationExecutor<VacationRequest> {
    @Query("select vacationRequest from VacationRequest vacationRequest where vacationRequest.user.login = ?#{principal.username}")
    List<VacationRequest> findByUserIsCurrentUser();

    default Optional<VacationRequest> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<VacationRequest> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<VacationRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct vacationRequest from VacationRequest vacationRequest left join fetch vacationRequest.user",
        countQuery = "select count(distinct vacationRequest) from VacationRequest vacationRequest"
    )
    Page<VacationRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vacationRequest from VacationRequest vacationRequest left join fetch vacationRequest.user")
    List<VacationRequest> findAllWithToOneRelationships();

    @Query("select vacationRequest from VacationRequest vacationRequest left join fetch vacationRequest.user where vacationRequest.id =:id")
    Optional<VacationRequest> findOneWithToOneRelationships(@Param("id") Long id);
}
