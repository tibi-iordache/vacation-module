package com.vacationmodule.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A NationalDay.
 */
@Data
@Entity
@Table(name = "national_day")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NationalDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "day", nullable = false)
    private Integer day;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;
}
