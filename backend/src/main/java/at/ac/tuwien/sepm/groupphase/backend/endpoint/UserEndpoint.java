package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserNotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;

/**
 * Endpoint for user related requests.
 *
 * @author Julia Bernold
 */
@RestController
@RequestMapping(path = UserEndpoint.path)
public class UserEndpoint {
    public static final String path = "/api/v1/users";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public SimpleUserDto getUser(@PathVariable Long id) {
        LOGGER.info("GET " + UserEndpoint.path);
        try {
            userService.getUser(id);
        } catch (ServiceException e) {
            LOGGER.error("Internal Server Error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public UserRegistrationDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.info("POST " + UserEndpoint.path);
        UserRegistrationDto user;
        try {
            user = userService.createUser(userRegistrationDto);
        } catch (ServiceException e) {
            LOGGER.error("Internal Server Error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (ValidationException e) {
            LOGGER.error("Validation Error", e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        return user;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public DetailedUserDto putUser(@RequestParam Boolean passwordChange, @RequestBody DetailedUserDto detailedUserDto, @PathVariable Long id) {
        LOGGER.info("PUT " + UserEndpoint.path + "/{}", id);
        //this method calls either changeUserData or changePassword or both
        DetailedUserDto detailedUser = new DetailedUserDto();
        if (passwordChange) {
            //TODO: either also give the old password to the endpoint or only give the new password to the service layer
            PasswordChangeDto passwordChangeDto = new PasswordChangeDto("", detailedUserDto.getPassword());
            try {
                detailedUser = userService.changePassword(passwordChangeDto, id);
            } catch (ServiceException e) {
                LOGGER.error("Internal Server Error", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
        }
        SimpleUserDto simpleUser = new SimpleUserDto(id, detailedUserDto.getFirstName(), detailedUserDto.getLastName(), detailedUserDto.getEmail(), detailedUserDto.getVerified());
        try {
            simpleUser = userService.changeUserData(simpleUser, id);
        } catch (ServiceException e) {
            LOGGER.error("Internal Server Error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return new DetailedUserDto(id, simpleUser.getFirstName(), simpleUser.getLastName(), simpleUser.getEmail(), detailedUser.getPassword(), simpleUser.getVerified());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser(@RequestParam Long id) {
        LOGGER.info("DELETE " + UserEndpoint.path + " " + id);
        try {
            userService.deleteUser(id);
        } catch (ServiceException e) {
            LOGGER.error("Internal Server Error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PostMapping(path = "/forgotten-password")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void forgottenPassword(@RequestBody String email) {
        LOGGER.info("POST " + UserEndpoint.path + "/forgotten-password");
        try {
            userService.forgotPassword(email);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");
        }
    }
}
