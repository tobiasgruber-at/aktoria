package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Endpoint for user related requests.
 *
 * @author Julia Bernold
 */
@RestController
@RequestMapping(path = UserEndpoint.path)
@Slf4j
public class UserEndpoint {
    public static final String path = "/api/v1/users";
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SimpleUserDto getUser(@PathVariable Long id) throws ServiceException {
        log.info("GET " + UserEndpoint.path);
        try {
            return userService.getUser(id);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SimpleUserDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) throws ServiceException {
        log.info("POST " + UserEndpoint.path);
        try {
            return userService.createUser(userRegistrationDto);
        } catch (ValidationException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public DetailedUserDto patchUser(@RequestParam Boolean passwordChange, @RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) throws ServiceException, ValidationException {
        log.info("PATCH " + UserEndpoint.path + "/{}", id);
        //this method calls either changeUserData or changePassword or both
        try {
            return userService.patch(updateUserDto, passwordChange, id);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }

    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser(@PathVariable Long id) throws ServiceException {
        log.info("DELETE {}/{}", UserEndpoint.path, id);
        //  try {
        userService.deleteUser(id);
        //  }
        /*TODO: uncomment as soon as Service is implemented
        catch(UserNotFoundException e){
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }*/
    }

    @PostMapping(path = "/forgotten-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void forgottenPassword(@RequestBody String email) {
        log.info("POST " + UserEndpoint.path + "/forgotten-password");
        try {
            userService.forgotPassword(email);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/submitToken/{token}")
    @ResponseStatus(HttpStatus.OK)
    public String verifyEmailToken(@PathVariable String token) {
        log.info("POST " + UserEndpoint.path + "/token");
        userService.verifyEmail(token);
        return "account verified";
    }

    @PostMapping(path = "/verificationToken")
    @ResponseStatus(HttpStatus.OK)
    public void resendEmailVerificationToken(@RequestBody Long id) {
        userService.resendEmailVerificationLink(id);
    }
}
