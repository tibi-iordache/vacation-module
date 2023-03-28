package com.vacationmodule.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A Project.
 */
@Data
@Entity
@Table(name = "project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne
    private User projectManager;

    @ManyToOne
    private User techLead;
}
