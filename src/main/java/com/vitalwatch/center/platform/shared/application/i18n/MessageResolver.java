package com.vitalwatch.center.platform.shared.application.i18n;

/**
 * Contract for resolving internationalized messages.
 */
public interface MessageResolver {

    String get(String key);

    String get(String key, Object... arguments);
}