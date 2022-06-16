package org.bandrsoftwares.cosmosmanager.backend.service.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bandrsoftwares.cosmosmanager.backend.UserManagementConfiguration;
import org.bandrsoftwares.cosmosmanager.backend.data.user.*;
import org.bandrsoftwares.cosmosmanager.backend.service.mail.EmailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class UserServiceImpl implements UserService {

    // Variables.

    private final UserRepository userRepository;

    private final EmailToVerifyRepository emailToVerifyRepository;

    private final ForgottenPasswordRepository forgottenPasswordRepository;

    private final EmailSender emailSender;

    private final UserManagementConfiguration userManagementConfiguration;

    // Methods.

    @Override
    public User userRegistration(@Valid UserRegistrationInformation userRegistrationInformation) {
        User user = manageUserCreation(userRegistrationInformation);
        manageEmailVerification(user);
        return user;
    }

    private User manageUserCreation(UserRegistrationInformation userRegistrationInformation) {
        User user = createUser(userRegistrationInformation);
        user = userRepository.save(user);
        return user;
    }

    private User createUser(UserRegistrationInformation userRegistrationInformation) {
        return User.builder()
                .email(userRegistrationInformation.email())
                .password(userRegistrationInformation.password())
                .firstName(userRegistrationInformation.firstName())
                .lastName(userRegistrationInformation.lastName())
                .phone(userRegistrationInformation.phone())
                .gender(userRegistrationInformation.gender())
                .birthday(userRegistrationInformation.birthday() != null ? LocalDate.parse(userRegistrationInformation.birthday()) : null)
                .photoPath(userRegistrationInformation.photoPath())
                .creationDate(Instant.now())
                .verified(false)
                .build();
    }

    private void manageEmailVerification(User user) {
        EmailToVerify emailToVerify = createEmailToVerify(user);
        emailToVerify = emailToVerifyRepository.save(emailToVerify);
        sendVerificationEmail(user, emailToVerify);
    }

    private EmailToVerify createEmailToVerify(User user) {
        return EmailToVerify.builder()
                .email(user.getEmail())
                .nonce(UUID.randomUUID().getLeastSignificantBits())
                .build();
    }

    private void sendVerificationEmail(User user, EmailToVerify emailToVerify) {
        final String userEmail = emailToVerify.getEmail();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();

        String subject = "CosmosManager, Validation d'email" + firstName + " " + lastName;
        String mailText = """
                Bonjour,
                Voici le lien pour verifier l'email de l'utilisateur %s:
                %s
                 """.formatted(firstName + " " + lastName,
                               userManagementConfiguration.getEmailValidationUrl() + "?code=" + emailToVerify.getNonce() + "&email=" +
                                       userEmail);

        emailSender.sendEmail(subject, mailText, userEmail);
    }

    @Override
    public boolean verifyEmail(@Valid EmailVerificationInformation emailVerificationInformation) {
        EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(emailVerificationInformation.email());
        User user = userRepository.findByEmail(emailVerificationInformation.email());
        if (user != null && !user.getVerified() && emailToVerify.getNonce().equals(emailVerificationInformation.nonce())) {
            user.setVerified(true);
            userRepository.save(user);
            emailToVerifyRepository.delete(emailToVerify);
            return true;
        }
        return false;
    }

    @Override
    public User updateUserInformation(@Valid UserUpdatedInformation userUpdatedInformation) {
        User user = userRepository.findByEmail(userUpdatedInformation.userEmail());
        if (user != null) {
            user.setFirstName(userUpdatedInformation.firstName());
            user.setLastName(userUpdatedInformation.lastName());
            user.setPhone(userUpdatedInformation.phone());
            user.setGender(userUpdatedInformation.gender());
            user.setBirthday(userUpdatedInformation.birthday() != null ? LocalDate.parse(userUpdatedInformation.birthday()) : null);
            user.setPhotoPath(userUpdatedInformation.photoPath());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public boolean changeUserPassword(@Valid ChangePasswordInformation changePasswordInformation) {
        User user = userRepository.findByEmail(changePasswordInformation.userEmail());
        if (user != null) {
            if (user.getPassword().equals(changePasswordInformation.oldPassword())) {
                user.setPassword(changePasswordInformation.newPassword());
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void manageForgottenPasswordForUser(@NonNull User user) {
        ForgottenPassword forgottenPassword = createForgottenPassword(user);
        forgottenPassword = forgottenPasswordRepository.save(forgottenPassword);
        sendForgottenPasswordEmail(forgottenPassword);
    }

    private ForgottenPassword createForgottenPassword(User user) {
        return ForgottenPassword.builder()
                .user(user)
                .nonce(UUID.randomUUID().getLeastSignificantBits())
                .build();
    }

    private void sendForgottenPasswordEmail(ForgottenPassword forgottenPassword) {
        final String userEmail = forgottenPassword.getUser().getEmail();
        final String firstName = forgottenPassword.getUser().getFirstName();
        final String lastName = forgottenPassword.getUser().getLastName();

        String subject = "CosmosManager, mot de passe oublié pour l'utilisateur  " + firstName + " " + lastName;
        String mailText = """
                Bonjour,
                Voici le lien pour définir un nouveau mot de passe pour l'utilisateur %s:
                %s
                 """.formatted(firstName + " " + lastName,
                               userManagementConfiguration.getFrontResetPasswordUrl() + "?code=" + forgottenPassword.getNonce() + "&mail=" +
                                       userEmail);

        emailSender.sendEmail(subject, mailText, userEmail);
    }

    @Override
    @Transactional
    public boolean changeUserForgottenPassword(@Valid ForgottenPasswordInformation forgottenPasswordInformation) {
        User user = userRepository.findByEmail(forgottenPasswordInformation.userEmail());
        if (user != null) {
            ForgottenPassword forgottenPassword = forgottenPasswordRepository.findByNonce(forgottenPasswordInformation.nonce());
            if (forgottenPassword != null && forgottenPassword.getUser().getId().equals(user.getId())) {
                user.setPassword(forgottenPasswordInformation.newPassword());
                userRepository.save(user);
                forgottenPasswordRepository.deleteAllByUser(user);
                return true;
            }
            return false;
        }
        return false;
    }
}
