package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;

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
    public SimpleUserDto getUser(@PathVariable Long id) {
        log.info("GET {}/{}", path, id);

        try {
            return userService.findById(id);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleUserDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) throws ServiceException {
        log.info("POST {}", path);

        try {
            return userService.create(userRegistrationDto);
        } catch (ValidationException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DetailedUserDto patchUser(@RequestParam Boolean passwordChange, @RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) throws ServiceException {
        log.info("PATCH {}/{}", path, id);

        try {
            return userService.patch(updateUserDto, passwordChange, id);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) throws ServiceException {
        log.info("DELETE {}/{}", UserEndpoint.path, id);

        try {
            userService.delete(id);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping(path = "/reset-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forgottenPassword(@RequestBody String email) {
        log.info("POST {}/reset-password", path);

        try {
            userService.forgotPassword(email);
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping(path = "/submitToken")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public void verifyEmailToken(@RequestBody String token) throws InvalidTokenException {
        log.info("POST {}/submitToken/{}", path, token);
        userService.verifyEmail(token);
    }

    @PostMapping(path = "/verificationToken")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public void resendEmailVerificationToken() throws ServiceException {
        log.info("POST {}/verificationToken", path);
        userService.resendEmailVerificationLink();
    }
}
