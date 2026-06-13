package com.vitalwatch.center.platform.profiles.interfaces.rest.transform;

import com.vitalwatch.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.vitalwatch.center.platform.profiles.interfaces.rest.resources.CreateProfileResource;

/**
 * Assembler to convert CreateProfileResource into CreateProfileCommand.
 */
public final class CreateProfileCommandFromResourceAssembler {

    private CreateProfileCommandFromResourceAssembler() {
    }

    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.street(),
                resource.number(),
                resource.city(),
                resource.postalCode(),
                resource.country()
        );
    }
}