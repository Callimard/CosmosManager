package org.bandrsoftwares.cosmosmanager.backend.service.user;

import org.bandrsoftwares.cosmosmanager.backend.data.user.EmailToVerify;
import org.bandrsoftwares.cosmosmanager.backend.data.user.ForgottenPassword;
import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.bandrsoftwares.cosmosmanager.backend.service.exception.UserCreationException;
import org.bandrsoftwares.cosmosmanager.backend.service.user.validation.ValidEmail;
import org.bandrsoftwares.cosmosmanager.backend.service.user.validation.ValidPassword;
import org.bandrsoftwares.cosmosmanager.backend.service.user.validation.ValidPhone;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface UserService {

    /**
     * Create a {@link User} from specified information. This {@code User} is saved in the database. In addition to this is in charge to send a
     * verification email to verified if the specified email is correct.
     *
     * @param userRegistrationInformation user registration information
     *
     * @return the user after it has been created and saved in the database.
     *
     * @throws UserCreationException if the creation or the save of the {@code User} failed.
     */
    User userRegistration(@Valid UserRegistrationInformation userRegistrationInformation);

    record UserRegistrationInformation(@NotNull @ValidEmail String email, @NotNull @ValidPassword String password,
                                       @NotNull @Size(min = 1, max = 255) String firstName,
                                       @NotNull @Size(min = 1, max = 255) String lastName,
                                       @ValidPhone String phone, boolean gender, String birthday, String photoPath) {
    }

    /**
     * Try to verify the email from the specified information. Search in the table {@link EmailToVerify} if there is the specified email and check if
     * the specified nonce is the same as the one in the database.
     * <p>
     * Returns false if the user has already been verified.
     *
     * @param emailVerificationInformation information to verify the email
     *
     * @return true if the email has been verified, else false.
     */
    boolean verifyEmail(@Valid EmailVerificationInformation emailVerificationInformation);

    record EmailVerificationInformation(@NotNull String email, @NotNull Long nonce) {
    }

    /**
     * Update specified {@link User} information with the specified {@link UserUpdatedInformation}. If an updated information are null, set this
     * information to null if the {@code User} (if it is a nullable information).
     *
     * @param userUpdatedInformation new user information
     *
     * @return the updated {@code User}.
     */
    User updateUserInformation(@Valid UserUpdatedInformation userUpdatedInformation);

    record UserUpdatedInformation(@NotNull String userEmail, @NotNull @Size(min = 1, max = 255) String firstName,
                                  @NotNull @Size(min = 1, max = 255) String lastName,
                                  @ValidPhone String phone, boolean gender, String birthday, String photoPath) {
    }

    /**
     * Change the password of the specified {@link User}. The oldPassword parameter is the old password that the user give to the system to prove that
     * he really is the user. This method must verify if the user current password is the same as the specified oldPassword.
     *
     * @param changePasswordInformation necessary information to change password
     *
     * @return true if the {@code User} password has been change and saved, else false.
     */
    boolean changeUserPassword(@Valid ChangePasswordInformation changePasswordInformation);

    record ChangePasswordInformation(@NotNull String userEmail, @NotNull String oldPassword, @NotNull @ValidPassword String newPassword) {
    }

    /**
     * Manage to create a {@link ForgottenPassword} for the specified {@link User} and to send an email to the {@code User} to allow him to change his
     * forgotten password.
     *
     * @param user the user who has forgotten its password
     */
    void manageForgottenPasswordForUser(@NotNull User user);

    /**
     * Check if the nonce in {@link ForgottenPasswordInformation} is corresponding to a nonce of a {@link ForgottenPassword} associated to the
     * specified {@link User}.
     *
     * @param forgottenPasswordInformation necessary information to change the forgotten password
     *
     * @return true if the {@code User} password has been changed, else false.
     */
    boolean changeUserForgottenPassword(@Valid ForgottenPasswordInformation forgottenPasswordInformation);

    record ForgottenPasswordInformation(@NotNull String userEmail, @NotNull Long nonce, @NotNull @ValidPassword String newPassword) {
    }
}
