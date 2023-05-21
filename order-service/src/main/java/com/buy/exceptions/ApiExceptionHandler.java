package com.buy.exceptions;

import feign.FeignException;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleAiRequestException(ApiRequestException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.SC_BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<Object> handleFeignBadRequest(FeignException.BadRequest e) {
        ApiException apiException = new ApiException(
                "Bad request in Feign client service",
                e,
                e.status(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, BAD_REQUEST);
    }
}
