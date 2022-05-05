package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import org.h2.engine.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/users")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    public UserEndpoint() {
        //TODO: implement constructor ---
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public SimpleUserDto postUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.info("POST /api/v1/users");
//TODO: write tests and implement method
        return null;

    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public SimpleUserDto putUser(@RequestBody SimpleUserDto simpleUserDto, @PathVariable Long id) {
        LOGGER.info("PUT /api/v1/users/{}", id);
        //TODO: write tests and implement method
        return null;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteUser(@RequestParam Long id) {
        //TODO: write tests and implement method
    }

}
