package org.bandrsoftwares.cosmosmanager.backend.service.exception;

public class FailToFormatPhoneNumber extends PhoneNumberException {

    public FailToFormatPhoneNumber(String message) {
        super(message);
    }

    public FailToFormatPhoneNumber(Throwable t) {
        super(t);
    }
}
