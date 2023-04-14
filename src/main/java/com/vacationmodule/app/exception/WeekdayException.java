package com.vacationmodule.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeekdayException extends RuntimeException {

    private final String message = "The requested day is not a weekday!";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
