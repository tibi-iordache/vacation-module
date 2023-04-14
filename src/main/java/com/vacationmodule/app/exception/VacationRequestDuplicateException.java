package com.vacationmodule.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class VacationRequestDuplicateException extends RuntimeException {

    private final String message = "You have already made a vacation request for this date!";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
