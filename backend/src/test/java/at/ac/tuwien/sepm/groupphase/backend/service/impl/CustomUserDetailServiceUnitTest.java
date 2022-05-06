package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing user services.
 *
 * @author Luke Nemeskeri
 */
@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
class CustomUserDetailServiceUnitTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("assert that the user with the right user data is created")
    void createUserIsOk() throws ServiceException, ValidationException {
        assertEquals(new UserRegistrationDto("John", "john@gmail.com", "hellohello"), userService.createUser(new UserRegistrationDto("John", "john@gmail.com", "hellohello")));
        assertEquals(new UserRegistrationDto("Amy", "amy.gmail.at", "hellohello2"), userService.createUser(new UserRegistrationDto("Amy", "amy.gmail.at", "hellohello2")));
        assertEquals(new UserRegistrationDto("Mathew", "mathew.newer@mail.com", "interestingpassword"), userService.createUser(new UserRegistrationDto("Mathew", "mathew.newer@mail.com", "interestingpassword")));
        assertEquals(new UserRegistrationDto("Alison", "alison@m.c", "maimaimai"), userService.createUser(new UserRegistrationDto("Alison", "alison@m.c", "maimaimai")));
        assertEquals(new UserRegistrationDto("Mark", "mark@state.com", "hw"), userService.createUser(new UserRegistrationDto("Mark", "mark@state.com", "hw")));
        assertEquals(new UserRegistrationDto("Anna", "anna.stunt@mail.com", "jdasdjiajsidjasidjasdjksadjkasjdkajdkasjdkasjdkajsdksajdkasjdkjasdjasdjasdakl"),
            userService.createUser(new UserRegistrationDto("Anna", "anna.stunt@mail.com", "jdasdjiajsidjasidjasdjksadjkasjdkajdkasjdkasjdkajsdksajdkasjdkjasdjasdjasdakl")));
    }

    @Test
    @DisplayName("assert that ValidationException is thrown")
    void createUserThrowsException() {
        //whitespaces Tests
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("", "anna.stunt@mail.com", "jdasdjiajsid")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("  ", "anna.stunt@mail.com", "jalloo")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("hallo", "", "jdasdjiajsidjo")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("halloeeqe", "  ", "jdasdjiajsidjoopopopop")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("test3", "jan.stunt@mail.com", "")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("test1", "janis.stunt@mail.com", "   ")));

        //TODO: validation tests
        //Name Tests
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("", "anna.stunt@mail.com", "jdasdjiajsidjasidjasdjksadjkasjdkajdkasjdkasjdkajsdksajdkasjdkjasdjasdjasdakl")));
        assertThrows(ServiceException.class, () -> userService.createUser(new UserRegistrationDto("", "anna.stunt@mail.com", "jdasdjiajsidjasidjasdjksadjkasjdkajdkasjdkasjdkajsdksajdkasjdkjasdjasdjasdakl")));
    }


    @Test
    @DisplayName("assert that changing user data returns the changed user datas")
    void changeUserDataIsOk() {

    }

    @Test
    void deleteUser() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void changeEmail() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void findApplicationUserByEmail() {
    }
}