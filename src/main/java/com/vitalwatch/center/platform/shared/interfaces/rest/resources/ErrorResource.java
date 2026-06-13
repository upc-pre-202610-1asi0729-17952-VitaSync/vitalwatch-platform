package com.vitalwatch.center.platform.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

/**
 * Standard error response resource.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResource(
        String code,
        String message,
        @Nullable String details
) {
    public ErrorResource(String code, String message) {
        this(code, message, null);
    }
}
