package com.vacationmodule.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A DTO for the {@link com.vacationmodule.app.domain.NationalDay} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NationalDayDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer day;

    @NotNull
    private Integer month;
}
