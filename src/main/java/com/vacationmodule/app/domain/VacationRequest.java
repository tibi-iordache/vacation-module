package com.vacationmodule.app.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A VacationRequest.
 */
@Data
@Entity
@Table(name = "vacation_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VacationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_vacation_request__approbed_by",
        joinColumns = @JoinColumn(name = "vacation_request_id"),
        inverseJoinColumns = @JoinColumn(name = "approbed_by_id")
    )
    private Set<User> approbedBies = new HashSet<>();

    public VacationRequest addApprobedBy(User user) {
        this.approbedBies.add(user);
        return this;
    }

    public VacationRequest removeApprobedBy(User user) {
        this.approbedBies.remove(user);
        return this;
    }
}
