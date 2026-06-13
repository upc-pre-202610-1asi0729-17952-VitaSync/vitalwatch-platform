package com.vitalwatch.center.platform.profiles.domain.model.aggregates;

import com.vitalwatch.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.vitalwatch.center.platform.profiles.domain.model.events.ProfileCreatedEvent;
import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.PersonName;
import com.vitalwatch.center.platform.profiles.domain.model.valueobjects.StreetAddress;
import com.vitalwatch.center.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Profile aggregate root.
 */
public class Profile extends AbstractDomainAggregateRoot<Profile> {

    @Getter
    @Setter
    private Long id;

    @Getter
    private PersonName name;

    private EmailAddress emailAddress;

    private StreetAddress streetAddress;

    public Profile(Long id, PersonName name, EmailAddress emailAddress, StreetAddress streetAddress) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        this.streetAddress = Objects.requireNonNull(streetAddress, "streetAddress must not be null");
    }

    public Profile(PersonName name, EmailAddress emailAddress, StreetAddress streetAddress) {
        this(null, name, emailAddress, streetAddress);
    }

    public Profile(
            String firstName,
            String lastName,
            String email,
            String street,
            String number,
            String city,
            String postalCode,
            String country
    ) {
        this(
                new PersonName(firstName, lastName),
                new EmailAddress(email),
                new StreetAddress(street, number, city, postalCode, country)
        );
    }

    public Profile(CreateProfileCommand command) {
        this(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.street(),
                command.number(),
                command.city(),
                command.postalCode(),
                command.country()
        );
    }

    public void setName(PersonName name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public EmailAddress getEmailAddressValue() {
        return emailAddress;
    }

    public StreetAddress getStreetAddressValue() {
        return streetAddress;
    }

    public String getFullName() {
        return name.getFullName();
    }

    public String getEmailAddress() {
        return emailAddress.address();
    }

    public String getStreetAddress() {
        return streetAddress.getStreetAddress();
    }

    public void onCreated() {
        registerDomainEvent(ProfileCreatedEvent.from(this));
    }
}