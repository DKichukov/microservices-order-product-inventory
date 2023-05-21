package com.buy.exceptions;

import java.time.ZonedDateTime;


public record ApiException(String message, Throwable throwable, int httpStatus, ZonedDateTime timestamp) {
}
