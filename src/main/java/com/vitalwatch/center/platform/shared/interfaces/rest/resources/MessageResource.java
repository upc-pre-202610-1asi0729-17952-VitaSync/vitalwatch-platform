package com.vitalwatch.center.platform.shared.interfaces.rest.resources;

/**
 * Standard message response returned by the REST API.
 *
 * @param message response message
 */
public record MessageResource(
        String message
) {
}