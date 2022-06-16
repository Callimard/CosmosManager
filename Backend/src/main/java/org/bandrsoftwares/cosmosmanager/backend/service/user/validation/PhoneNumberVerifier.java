package org.bandrsoftwares.cosmosmanager.backend.service.user.validation;

import org.bandrsoftwares.cosmosmanager.backend.service.user.PhoneTool;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberVerifier implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String phoneNumberToVerify, ConstraintValidatorContext context) {
        if (phoneNumberToVerify != null) {
            return !phoneNumberToVerify.isBlank() && PhoneTool.isCorrectAndSupportedPhoneNumber(phoneNumberToVerify);
        } else
            return true;
    }
}
