package com.vacationmodule.app.repository;

import com.vacationmodule.app.domain.NationalDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NationalDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalDayRepository extends JpaRepository<NationalDay, Long>, JpaSpecificationExecutor<NationalDay> {}
