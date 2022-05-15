package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.repository.SecureTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

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
    private final SecureTokenRepository secureTokenRepository;

    public UserEndpoint(UserService userService, SecureTokenRepository secureTokenRepository) {
        this.userService = userService;
        this.secureTokenRepository = secureTokenRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SimpleUserDto getUser(@RequestParam String email) {
        log.info("GET {}/{}", path, email);
        return userService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleUserDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("POST {}", path);
        return userService.create(userRegistrationDto);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DetailedUserDto patchUser(@RequestParam Boolean passwordChange, @RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        log.info("PATCH {}/{}", path, id);
        return userService.patch(updateUserDto, passwordChange, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("DELETE {}/{}", UserEndpoint.path, id);
        userService.delete(id);
    }

    @PostMapping(path = "/reset-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forgottenPassword(@RequestBody String email) {
        log.info("POST {}/reset-password", path);
        userService.forgotPassword(email);
    }

    @PutMapping(path = "/change-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changePassword(@RequestBody PasswordChangeDto passwordChange) {
        log.info("POST {}/reset-password", path);
        userService.changePassword(passwordChange, null);
    }

    @PostMapping(path = "/verification")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public void verifyEmailToken(@RequestBody String token) {
        log.info("POST {}/verification", path);
        userService.verifyEmail(token);
    }

    @PostMapping(path = "/tokens")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_USER")
    public void resendEmailVerificationToken() {
        log.info("POST {}/verification", path);
        userService.resendEmailVerificationLink();
    }
}
