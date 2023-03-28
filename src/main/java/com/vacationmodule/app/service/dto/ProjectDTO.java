package com.vacationmodule.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.vacationmodule.app.domain.Project} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private String code;

    private UserDTO projectManager;

    private UserDTO techLead;
}
