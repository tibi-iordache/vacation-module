package com.vacationmodule.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vacationmodule.app.domain.Project} entity. This class is used
 * in {@link com.vacationmodule.app.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private LongFilter projectManagerId;

    private LongFilter techLeadId;

    private Boolean distinct;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.projectManagerId = other.projectManagerId == null ? null : other.projectManagerId.copy();
        this.techLeadId = other.techLeadId == null ? null : other.techLeadId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }
}
