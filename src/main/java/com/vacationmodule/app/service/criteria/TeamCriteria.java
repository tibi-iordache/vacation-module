package com.vacationmodule.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vacationmodule.app.domain.Team} entity. This class is used
 * in {@link com.vacationmodule.app.web.rest.TeamResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /teams?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter projectId;

    private LongFilter userId;

    private Boolean distinct;

    public TeamCriteria() {}

    public TeamCriteria(TeamCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TeamCriteria copy() {
        return new TeamCriteria(this);
    }
}
