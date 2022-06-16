package org.bandrsoftwares.cosmosmanager.backend.service.user;

import lombok.extern.slf4j.Slf4j;
import org.bandrsoftwares.cosmosmanager.backend.data.user.*;
import org.bandrsoftwares.cosmosmanager.backend.service.mail.EmailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Nested
@DisplayName("UserServiceImpl test")
@Tag("UserService")
@SpringBootTest
class UserServiceImplTest {

    private final static Random random = new SecureRandom();

    @MockBean
    private EmailSender emailSender;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailToVerifyRepository emailToVerifyRepository;

    @Autowired
    private ForgottenPasswordRepository forgottenPasswordRepository;

    @Nested
    @DisplayName("UserService userRegistration()")
    @Tag("UserRegistration")
    class UserRegistrationTest {

        @Test
        @DisplayName("userRegistration() register correctly the user if specified information are valid and a EmailToVerify has been created")
        void withValidUserInformation() {
            String testEmail = "test@cosmosmanager.fr";
            UserService.UserRegistrationInformation registration
                    = new UserService.UserRegistrationInformation(testEmail, "testPassword59$", "FirstName", "LastName", "+33 6 07 27 14 42", true,
                                                                  "1996-09-03", null);
            User user = userService.userRegistration(registration);
            assertThat(user).isNotNull();
            User found = userRepository.findByEmail(testEmail);
            assertThat(found).isNotNull();
            assertThat(user.getId()).isEqualByComparingTo(found.getId());
            assertThat(user.getId()).isEqualByComparingTo(found.getId());

            EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(testEmail);
            assertThat(emailToVerify).isNotNull();
        }

        @Test
        @DisplayName("userRegistration() throws ConstraintViolationException if specified information are not valid")
        void withInvalidUserInformation() {
            String validEmail = "test@cosmosmanager.fr";
            String validPassword = "testPassword59$";
            String validPhone = "+33 6 07 27 14 42";

            String invalidEmail = "test@cosmosmanager.";
            String invalidPassword = "Calli";
            String invalidPhone = "06 07 27 14 42";
            UserService.UserRegistrationInformation invalid0 =
                    new UserService.UserRegistrationInformation(invalidEmail, validPassword, "FirstName", "LastName", validPhone, true,
                                                                "1996-09-03", null);
            UserService.UserRegistrationInformation invalid1 =
                    new UserService.UserRegistrationInformation(validEmail, invalidPassword, "FirstName", "LastName", validPhone, true,
                                                                "1996-09-03", null);
            UserService.UserRegistrationInformation invalid2 =
                    new UserService.UserRegistrationInformation(validEmail, validPassword, "FirstName", "LastName", invalidPhone, true,
                                                                "1996-09-03", null);

            UserService.UserRegistrationInformation invalid3 =
                    new UserService.UserRegistrationInformation(invalidEmail, validPassword, null, "LastName", validPhone, true,
                                                                "1996-09-03", null);

            UserService.UserRegistrationInformation invalid4 =
                    new UserService.UserRegistrationInformation(invalidEmail, validPassword, "FirstName", null, validPhone, true,
                                                                "1996-09-03", null);

            assertThrows(ConstraintViolationException.class, () -> userService.userRegistration(invalid0));
            assertThrows(ConstraintViolationException.class, () -> userService.userRegistration(invalid1));
            assertThrows(ConstraintViolationException.class, () -> userService.userRegistration(invalid2));
            assertThrows(ConstraintViolationException.class, () -> userService.userRegistration(invalid3));
            assertThrows(ConstraintViolationException.class, () -> userService.userRegistration(invalid4));
        }

        @Test
        @DisplayName("userRegistration() throws DataIntegrityViolationException if the specified email has already been register")
        void withAlreadyUsedEmail() {
            User rUser = saveRandomUser();
            UserService.UserRegistrationInformation registration =
                    new UserService.UserRegistrationInformation(rUser.getEmail(), "Password96$", "Name",
                                                                "NameName", null, true, null, null);
            assertThrows(DataIntegrityViolationException.class, () -> userService.userRegistration(registration));
        }

        @Test
        @DisplayName("userRegistration() throws DataIntegrityViolationException if the specified phone has already been register")
        void withAlreadyUsedPhone() {
            String phone = "+33 6 07 27 14 45";
            User user = createUser("random@cosmosmanager.fr", phone);
            userRepository.save(user);

            UserService.UserRegistrationInformation registration = new UserService.UserRegistrationInformation("other@cosmosmanager.fr",
                                                                                                               "Password654$", "FirstName",
                                                                                                               "LastName", phone, true, null, null);
            assertThrows(DataIntegrityViolationException.class, () -> userService.userRegistration(registration));
        }
    }

    @Nested
    @DisplayName("UserService verifyEmail()")
    @Tag("VerifyEmail")
    class VerifyEmailTest {

        @Test
        @DisplayName("verifyEmail() returns true and set the user verified if the email has not already been verified and the nonce is correct")
        void verifyEmailCorrectlyVerifyEmail() {
            User user = registerRandomUser();
            String email = user.getEmail();

            EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(email);
            assertThat(emailToVerify).isNotNull();

            UserService.EmailVerificationInformation verificationInformation = new UserService.EmailVerificationInformation(email,
                                                                                                                            emailToVerify.getNonce());

            boolean verified = userService.verifyEmail(verificationInformation);
            assertThat(verified).isTrue();

            User userVerified = userRepository.findByEmail(email);
            assertThat(userVerified).isNotNull();
            assertThat(userVerified.getVerified()).isTrue();

            EmailToVerify found = emailToVerifyRepository.findByEmail(email);
            assertThat(found).isNull();
        }

        @Test
        @DisplayName("verifyEmail() returns false if the email has already been verified")
        void alreadyVerifiedUser() {
            User user = registerRandomUser();
            user.setVerified(true);
            user = userRepository.save(user);
            String email = user.getEmail();

            EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(email);
            assertThat(emailToVerify).isNotNull();

            UserService.EmailVerificationInformation verificationInformation = new UserService.EmailVerificationInformation(email,
                                                                                                                            emailToVerify.getNonce());
            boolean verified = userService.verifyEmail(verificationInformation);
            assertThat(verified).isFalse();

            EmailToVerify found = emailToVerifyRepository.findByEmail(email);
            assertThat(found).isNotNull(); // Verification not done -> EmailToVerify not deleted
        }

        @Test
        @DisplayName("verifyEmail() returns false if the email is not associated to a User")
        void withEmailNotAssociatedToAUser() {
            String email = generateRandomEmail();
            UserService.EmailVerificationInformation verificationInformation = new UserService.EmailVerificationInformation(email,
                                                                                                                            random.nextLong());
            boolean verified = userService.verifyEmail(verificationInformation);
            assertThat(verified).isFalse();
        }

        @Test
        @DisplayName("verifyEmail() returns false and does not set the user verified if the nonce is not correct")
        void withWrongNonce() {
            User user = registerRandomUser();
            String email = user.getEmail();

            EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(email);
            assertThat(emailToVerify).isNotNull();

            UserService.EmailVerificationInformation verificationInformation =
                    new UserService.EmailVerificationInformation(email, emailToVerify.getNonce() + random.nextLong());

            boolean verified = userService.verifyEmail(verificationInformation);
            assertThat(verified).isFalse();

            User userVerified = userRepository.findByEmail(email);
            assertThat(userVerified).isNotNull();
            assertThat(userVerified.getVerified()).isFalse();

            EmailToVerify found = emailToVerifyRepository.findByEmail(email);
            assertThat(found).isNotNull(); // Not deleted because verification failed
        }
    }

    @Nested
    @DisplayName("UserService updateUserInformation")
    @Tag("UpdateUserInformation")
    class UpdateUserInformationTest {

        @Test
        @DisplayName("updateUserInformation() change user information")
        void changeUserInformation() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String newFirstName = "Guillaume";
            String newLastName = "Rakotomalala";

            UserService.UserUpdatedInformation updatedInformation = new UserService.UserUpdatedInformation(email, newFirstName, newLastName, null,
                                                                                                           true, null, null);
            User updated = userService.updateUserInformation(updatedInformation);
            assertThat(updated).isNotNull();
            assertThat(updated.getFirstName()).isEqualTo(newFirstName);
            assertThat(updated.getLastName()).isEqualTo(newLastName);

            User found = userRepository.findByEmail(email);
            assertThat(found).isNotNull();
            assertThat(found.getId()).isEqualByComparingTo(updated.getId());
            assertThat(found.getFirstName()).isEqualTo(newFirstName);
            assertThat(found.getLastName()).isEqualTo(newLastName);
        }

        @Test
        @DisplayName("updateUserInformation() throws ConstraintViolationException if specified updated information are not valid")
        void withInvalidUpdatedInformation() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String newFirstName = "Guillaume";
            String newLastName = "Rakotomalala";
            String invalidPhone = "06 07 27 14 40";

            UserService.UserUpdatedInformation updatedInformation =
                    new UserService.UserUpdatedInformation(email, newFirstName, newLastName, invalidPhone,
                                                           true, null, null);
            assertThrows(ConstraintViolationException.class, () -> userService.updateUserInformation(updatedInformation));
        }

        @Test
        @DisplayName("updateUserInformation() returns null if email is not associated to a user")
        void withUnknownEmail() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String newFirstName = "Guillaume";
            String newLastName = "Rakotomalala";

            UserService.UserUpdatedInformation updatedInformation =
                    new UserService.UserUpdatedInformation("unknown@comosmanager.fr", newFirstName, newLastName, null,
                                                           true, null, null);

            User updated = userService.updateUserInformation(updatedInformation);
            assertThat(updated).isNull();
        }
    }

    @Nested
    @DisplayName("UserService changeUserPassword()")
    @Tag("ChangeUserPassword")
    class ChangeUserPasswordTest {

        @Test
        @DisplayName("changeUserPassword() returns true if the user password has been changed")
        void userPasswordChangedCorrectly() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String password = user.getPassword();
            String newPassword = "myNewPassword89$";

            UserService.ChangePasswordInformation passwordInformation = new UserService.ChangePasswordInformation(email, password, newPassword);
            boolean passwordChanged = userService.changeUserPassword(passwordInformation);
            assertThat(passwordChanged).isTrue();

            User found = userRepository.findByEmail(email);
            assertThat(found.getPassword()).isEqualTo(newPassword);
        }

        @Test
        @DisplayName("changeUserPassword() throws ConstraintViolationException if password information are not valid")
        void withNotValidPasswordInformation() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String password = user.getPassword();
            String invalidNewPassword = "myNewPassword89";

            UserService.ChangePasswordInformation passwordInformation =
                    new UserService.ChangePasswordInformation(email, password, invalidNewPassword);
            assertThrows(ConstraintViolationException.class, () -> userService.changeUserPassword(passwordInformation));
        }

        @Test
        @DisplayName("changeUserPassword() returns false if the old password is not correct")
        void withWrongOldPassword() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String newPassword = "myNewPassword89$";

            UserService.ChangePasswordInformation passwordInformation = new UserService.ChangePasswordInformation(email, "wrongOldPassword",
                                                                                                                  newPassword);
            boolean passwordChanged = userService.changeUserPassword(passwordInformation);
            assertThat(passwordChanged).isFalse();

            User found = userRepository.findByEmail(email);
            assertThat(found.getPassword()).isEqualTo(user.getPassword());
        }

        @Test
        @DisplayName("changeUserPassword() returns false if the user is not found")
        void withNotFoundUser() {
            User user = registerRandomUser();
            String password = user.getPassword();
            String newPassword = "myNewPassword89$";

            UserService.ChangePasswordInformation passwordInformation = new UserService.ChangePasswordInformation("unknown@cosmosmanager.fr",
                                                                                                                  password,
                                                                                                                  newPassword);
            boolean passwordChanged = userService.changeUserPassword(passwordInformation);
            assertThat(passwordChanged).isFalse();
        }
    }

    @Nested
    @DisplayName("UserService manageForgottenPasswordForUser()")
    @Tag("ManageForgottenPasswordForUser")
    class ManageForgottenPasswordForUserTest {

        @Test
        @DisplayName("manageForgottenPasswordForUser() create a ForgottenPassword for an existing User")
        void withExistingUser() {
            User user = registerRandomUser();

            userService.manageForgottenPasswordForUser(user);
            List<ForgottenPassword> listForgottenPassword = forgottenPasswordRepository.findByUser(user);
            assertThat(listForgottenPassword).isNotNull().isNotEmpty().hasSize(1);
        }

        @Test
        @DisplayName("manageForgottenPasswordForUser() throws DataIntegrityViolationException if the user does not exists")
        void withNonExistingUser() {
            User user = registerRandomUser();
            EmailToVerify emailToVerify = emailToVerifyRepository.findByEmail(user.getEmail());
            emailToVerifyRepository.delete(emailToVerify);
            userRepository.delete(user);

            assertThrows(DataIntegrityViolationException.class, () -> userService.manageForgottenPasswordForUser(user));
        }
    }

    @Nested
    @DisplayName("UserService changeUserForgottenPassword()")
    @Tag("ChangeUserForgottenPassword")
    class ChangeUserForgottenPasswordTest {

        @Test
        @DisplayName("changeUserForgottenPassword() returns true and change the password of the User if specified forgotten password information " +
                "are correct")
        void withCorrectForgottenPasswordInformation() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String newPassword = "SuperPassword96555$";

            userService.manageForgottenPasswordForUser(user);

            List<ForgottenPassword> listForgottenPassword = forgottenPasswordRepository.findByUser(user);
            ForgottenPassword forgottenPassword = listForgottenPassword.get(0);

            UserService.ForgottenPasswordInformation forgottenPasswordInformation = new UserService.ForgottenPasswordInformation(email,
                                                                                                                                 forgottenPassword.getNonce(),
                                                                                                                                 newPassword);
            boolean passwordChanged = userService.changeUserForgottenPassword(forgottenPasswordInformation);
            assertThat(passwordChanged).isTrue();

            User found = userRepository.findByEmail(email);
            assertThat(found.getPassword()).isEqualTo(newPassword);

            listForgottenPassword = forgottenPasswordRepository.findByUser(user);
            assertThat(listForgottenPassword).isEmpty();
        }

        @Test
        @DisplayName("changeUserForgottenPassword() throws ConstraintViolationException if ForgottenPasswordInformation are not valid")
        void withNotValidForgottenPasswordInformation() {
            User user = registerRandomUser();
            String email = user.getEmail();
            String invalidPassword = "SuperPassword$";

            userService.manageForgottenPasswordForUser(user);

            List<ForgottenPassword> listForgottenPassword = forgottenPasswordRepository.findByUser(user);
            ForgottenPassword forgottenPassword = listForgottenPassword.get(0);

            UserService.ForgottenPasswordInformation forgottenPasswordInformation = new UserService.ForgottenPasswordInformation(email,
                                                                                                                                 forgottenPassword.getNonce(),
                                                                                                                                 invalidPassword);
            assertThrows(ConstraintViolationException.class, () -> userService.changeUserForgottenPassword(forgottenPasswordInformation));
        }

        @Test
        @DisplayName("changeUserForgottenPassword() returns false and does not change the password if the nonce is not correct")
        void withWrongNonce() {
            User u1 = registerRandomUser();
            User u2 = registerRandomUser();
            String oldPassword = u2.getPassword();
            String newPassword = "SuperPassword223255$";

            userService.manageForgottenPasswordForUser(u1);

            List<ForgottenPassword> listFPu1 = forgottenPasswordRepository.findByUser(u1);
            ForgottenPassword fPu1 = listFPu1.get(0);

            UserService.ForgottenPasswordInformation forgottenPasswordInformation = new UserService.ForgottenPasswordInformation(u2.getEmail(),
                                                                                                                                 fPu1.getNonce(),
                                                                                                                                 newPassword);
            boolean passwordChanged = userService.changeUserForgottenPassword(forgottenPasswordInformation);
            assertThat(passwordChanged).isFalse();
            User found = userRepository.findByEmail(u2.getEmail());
            assertThat(found.getPassword()).isEqualTo(oldPassword);
        }

        @Test
        @DisplayName("changeUserForgottenPassword() returns false if the user is not found")
        void withUnknownUser() {
            User user = registerRandomUser();
            String oldPassword = user.getPassword();
            String newPassword = "SuperPassword64516$";

            userService.manageForgottenPasswordForUser(user);

            List<ForgottenPassword> listFPUser = forgottenPasswordRepository.findByUser(user);
            ForgottenPassword fPu = listFPUser.get(0);

            UserService.ForgottenPasswordInformation forgottenPasswordInformation = new UserService.ForgottenPasswordInformation("unknown" +
                                                                                                                                         "@cosmosmanager.fr",
                                                                                                                                 fPu.getNonce(),
                                                                                                                                 newPassword);

            boolean passwordChanged = userService.changeUserForgottenPassword(forgottenPasswordInformation);
            assertThat(passwordChanged).isFalse();

            User found = userRepository.findByEmail(user.getEmail());
            assertThat(found.getPassword()).isEqualTo(oldPassword);
        }
    }

    private User registerRandomUser() {
        return registerUser(generateRandomEmail());
    }

    private String generateRandomEmail() {
        return random.nextInt(0, Integer.MAX_VALUE - 1) + "@cosmosmanager.fr";
    }

    private User registerUser(String mail) {
        UserService.UserRegistrationInformation registration
                = new UserService.UserRegistrationInformation(mail,
                                                              "testPassword59$",
                                                              "FirstName",
                                                              "LastName",
                                                              null,
                                                              true,
                                                              null, null);
        return userService.userRegistration(registration);
    }

    private User saveRandomUser() {
        User user = createUserWithRandomEmail();
        return userRepository.save(user);
    }

    private User createUserWithRandomEmail() {
        return createUser(generateRandomEmail(), null);
    }

    private User createUser(String email, String phone) {
        return User.builder()
                .email(email)
                .password("Password95$")
                .firstName("FirstName")
                .lastName("LastName")
                .phone(phone)
                .gender(true)
                .creationDate(Instant.now())
                .verified(false)
                .build();
    }
}
