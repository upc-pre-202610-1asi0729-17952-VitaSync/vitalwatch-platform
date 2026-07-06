package com.vitalwatch.center.platform.shared.interfaces.rest;

import com.vitalwatch.center.platform.shared.application.i18n.MessageResolver;
import com.vitalwatch.center.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global REST exception handler for standardized API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageResolver messageResolver;

    public GlobalExceptionHandler(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        var details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ErrorResponseAssembler.toResponseEntity(
                "VALIDATION_ERROR",
                messageResolver.get("error.validation"),
                details,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException exception) {
        var details = exception.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        return ErrorResponseAssembler.toResponseEntity(
                "VALIDATION_ERROR",
                messageResolver.get("error.validation"),
                details,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return ErrorResponseAssembler.toResponseEntity(
                "INVALID_REQUEST_BODY",
                messageResolver.get("error.invalidRequestBody"),
                exception.getMostSpecificCause().getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException exception) {
        return ErrorResponseAssembler.toResponseEntity(
                "VALIDATION_ERROR",
                exception.getMessage(),
                null,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public Object handleUnexpected(Exception exception) {
        return ErrorResponseAssembler.toResponseEntity(
                "UNEXPECTED_ERROR",
                messageResolver.get("error.unexpected"),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}