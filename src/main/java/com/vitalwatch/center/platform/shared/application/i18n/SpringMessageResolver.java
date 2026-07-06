package com.vitalwatch.center.platform.shared.application.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Spring implementation for resolving internationalized messages.
 */
@Service
public class SpringMessageResolver implements MessageResolver {

    private final MessageSource messageSource;

    public SpringMessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String get(String key) {
        return messageSource.getMessage(
                key,
                null,
                key,
                LocaleContextHolder.getLocale()
        );
    }

    @Override
    public String get(String key, Object... arguments) {
        return messageSource.getMessage(
                key,
                arguments,
                key,
                LocaleContextHolder.getLocale()
        );
    }
}