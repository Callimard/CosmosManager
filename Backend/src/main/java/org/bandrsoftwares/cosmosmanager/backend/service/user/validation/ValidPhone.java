package org.bandrsoftwares.cosmosmanager.backend.service.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberVerifier.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Phone number has a wrong format or is unsupported";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
