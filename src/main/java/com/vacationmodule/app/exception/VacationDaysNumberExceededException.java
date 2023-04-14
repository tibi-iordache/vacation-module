package com.vacationmodule.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class VacationDaysNumberExceededException extends RuntimeException {

    private final String message = "You have already used all your vacations for this year!";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
