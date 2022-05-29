package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

/**
 * Session endpoint.
 *
 * @author Marvin Flandorfer
 */
@RestController
@RequestMapping(path = SessionEndpoint.path)
@Slf4j
public class SessionEndpoint {
    public static final String path = "/api/v1/sessions";
    public final SessionService sessionService;

    public SessionEndpoint(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(Permission.verified)
    public SessionDto startSession(@RequestBody SimpleSessionDto simpleSessionDto) {
        log.info("POST {}", path);
        return sessionService.save(simpleSessionDto);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public SessionDto updateSession(@PathVariable Long id, @RequestBody UpdateSessionDto updateSessionDto) {
        log.info("PATCH {}/{}", path, id);
        return sessionService.update(updateSessionDto, id);
    }

    @PostMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public SessionDto endSession(@PathVariable Long id) {
        log.info("POST {}/{}", path, id);
        return sessionService.finish(id);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public SessionDto getSessionById(@PathVariable Long id) {
        log.info("GET {}/{}", path, id);
        return sessionService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public Stream<SessionDto> getQuerySessions(@RequestParam(required = false) Boolean deprecated,
                                               @RequestParam(required = false) Long sectionId,
                                               @RequestParam(required = false) Boolean past) {
        log.info("GET {}/past?deprecated={}&sectionId={}&past={}", path, deprecated, sectionId, past);
        return sessionService.findQuerySessions(deprecated, sectionId, past);
    }
}
