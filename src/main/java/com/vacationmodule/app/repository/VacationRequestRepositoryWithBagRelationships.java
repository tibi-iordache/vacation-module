package com.vacationmodule.app.repository;

import com.vacationmodule.app.domain.VacationRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VacationRequestRepositoryWithBagRelationships {
    Optional<VacationRequest> fetchBagRelationships(Optional<VacationRequest> vacationRequest);

    List<VacationRequest> fetchBagRelationships(List<VacationRequest> vacationRequests);

    Page<VacationRequest> fetchBagRelationships(Page<VacationRequest> vacationRequests);
}
