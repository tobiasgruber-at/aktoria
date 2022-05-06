package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing user services
 *
 * @author luke nemeskeri
 */
@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class CustomUserDetailServiceUnitTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("assert that changing user data returns the changed user data")
    void changeUserDataIsOk() {

    }

    @Test
    void deleteUser() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void getUser() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void findApplicationUserByEmail() {
    }

    @Nested
    @DisplayName("Creating user with invalid inputs")
    class CreateUserThrowsExceptions {
        private static Stream<UserRegistrationDto> parameterizedCreateUserThrowsExceptionProvide() {
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
        @MethodSource("parameterizedCreateUserThrowsExceptionProvide")
        void createUserThrowsException(UserRegistrationDto input) {
            //whitespaces, null and too long tests
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
        void createUserIsOK(UserRegistrationDto input) throws ServiceException, ValidationException {
            assertEquals(input, userService.createUser(input));
        }
    }

}