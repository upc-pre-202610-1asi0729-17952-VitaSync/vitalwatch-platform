package com.vitalwatch.center.platform.profiles.application.internal.eventhandlers;

import com.vitalwatch.center.platform.profiles.domain.model.events.ProfileCreatedEvent;
import com.vitalwatch.center.platform.profiles.interfaces.events.ProfileCreatedIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Translates internal profile domain events into integration events.
 */
@Service("profilesProfileCreatedEventHandler")
public class ProfileCreatedEventHandler {

    private final ApplicationEventPublisher eventPublisher;

    public ProfileCreatedEventHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void on(ProfileCreatedEvent event) {
        eventPublisher.publishEvent(new ProfileCreatedIntegrationEvent(
                event.profileId(),
                event.firstName(),
                event.lastName(),
                event.email(),
                event.street(),
                event.number(),
                event.city(),
                event.postalCode(),
                event.country()
        ));
    }
}