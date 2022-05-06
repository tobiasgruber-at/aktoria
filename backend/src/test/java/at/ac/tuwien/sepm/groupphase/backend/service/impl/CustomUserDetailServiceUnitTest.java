package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing user services
 *
 * @author Luke Nemeskeri
 */
@Disabled
@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class CustomUserDetailServiceUnitTest {

    @Autowired
    UserService userService;

    @Test
    void forgotPassword() {
    }


    @Test
    void loadUserByUsername() {
    }

    @Test
    void findApplicationUserByEmail() {
    }


    @Nested
    @DisplayName("change password tests")
    class ChangePasswordTesting {

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordWorksProvider() {
            List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for valid ids and password
            return temp.stream();
        }

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordThrowsUnauthorizedExceptionProvider() {
            List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for invalid ids and password
            return temp.stream();
        }

        private static Stream<ChangePasswordRecord> parameterizedChangePasswordThrowsValidationExceptionProvider() {
            List<ChangePasswordRecord> temp = new LinkedList<>();
            //needed datagen for invalid ids and password
            return temp.stream();
        }

        @Disabled
        @ParameterizedTest
        @DisplayName("changePassword works")
        @Transactional
        @MethodSource("parameterizedChangePasswordWorksProvider")
        void changePasswordWorks(ChangePasswordRecord input) {

        }

        @Disabled
        @ParameterizedTest
        @DisplayName("changePassword throws UnauthorizedException")
        @Transactional
        @MethodSource("parameterizedChangePasswordThrowsUnauthorizedExceptionProvider")
        void changePasswordThrowsUnauthorizedException(ChangePasswordRecord input) {
            //test if entered old password matches current password
            assertThrows(UnauthorizedException.class, () -> {
                userService.changePassword(input.passwordChangeDto, input.id);
            });
        }

        @Disabled
        @ParameterizedTest
        @DisplayName("changePassword throws ValidationException")
        @Transactional
        @MethodSource("parameterizedChangePasswordThrowsValidationExceptionProvider")
        void changePasswordThrowsValidationException(ChangePasswordRecord input) {
            //test if new password is a valid password
            assertThrows(ValidationException.class, () -> {
                userService.changePassword(input.passwordChangeDto, input.id);
            });
        }

        record ChangePasswordRecord(PasswordChangeDto passwordChangeDto, Long id) {
        }

    }

    @Nested
    @DisplayName("get user tests")
    class GetUserTesting {
        private static Stream<Long> parameterizedGetUserWorksProvider() {
            List<Long> temp = new LinkedList<>();
            //needed datagen for valid ids
            return temp.stream();
        }

        private static Stream<Long> parameterizedGetUserExceptionProvider() {
            List<Long> temp = new LinkedList<>();
            //needed datagen to know which ids are invalid for this test
            return temp.stream();
        }

        @Disabled
        @ParameterizedTest
        @Transactional
        @MethodSource("parameterizedGetUserExceptionProvider")
        @DisplayName("get user by his id throws exception")
        void getUserThrowsException(Long input) throws ServiceException {
            assertThrows(NotFoundException.class, () -> {
                userService.getUser(input);
            });
        }

        @Disabled
        @ParameterizedTest
        @Transactional
        @MethodSource("parameterizedGetUserWorksProvider")
        @DisplayName("get user by his id works accordingly")
        void getUserWorks(Long input) {
            //assertEquals(null,userService.getUser(input));
        }

    }

    @Nested
    @DisplayName("delete user tests")
    class DeleteUserTesting {
        private static Stream<Long> parameterizedDeleteUserProvider() {
            List<Long> temp = new LinkedList<>();
            //needed datagen
            return temp.stream();
        }

        private static Stream<Long> parameterizedDeleteUserExceptionProvider() {
            List<Long> temp = new LinkedList<>();
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
        @DisplayName("delete user really deletes user")
        @MethodSource("parameterizedDeleteUserProvider")
        void deleteUserWorks(SimpleUserDto input) {
            //delete users and check for no errors and void return. then check if user doesnt exist anymore
        }

        @Transactional
        @ParameterizedTest
        @DisplayName("delete user throws not found exception")
        @MethodSource("parameterizedDeleteUserExceptionProvider")
        void deleteUserThrowsException(Long input) {
            assertThrows(NotFoundException.class, () -> {
                userService.deleteUser(input);
            });
        }

    }

    @Nested
    @DisplayName("assert that changeUser changes the user data accordingly")
    class ChangeUserWorks {
        private static Stream<SimpleUserDto> parameterizedChangeUserProvider() {
            List<SimpleUserDto> temp = new LinkedList<>();
            //needed datagen
            return temp.stream();
        }

        @Disabled
        @ParameterizedTest
        @DisplayName("assert that changing user data returns the changed user data")
        @Transactional
        @MethodSource("parameterizedChangeUserProvider")
        void changeUserDataIsOk(SimpleUserDto input) {
        }
    }

    @Nested
    @DisplayName("Creating user with invalid inputs")
    class CreateUserThrowsExceptions {
        private static Stream<UserRegistrationDto> parameterizedCreateUserThrowsExceptionProvider() {
            List<UserRegistrationDto> temp = new LinkedList<>();
            temp.add(new UserRegistrationDto("", "anna.stunt@mail.com", "jdasdjiajsid"));
            temp.add(new UserRegistrationDto("  ", "anna.stunt@mail.com", "jalloo12334"));
            temp.add(new UserRegistrationDto("Fjsafkaskfjaskdjkasjkdasjkdjaskdjaskldjaskdjkasdjkasjdkasjdasjdasjdjasdjaskdjkasdjkasjdkasdjkasjdk" +
                "asjdkasdjkasjdkasjdkasjfiwjifjqwfgnwqudwinwqdwqdwqndqngqbgvuiqbuewhuhebfqnunfzvqndbqdjjeiqjwejqiwjeiqjeiwqe" +
                "821e291u32713z1he2j12e12jw281nd1bh1vdu192hd1b2d1du12hdu12db127du12d912bd91sf891u88c31n8udc1jd8j1c8dj1jcd18" +
                "j2c1dj1c818cdjch1c89w1cd9w1cw1hcd1whc1wndcn1wc17whdc1w7hdc17whc81hc1dhc1c81hwnc1hwc1hwd8c131", "longname@mail.at", "hellooooo"));
            temp.add(new UserRegistrationDto(null, "annasum.stunt@mail.com", "jalliiioo12334"));
            temp.add(new UserRegistrationDto("hallo", "", "jdasdjiajsidjo"));
            temp.add(new UserRegistrationDto("halloeeqe", "  ", "jdasdjiajsidjoopopopop"));
            temp.add(new UserRegistrationDto("hallo19239", "Fjsafkaskfjaskdjkasjkdasjkdjaskdjaskldjaskdjkasdjkasjdkasjdasjdasjdjasdjaskdjka" +
                "sdjkasjdkasdjkasjdkasjdkasdjkasjdkasjdkasjfiwjifjqwfgnwqudwinwqdwqdwqndqngqbgvuiqbuewhuhebfqnunfzvqnd" +
                "bqdjjeiqjwejqiwjeiqjeiwqe821e291u32713z1he2j12e12jw281nd1bh1vdu192hd1b2d1du12hdu12db127du12d912bd91sf891u" +
                "88c31n8udc1jd8j1c8dj1jcd1oooc1dj1c818cdjch1c89w1cd9w1cw1hcd1whc1wndcn1wc17whdc1w7hdc17whc81hc1dhc1c1hwnc1hwc1hwd@gmx.at", "jdasdjiajsidjo231"));
            temp.add(new UserRegistrationDto("Hellomain", null, "jalapenjo123"));
            temp.add(new UserRegistrationDto("test3", "jan.stunt@mail.com", ""));
            temp.add(new UserRegistrationDto("test1", "janis.stunt@mail.com", "   "));
            temp.add(new UserRegistrationDto("test1", "janis.stunt@mail.com", "Fjsafkaskfjaskdjkhuuuaasjkdjaskdjaskldjaskdjkasdjkasjdkasjdasjdasjdjasd" +
                "jaskdjkasdjkasjdkasdjkasjdkasjdkasdjkasjdkasjdkasjfiwjifjqwfgnwqudwinwqdwqdwqndqngqbgvuiqbuewhuhebfqnunfzvqndbqdjjeiqjwejqiwjeiqjeiwqe821e291u32713" +
                "z1he2j12e12jw281nd1bh1vdu192hd1b2d1du12hdu12db127du12d912bd91sf891u88c31n8udc1jd8j1c8dj1jcd1oooc1dj1c818cdjch" +
                "1c89w1cd9w1cw1hcd1whc1wndcn1wc17whdc1w7hdc17whc81hc1dhc1c81hwnc1hwc1hwd8c131"));
            temp.add(new UserRegistrationDto("petro12", "petrot@mail.com", null));
            return temp.stream();
        }

        @ParameterizedTest
        @DisplayName("assert that ValidationException is thrown")
        @MethodSource("parameterizedCreateUserThrowsExceptionProvider")
        @Transactional
        void createUserThrowsException(UserRegistrationDto input) {
            //test for whitespaces, null and too long inputs
            assertThrows(ValidationException.class, () -> {
                userService.createUser(input);
            });
        }
    }

    @Nested
    @DisplayName("Create User Tests")
    class CreateUser {
        private static Stream<UserRegistrationDto> parameterizedUserRegistrationDtoProvider() {
            List<UserRegistrationDto> temp = new LinkedList<>();
            temp.add(new UserRegistrationDto("John", "john@gmail.com", "hellohello"));
            temp.add(new UserRegistrationDto("Amy", "amy.gmail.at", "hellohello2"));
            temp.add(new UserRegistrationDto("Mathew", "mathew.newer@mail.com", "interestingpassword"));
            temp.add(new UserRegistrationDto("Alison", "alison@m.c", "maimaimai"));
            temp.add(new UserRegistrationDto("Mark", "mark@state.com", "hwkdoaksdoasd"));
            temp.add(new UserRegistrationDto("Anna", "anna.stunt@mail.com", "jdasdjiajsidjasidjasdjksadjkasjdkajdkasjdkasjdkajsdksajdkasjdkjasdjasdjasdakl"));
            temp.add(new UserRegistrationDto("Leon", "leon@mail.com", "okok20832"));
            temp.add(new UserRegistrationDto("Lara", "lara.lol@gmx.at", "jlljhallo1321"));
            temp.add(new UserRegistrationDto("Harald", "harald@mymail.com", "wildesPasswort"));
            temp.add(new UserRegistrationDto("Gerald", "Gerald@world.at", "aber warum"));
            return temp.stream();
        }

        @ParameterizedTest
        @DisplayName("assert that the user with the right user data is created")
        @MethodSource("parameterizedUserRegistrationDtoProvider")
        @Transactional
        void createUserIsOK(UserRegistrationDto input) throws ServiceException, ValidationException {
            assertEquals(input, userService.createUser(input));
        }
    }
}