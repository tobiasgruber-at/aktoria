package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Class for testing user services.
 *
 * @author Luke Nemeskeri
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
class UserServiceUnitTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("tester", "password"))
        .withPerMethodLifecycle(true);


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecureTokenService secureTokenService;

    @Autowired
    public UserServiceUnitTest(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository, SecureTokenService secureTokenService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.secureTokenService = secureTokenService;
    }

    @Test
    @DisplayName("findUserByEmail()")
    @Transactional
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    void findUserByEmail() {
        assertEquals(new SimpleUserDto(1L, UserDataGenerator.TEST_USER_FIRST_NAME + 1, UserDataGenerator.TEST_USER_LAST_NAME + 1,
            UserDataGenerator.TEST_USER_EMAIL_LOCAL + 1 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, true), userService.findByEmail(UserDataGenerator.TEST_USER_EMAIL_LOCAL + 1 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN));

        assertEquals(new SimpleUserDto(3L, UserDataGenerator.TEST_USER_FIRST_NAME + 3, UserDataGenerator.TEST_USER_LAST_NAME + 3,
            UserDataGenerator.TEST_USER_EMAIL_LOCAL + 3 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, true), userService.findByEmail(UserDataGenerator.TEST_USER_EMAIL_LOCAL + 3 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN));

        assertEquals(new SimpleUserDto(14L, UserDataGenerator.TEST_USER_FIRST_NAME + 14, UserDataGenerator.TEST_USER_LAST_NAME + 14,
            UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, true), userService.findByEmail(UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN));
    }

    @Nested
    @DisplayName("changePassword()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class ChangePasswordTest {

        @Test
        @DisplayName("changes the password correctly")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 1 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 1,
            roles = { Role.verified })
        void changePasswordWorks() {
            assertTrue(passwordEncoder.matches("hallo12345",
                userService.changePassword(new PasswordChangeDto(null, UserDataGenerator.TEST_USER_PASSWORD + 1, "hallo12345"), 1L).getPasswordHash()));
        }

        @Test
        @DisplayName("changes the password correctly and encodes correctly")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 2 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 2,
            roles = { Role.verified })
        void changePasswordChangesPassword() {
            assertFalse(passwordEncoder.matches("hallo1256",
                userService.changePassword(new PasswordChangeDto(null, UserDataGenerator.TEST_USER_PASSWORD + 2, "hallo12345"), 2L).getPasswordHash()));
        }


        @Test
        @DisplayName("throws ValidationException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 10 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 10,
            roles = { Role.verified })
        void changePasswordThrowsValidationException() {
            assertThrows(ValidationException.class, () -> userService.changePassword(new PasswordChangeDto(null,
                UserDataGenerator.TEST_USER_PASSWORD + 10, ""), 10L));
            assertThrows(ValidationException.class, () -> userService.changePassword(new PasswordChangeDto(null,
                UserDataGenerator.TEST_USER_PASSWORD + 10, "passw"), 10L));
        }

        @Test
        @DisplayName("throws ConflictException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 14,
            roles = { Role.verified })
        void changePasswordThrowsConflictException() {
            assertThrows(ConflictException.class, () -> userService.changePassword(new PasswordChangeDto(null,
                "wrongPassword", "hallo1010"), 14L));
        }

        @Test
        @DisplayName("throws InvalidTokenException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 14,
            roles = {Role.verified})
        void changePasswordWithInvalidToken() {
            assertThrows(InvalidTokenException.class, () -> userService.changePassword(new PasswordChangeDto("ExpiredPasswordToken",
                null, "hallo1010"), 14L));
        }

        @Test
        @DisplayName("throws InvalidTokenException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 14,
            roles = {Role.verified})
        void changePasswordWithExpiredToken() {
            Optional<User> userOptional = userRepository.findById(14L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD, 0);
            token.setToken("ExpiredPasswordToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertThrows(InvalidTokenException.class, () -> userService.changePassword(new PasswordChangeDto("ExpiredPasswordToken",
                null, "hallo1010"), 14L));
        }

        @Test
        @DisplayName("throws InvalidTokenException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 14,
            roles = {Role.verified})
        void changePasswordWithWrongTokenType() {
            Optional<User> userOptional = userRepository.findById(14L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.VERIFY_EMAIL, 15);
            token.setToken("ExpiredEmailToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertThrows(InvalidTokenException.class, () -> userService.changePassword(new PasswordChangeDto("ExpiredEmailToken",
                null, "hallo1010"), 14L));
        }

        @Test
        @DisplayName("throws InvalidTokenException")
        @Transactional
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 14 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 14,
            roles = {Role.verified})
        void changePasswordWithValidToken() {
            Optional<User> userOptional = userRepository.findById(14L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD, 15);
            token.setToken("ValidPasswordToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertTrue(passwordEncoder.matches("hallo12345",
                userService.changePassword(new PasswordChangeDto("ValidPasswordToken", null, "hallo12345"), 1L).getPasswordHash()));
        }

    }

    @Test
    @DisplayName("findById works")
    @Transactional
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    void findByIdReturnsCorrectUse() {
        assertEquals(new SimpleUserDto(7L, UserDataGenerator.TEST_USER_FIRST_NAME + 7, UserDataGenerator.TEST_USER_LAST_NAME + 7,
            UserDataGenerator.TEST_USER_EMAIL_LOCAL + 7 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, true), userService.findById(7L));

        assertEquals(new SimpleUserDto(13L, UserDataGenerator.TEST_USER_FIRST_NAME + 13, UserDataGenerator.TEST_USER_LAST_NAME + 13,
            UserDataGenerator.TEST_USER_EMAIL_LOCAL + 13 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, true), userService.findById(13L));
    }

    @Test
    @Transactional
    @DisplayName("patch user throws ValidationException")
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 16 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 16,
        roles = { Role.verified })
    void changeUserThrowsValidationException() {
        assertThrows(ValidationException.class, () -> userService.patch(new UpdateUserDto(null, "n".repeat(200), null, null, null, null, true), 16L));
        assertThrows(ValidationException.class, () -> userService.patch(new UpdateUserDto(null, null, "nd".repeat(187), null, null, null, true), 16L));
        assertThrows(ValidationException.class, () -> userService.patch(new UpdateUserDto(null, null, null, "halo", null, null, true), 16L));
        assertThrows(ConflictException.class, () -> userService.patch(new UpdateUserDto(null, null, null, UserDataGenerator.TEST_USER_EMAIL_LOCAL + 1 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, null, null, true), 16L));
    }

    @Test
    @DisplayName("changes the user data correctly")
    @Transactional
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 16 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 16,
        roles = { Role.verified })
    void changeUserDataIsOk() {
        assertEquals("newName", userService.patch(new UpdateUserDto(null, "newName", null, null, null, null, true), 16L).getFirstName());
        assertEquals("newLastName", userService.patch(new UpdateUserDto(null, null, "newLastName", null, null, null, true), 16L).getLastName());
        assertEquals("newmail16@gmx.com", userService.patch(new UpdateUserDto(null, null, null, "newmail16@gmx.com", null, null, true), 16L).getEmail());
    }

    @Nested
    @DisplayName("forgotPassword()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class ForgotPasswordTest {

        @Test
        @DisplayName("sends email")
        @Transactional
        void forgotPasswordSendsEmail() {
            userService.forgotPassword("test1@test.com");

            final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertEquals(1, receivedMessages.length);
        }


        @Test
        @DisplayName("NotFoundException when email does not exists")
        @Transactional
        void forgotPasswordEmailDoesNotExist() {
            assertThrows(NotFoundException.class, () -> userService.forgotPassword("notfound@test.com"));
        }
    }

    @Nested
    @DisplayName("verifyEmail()")
    @SpringBootTest
    @WithMockUser(username = "test5@test.com", password = "password5", roles = {Role.user, Role.admin})
    class VerifyEmailTest {

        @Test
        @DisplayName("no token throws InvalidTokenException")
        @Transactional
        void VerifyEmailWithNoToken() {
            assertFalse(userService.findById(5L).getVerified());
            assertThrows(InvalidTokenException.class, () -> userService.verifyEmail(null));
            assertFalse(userService.findById(5L).getVerified());
        }

        @Test
        @DisplayName("empty token throws InvalidTokenException")
        @Transactional
        void VerifyEmailWithEmptyToken() {
            assertFalse(userService.findById(5L).getVerified());
            assertThrows(InvalidTokenException.class, () -> userService.verifyEmail(""));
            assertFalse(userService.findById(5L).getVerified());
        }

        @Test
        @DisplayName("not existent token throws InvalidTokenException")
        @Transactional
        void VerifyEmailWithNotExistentToken() {
            assertFalse(userService.findById(5L).getVerified());
            assertThrows(InvalidTokenException.class, () -> userService.verifyEmail("someRandomToken"));
            assertFalse(userService.findById(5L).getVerified());
        }

        @Test
        @DisplayName("expired token throws InvalidTokenException")
        @Transactional
        void VerifyEmailWithExpiredToken() {

            Optional<User> userOptional = userRepository.findById(5L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.VERIFY_EMAIL, 0);
            token.setToken("ExpiredEmailToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertFalse(userService.findById(5L).getVerified());
            assertThrows(InvalidTokenException.class, () -> userService.verifyEmail("ExpiredEmailToken"));
            assertFalse(userService.findById(5L).getVerified());
        }

        @Test
        @DisplayName("expired token throws InvalidTokenException")
        @Transactional
        void VerifyEmailWithWrongTokenType() {

            Optional<User> userOptional = userRepository.findById(5L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD, 15);
            token.setToken("ValidPasswordToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertFalse(userService.findById(5L).getVerified());
            assertThrows(InvalidTokenException.class, () -> userService.verifyEmail("ValidPasswordToken"));
            assertFalse(userService.findById(5L).getVerified());
        }

        @Test
        @DisplayName("send verification email")
        @Transactional
        void VerifyEmailWithValidToken() {

            Optional<User> userOptional = userRepository.findById(5L);
            assertTrue(userOptional.isPresent());
            User user = userOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.VERIFY_EMAIL, 15);
            token.setToken("ValidEmailToken");
            token.setAccount(user);
            secureTokenService.saveSecureToken(token);

            assertFalse(userService.findById(5L).getVerified());
            userService.verifyEmail("ValidEmailToken");
            assertTrue(userService.findById(5L).getVerified());

            userRepository.saveAndFlush(user);
        }

    }

    @Test
    @DisplayName("changes the user data correctly")
    @Transactional
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 16 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 16,
        roles = {Role.verified})
    void sendVerificationEmailTest() {
        Optional<User> userOptional = userRepository.findById(5L);
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();

        userService.sendEmailVerificationLink(user);

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
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

    @Nested
    @DisplayName("deleteUser()")
    class DeleteUserTest {
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

        @Test
        @Transactional
        @DisplayName("deletes user correctly")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 3 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 3,
            roles = { Role.verified })
        void deleteUserWorks() {
            userService.delete(3L);
        }

        @Test
        @Transactional
        @DisplayName("deletes user correctly without verification")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 3 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 3,
            roles = { Role.user })
        void deleteUserWorks2() {
            userService.delete(3L);
        }

        @Test
        @Transactional
        @DisplayName("deletes throws ")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 4 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 4,
            roles = { Role.verified })
        void deleteUserThrowsUnauthorized() {
            assertThrows(UnauthorizedException.class, () -> userService.delete(3L));
        }

        @Transactional
        @ParameterizedTest
        @DisplayName("throws NotFoundException")
        @MethodSource("parameterizedDeleteUserExceptionProvider")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
        void deleteUserThrowsException(Long input) {
            assertThrows(NotFoundException.class, () -> userService.delete(input));
        }
    }
}

