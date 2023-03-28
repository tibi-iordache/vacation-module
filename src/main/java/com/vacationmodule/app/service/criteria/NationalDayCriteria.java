package com.vacationmodule.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.vacationmodule.app.domain.NationalDay} entity. This class is used
 * in {@link com.vacationmodule.app.web.rest.NationalDayResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /national-days?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NationalDayCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter day;

    private IntegerFilter month;

    private Boolean distinct;

    public NationalDayCriteria() {}

    public NationalDayCriteria(NationalDayCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.day = other.day == null ? null : other.day.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NationalDayCriteria copy() {
        return new NationalDayCriteria(this);
    }
}
