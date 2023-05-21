package com.edu.exceptions;

import java.time.ZonedDateTime;


public record ApiException(String message, Throwable throwable, int httpStatus, ZonedDateTime timestamp) {
}
