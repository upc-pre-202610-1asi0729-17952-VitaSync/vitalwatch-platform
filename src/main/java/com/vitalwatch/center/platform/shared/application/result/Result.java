package com.vitalwatch.center.platform.shared.application.result;

import java.util.Optional;

/**
 * Generic application result wrapper for command and query services.
 *
 * @param <T> type of the successful value
 */
public final class Result<T> {

    private final T value;
    private final ApplicationError error;

    private Result(T value, ApplicationError error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(ApplicationError error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    public Optional<ApplicationError> error() {
        return Optional.ofNullable(error);
    }

    public T getValueOrThrow() {
        if (isFailure()) {
            throw new IllegalStateException(error.message());
        }

        return value;
    }
}