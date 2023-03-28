package com.vacationmodule.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.vacationmodule.app.domain.VacationRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VacationRequestDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private Instant date;

    private UserDTO user;

    private Set<UserDTO> approbedBies = new HashSet<>();
}
