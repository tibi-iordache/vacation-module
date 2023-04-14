package com.vacationmodule.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class NationalDayException extends RuntimeException {

    private final String message = "The chosen date is a national holiday and it is by default free for everyone!";

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
