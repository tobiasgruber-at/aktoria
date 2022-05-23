package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Session endpoint.
 *
 * @author Marvin Flandorfer
 */
@RestController
@RequestMapping(path = SessionEndpoint.path)
@Slf4j
public class SessionEndpoint {
    public static final String path = "api/v1/session";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_VERIFIED")
    public SessionDto startSession(@RequestBody SimpleSessionDto simpleSessionDto) {
        log.info("POST {}", path);
        return null;
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_VERIFIED")
    public SessionDto updateSession(@PathVariable Long id) {
        log.info("PATCH {}", path);
        return null;
    }
}
