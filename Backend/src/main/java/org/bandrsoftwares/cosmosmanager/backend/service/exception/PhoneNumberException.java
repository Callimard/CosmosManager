package org.bandrsoftwares.cosmosmanager.backend.service.exception;

public class PhoneNumberException extends RuntimeException {

    public PhoneNumberException() {
        super();
    }

    public PhoneNumberException(String message) {
        super(message);
    }

    public PhoneNumberException(Throwable cause) {
        super(cause);
    }
}
