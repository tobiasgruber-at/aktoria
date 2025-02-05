package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.stream.Stream;

/**
 * Script endpoint.
 *
 * @author Simon Josef Kreuzpointner
 */
@RestController
@RequestMapping(path = ScriptEndpoint.path)
@Slf4j
public class ScriptEndpoint {
    public static final String path = "/api/v1/scripts";
    private final ScriptService scriptService;

    public ScriptEndpoint(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @PostMapping(path = "/new", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public SimpleScriptDto uploadScript(@RequestPart("file") MultipartFile multipartFile, @RequestPart(value = "startPage", required = false) String startPage) {
        log.info("POST {}/new", path);
        return scriptService.parse(multipartFile, startPage == null ? 0 : Integer.parseInt(startPage));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(Permission.verified)
    public ScriptDto saveScript(@RequestBody SimpleScriptDto simpleScriptDto) {
        log.info("POST {}", path);
        return scriptService.save(simpleScriptDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public Stream<ScriptPreviewDto> getScriptPreviews() {
        log.info("GET {}", path);
        return scriptService.findAllPreviews();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public ScriptDto getScriptById(@PathVariable Long id) {
        log.info("GET {}/{}", path, id);
        return scriptService.findById(id);
    }

    @GetMapping(path = "/session")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public ScriptDto getScriptBySessionId(@RequestParam Long id) {
        log.info("GET {}/session?id={}", path, id);
        return scriptService.getBySessionId(id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(Permission.verified)
    public void deleteScript(@PathVariable Long id) {
        log.info("DELETE {}/{}", ScriptEndpoint.path, id);
        scriptService.delete(id);
    }

    @PostMapping(path = "/{id}/invitations")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public void inviteUser(@PathVariable Long id, @RequestBody String email) {
        log.info("POST /{}/invitation", id);
        scriptService.invite(id, email);
    }

    @PostMapping(path = "/{id}/invite-link")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public String inviteLink(@PathVariable Long id) {
        log.info("POST /{}/invite-link", id);
        return scriptService.inviteLink(id);
    }

    @PostMapping(path = "/{id}/participants")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public void addParticipant(@PathVariable Long id, @RequestBody String token) {
        log.info("POST /{}/participants", id);
        scriptService.addParticipant(id, token);
    }

    @DeleteMapping(path = "/{id}/participants/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(Permission.verified)
    public void deleteParticipant(@PathVariable Long id, @PathVariable String email) {
        log.info("DELETE /{}/participants/{}", id, email);
        scriptService.deleteParticipant(id, email);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public ScriptDto updateScript(@RequestBody @Valid UpdateScriptDto updateScriptDto, @PathVariable Long id) {
        log.info("PATCH {}/{}", ScriptEndpoint.path, id);
        return scriptService.update(updateScriptDto, id);
    }
}
