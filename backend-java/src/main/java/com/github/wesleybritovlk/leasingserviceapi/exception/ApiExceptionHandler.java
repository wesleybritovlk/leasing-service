package com.github.wesleybritovlk.leasingserviceapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.util.stream.Collectors.joining;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@RequiredArgsConstructor
class ApiExceptionHandler {
    private final ApiExceptionService service;
    private static final Logger LOGGER = getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(PropertyReferenceException.class)
    ResponseEntity<ApiExceptionResponse> handleInvalidPageableRequestException(PropertyReferenceException ex, HttpServletRequest request) {
        var message = "Invalid paging parameter: " + ex.getMessage();
        var response = new ApiExceptionResponse(now(of("America/Sao_Paulo")), BAD_REQUEST, message, request.getRequestURI());
        service.create(response);
        return status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var allErrors = ex.getBindingResult().getAllErrors();
        var message = allErrors.stream().map(ObjectError::getDefaultMessage).collect(joining(", "));
        var response = new ApiExceptionResponse(now(of("America/Sao_Paulo")), UNPROCESSABLE_ENTITY, message, request.getRequestURI());
        service.create(response);
        return status(UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiExceptionResponse> handleApiRequestExceptions(ResponseStatusException ex, HttpServletRequest request) {
        var response = new ApiExceptionResponse(now(of("America/Sao_Paulo")), ex.getStatusCode(), ex.getReason(), request.getRequestURI());
        service.create(response);
        return status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<String> handleUnexpectedException(Throwable unexpectedException) {
        var message = "Unexpected server error.";
        LOGGER.error(message, unexpectedException);
        return new ResponseEntity<>(message, INTERNAL_SERVER_ERROR);
    }
}