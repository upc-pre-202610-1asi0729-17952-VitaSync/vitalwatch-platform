package com.vitalwatch.center.platform.shared.interfaces.rest.resources;

/**
 * Standard error response returned by the REST API.
 *
 * @param code    machine-readable error code
 * @param message human-readable error message
 * @param details optional error details
 */
public record ErrorResource(
        String code,
        String message,
        String details
) {
}