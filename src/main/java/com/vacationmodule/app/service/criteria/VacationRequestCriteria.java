package com.vacationmodule.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vacationmodule.app.domain.VacationRequest} entity. This class is used
 * in {@link com.vacationmodule.app.web.rest.VacationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vacation-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VacationRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private InstantFilter date;

    private LongFilter userId;

    private LongFilter approbedById;

    private Boolean distinct;

    public VacationRequestCriteria() {}

    public VacationRequestCriteria(VacationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.approbedById = other.approbedById == null ? null : other.approbedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VacationRequestCriteria copy() {
        return new VacationRequestCriteria(this);
    }
}
