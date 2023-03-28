package com.vacationmodule.app.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.vacationmodule.app.domain.Team} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamDTO implements Serializable {

    private Long id;

    private String name;

    private ProjectDTO project;

    private Set<UserDTO> users = new HashSet<>();
}
