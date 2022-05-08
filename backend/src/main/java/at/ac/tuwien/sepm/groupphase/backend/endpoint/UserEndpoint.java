package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SimpleUserDto getUser(@PathVariable Long id) throws ServiceException {
        LOGGER.info("GET " + UserEndpoint.path);
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRegistrationDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) throws ServiceException {
        LOGGER.info("POST " + UserEndpoint.path);
        try {
            return userService.createUser(userRegistrationDto);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public DetailedUserDto patchUser(@RequestParam Boolean passwordChange, @RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) throws ServiceException {
        LOGGER.info("PATCH " + UserEndpoint.path + "/{}", id);
        //this method calls either changeUserData or changePassword or both
        // TODO: create path method in Service
        // return userService.patch(updateUserDto, passwordChange, id);
        return null;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser(@PathVariable Long id) throws ServiceException {
        LOGGER.info("DELETE {}/{}", UserEndpoint.path, id);
        //  try {
        userService.deleteUser(id);
        //  }
        /*TODO: uncomment as soon as Service is implemented
        catch(UserNotFoundException e){
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }*/
    }

    @PostMapping(path = "/forgotten-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void forgottenPassword(@RequestBody String email) {
        LOGGER.info("POST " + UserEndpoint.path + "/forgotten-password");
        try {
            userService.forgotPassword(email);
        } catch (UserNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
