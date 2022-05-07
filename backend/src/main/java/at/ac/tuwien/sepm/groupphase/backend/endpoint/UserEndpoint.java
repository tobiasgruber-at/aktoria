package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
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
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public SimpleUserDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.info("POST " + UserEndpoint.path);
        //TODO: write tests and implement method
        return null;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public DetailedUserDto putUser(@RequestBody DetailedUserDto detailedUserDto, @PathVariable Long id) {
        LOGGER.info("PUT " + UserEndpoint.path + "/{}", id);
        //TODO: write tests and implement method
        //this method calls either changeUserData or changePassword or both
        return null;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser(@RequestParam Long id) {
        LOGGER.info("DELETE " + UserEndpoint.path + " " + id);
        //TODO: write tests and implement method
    }

    @PostMapping(path = "/forgotten-password")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void forgottenPassword(@RequestBody String email) {
        LOGGER.info("POST " + UserEndpoint.path + "/forgotten-password");
        //TODO: write tests and implement method
    }
}
