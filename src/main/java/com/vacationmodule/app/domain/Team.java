package com.vacationmodule.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;

/**
 * A Team.
 */
@Data
@Entity
@Table(name = "team")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "projectManager", "techLead" }, allowSetters = true)
    private Project project;

    @ManyToMany
    @NotNull
    @JoinTable(name = "rel_team__user", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public Team addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Team removeUser(User user) {
        this.users.remove(user);
        return this;
    }
}
