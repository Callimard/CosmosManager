package org.bandrsoftwares.cosmosmanager.backend.service.exception;

public class UserCreationException extends UserException {

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
