package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing user services.
 *
 * @author Luke Nemeskeri
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class UserServiceUnitTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("tester", "password"))
        .withPerMethodLifecycle(true);

    @Autowired
    UserService userService;

    @Test
    @DisplayName("loadUserByUsername()")
    void loadUserByUsername() {
    }

    @Test
    @DisplayName("findUserByEmail()")
    void findUserByEmail() {
    }

    @Nested
    @DisplayName("forgotPassword()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {"USER", "VERIFIED", "ADMIN"})
    class ForgotPasswordTest {
        @Test
        @DisplayName("forgotPassword sends email")
        void forgotPasswordSendsEmail() {
            userService.forgotPassword("test1@test.com");

            final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertEquals(1, receivedMessages.length);
        }

        @Test
        @DisplayName("NotFoundException when email does not exists")
        void forgotPasswordEmailDoesNotExist() {
            assertThrows(NotFoundException.class, () -> userService.forgotPassword("notfound@test.com"));
        }
    }

    @Disabled
    @Nested
    @DisplayName("changePassword()")
    class ChangePasswordTest {

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordWorksProvider() {
            final List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for valid ids and password
            return temp.stream();
        }

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordThrowsUnauthorizedExceptionProvider() {
            final List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for invalid ids and password
            return temp.stream();
        }

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordThrowsValidationExceptionProvider() {
            final List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for invalid ids and password
            return temp.stream();
        }

        @Disabled
        @ParameterizedTest
        @DisplayName("changes the password correctly")
        @Transactional
        @MethodSource("parameterizedChangePasswordWorksProvider")
        void changePasswordWorks(ChangePasswordRecord input) {

        }

        @ParameterizedTest
        @DisplayName("throws UnauthorizedException")
        @Transactional
        @MethodSource("parameterizedChangePasswordThrowsUnauthorizedExceptionProvider")
        void changePasswordThrowsUnauthorizedException(ChangePasswordRecord input) {
            //test if entered old password matches current password
            assertThrows(UnauthorizedException.class, () -> userService.changePassword(input.passwordChangeDto, input.id));
        }

        @ParameterizedTest
        @DisplayName("throws ValidationException")
        @Transactional
        @MethodSource("parameterizedChangePasswordThrowsValidationExceptionProvider")
        void changePasswordThrowsValidationException(ChangePasswordRecord input) {
            //test if new password is a valid password
            assertThrows(ValidationException.class, () -> userService.changePassword(input.passwordChangeDto, input.id));
        }

        record ChangePasswordRecord(PasswordChangeDto passwordChangeDto, Long id) {
        }
    }

    @Disabled
    @Nested
    @DisplayName("getUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {"USER", "VERIFIED", "ADMIN"})
    class GetUserTest {
        private static Stream<String> parameterizedGetUserWorksProvider() {
            final List<String> temp = new LinkedList<>();
            //needed datagen for valid ids
            return temp.stream();
        }

        private static Stream<String> parameterizedGetUserExceptionProvider() {
            final List<String> temp = new LinkedList<>();
            //needed datagen to know which ids are invalid for this test
            return temp.stream();
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("throws ServiceException")
        @MethodSource("parameterizedGetUserExceptionProvider")
        void getUserThrowsException(String input) {
            assertThrows(NotFoundException.class, () -> userService.findByEmail(input));
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("gets the correct user")
        @MethodSource("parameterizedGetUserWorksProvider")
        void getUserWorks(String input) {
            assertNull(userService.findByEmail(input));
        }
    }

    @Nested
    @DisplayName("deleteUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {"USER", "VERIFIED", "ADMIN"})
    class DeleteUserTest {
        private static Stream<Long> parameterizedDeleteUserProvider() {
            final List<Long> temp = new LinkedList<>();
            //needed datagen
            return temp.stream();
        }

        private static Stream<Long> parameterizedDeleteUserExceptionProvider() {
            final List<Long> temp = new LinkedList<>();
            temp.add(-200L);
            temp.add(-201L);
            temp.add(-202L);
            temp.add(-203L);
            temp.add(-204L);
            temp.add(-205L);
            temp.add(-206L);
            temp.add(-207L);
            temp.add(-208L);
            temp.add(-209L);
            temp.add(-210L);
            return temp.stream();
        }

        @Disabled
        @Transactional
        @ParameterizedTest
        @DisplayName("deletes user correctly")
        @MethodSource("parameterizedDeleteUserProvider")
        void deleteUserWorks(SimpleUserDto input) {
            //delete users and check for no errors and void return. then check if user doesn't exist anymore
        }

        @Transactional
        @ParameterizedTest
        @DisplayName("throws NotFoundException")
        @MethodSource("parameterizedDeleteUserExceptionProvider")
        void deleteUserThrowsException(Long input) {
            assertThrows(NotFoundException.class, () -> userService.delete(input));
        }
    }

    @Nested
    @DisplayName("changeUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {"USER", "VERIFIED", "ADMIN"})
    class ChangeUserTest {
        private static Stream<SimpleUserDto> parameterizedChangeUserProvider() {
            final List<SimpleUserDto> temp = new LinkedList<>();
            //needed datagen
            return temp.stream();
        }

        @Disabled
        @ParameterizedTest
        @DisplayName("changes the user data correctly")
        @Transactional
        @MethodSource("parameterizedChangeUserProvider")
        void changeUserDataIsOk(SimpleUserDto input) {
        }
    }

    @Nested
    @DisplayName("createUser()")
    @SpringBootTest
    class CreateUserTest {

        private static Stream<UserRegistrationDto> parameterizedCreateUserThrowsExceptionProvider() {
            final List<UserRegistrationDto> temp = new LinkedList<>();
            temp.add(new UserRegistrationDto(null, "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("  ", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("\n\n", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("\t\t", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("\r\nFirst Name", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("First\r\nName", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("First\tName", "Lastname", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("a".repeat(101), "Lastname", "longname@mail.at", "password"));
            temp.add(new UserRegistrationDto("a".repeat(400), "Lastname", "longname@mail.at", "password"));

            temp.add(new UserRegistrationDto("Firstname", null, "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "  ", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "\n\n", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "\t\t", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "\r\nLast Name", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Last\r\nName", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Last\tName", "name@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "a".repeat(101), "longname@mail.at", "password"));
            temp.add(new UserRegistrationDto("Firstname", "a".repeat(400), "longname@mail.at", "password"));

            temp.add(new UserRegistrationDto("Firstname", "Lastname", null, "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "\n\n", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "\t\t", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name\t@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "  name@mail.com ", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "a".repeat(101) + "@mail.com", "password"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "a".repeat(400) + "@mail.com", "password"));

            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", null));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", ""));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", "   "));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", "\n\n"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", "\t\t"));
            temp.add(new UserRegistrationDto("Firstname", "Lastname", "name@mail.com", "kurz"));
            return temp.stream();
        }

        private static Stream<CreateUserRecord> parameterizedUserRegistrationDtoProvider() {
            final List<CreateUserRecord> temp = new LinkedList<>();
            temp.add(
                new CreateUserRecord(
                    new UserRegistrationDto(
                        "Firstname",
                        "Lastname",
                        "firstname.lastname@gmail.com",
                        "   password"
                    ),
                    new DetailedUserDto(
                        null,
                        "Firstname",
                        "Lastname",
                        "firstname.lastname@gmail.com",
                        "   password",
                        false
                    )
                )
            );
            temp.add(
                new CreateUserRecord(
                    new UserRegistrationDto(
                        "Firstname",
                        "Last Name",
                        "firstname.last-name@gmail.at",
                        "^?ß+.,-#%$§/(){}="
                    ),
                    new DetailedUserDto(
                        null,
                        "Firstname",
                        "Last Name",
                        "firstname.last-name@gmail.at",
                        "^?ß+.,-#%$§/(){}=",
                        false
                    )
                )
            );
            temp.add(
                new CreateUserRecord(
                    new UserRegistrationDto(
                        "Dr. Firstname",
                        "Last Name",
                        "firstname.last-name@gmx.c",
                        "password"
                    ),
                    new DetailedUserDto(
                        null,
                        "Dr. Firstname",
                        "Last Name",
                        "firstname.last-name@gmx.c",
                        "password",
                        false
                    )
                )
            );
            temp.add(
                new CreateUserRecord(
                    new UserRegistrationDto(
                        "Dr. Firstname",
                        "Last Name",
                        "firstname.last-name@gmx.c",
                        "a".repeat(200)
                    ),
                    new DetailedUserDto(
                        null,
                        "Dr. Firstname",
                        "Last Name",
                        "firstname.last-name@gmx.c",
                        "a".repeat(200),
                        false
                    )
                )
            );
            return temp.stream();
        }

        @ParameterizedTest
        @DisplayName("throws ValidationException")
        @MethodSource("parameterizedCreateUserThrowsExceptionProvider")
        @Transactional
        void createUserThrowsException(UserRegistrationDto input) {
            //test for whitespaces, null and too long inputs
            assertThrows(ValidationException.class, () -> userService.create(input));
        }

        @ParameterizedTest
        @DisplayName("creates user correctly")
        @MethodSource("parameterizedUserRegistrationDtoProvider")
        @Transactional
        void createUserIsOk(CreateUserRecord input) {
            SimpleUserDto actual = userService.create(input.input);
            input.expected.setId(actual.getId());

            assertEquals(input.expected.getId(), actual.getId());
            assertEquals(input.expected.getFirstName(), actual.getFirstName());
            assertEquals(input.expected.getLastName(), actual.getLastName());
            assertEquals(input.expected.getEmail(), actual.getEmail());
            assertEquals(input.expected.getVerified(), actual.getVerified());
        }

        record CreateUserRecord(UserRegistrationDto input, DetailedUserDto expected) {
        }
    }
}