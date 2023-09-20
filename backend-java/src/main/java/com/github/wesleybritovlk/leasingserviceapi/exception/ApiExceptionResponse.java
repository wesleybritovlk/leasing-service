package com.github.wesleybritovlk.leasingserviceapi.exception;

import org.springframework.http.HttpStatusCode;

import java.time.ZonedDateTime;

record ApiExceptionResponse(ZonedDateTime timestamp,
                                   HttpStatusCode status,
                                   String message,
                                   String path
) {
}