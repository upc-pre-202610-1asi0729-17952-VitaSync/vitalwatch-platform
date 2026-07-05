package com.vitalwatch.center.platform.shared.interfaces.rest.transform;

import com.vitalwatch.center.platform.shared.application.result.ApplicationError;
import com.vitalwatch.center.platform.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Converts application errors into HTTP responses.
 */
public final class ErrorResponseAssembler {

    private ErrorResponseAssembler() {
    }

    public static ResponseEntity<ErrorResource> toResponseEntity(ApplicationError error) {
        var status = httpStatusFrom(error.code());

        var resource = new ErrorResource(
                error.code(),
                error.message(),
                null
        );

        return ResponseEntity.status(status).body(resource);
    }

    public static ResponseEntity<ErrorResource> toResponseEntity(
            String code,
            String message,
            String details,
            HttpStatus status
    ) {
        var resource = new ErrorResource(code, message, details);
        return ResponseEntity.status(status).body(resource);
    }

    private static HttpStatus httpStatusFrom(String code) {
        if (code == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        if (code.contains("NOT_FOUND")) {
            return HttpStatus.NOT_FOUND;
        }

        if (code.contains("CONFLICT")) {
            return HttpStatus.CONFLICT;
        }

        if (code.contains("VALIDATION")) {
            return HttpStatus.BAD_REQUEST;
        }

        if (code.contains("BUSINESS_RULE")) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}