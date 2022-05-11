package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping(path = "/new", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public SimpleScriptDto uploadScript(@RequestPart("file") MultipartFile multipartFile) throws ServiceException {
        log.info("POST {}/new", path);

        try {
            return scriptService.create(multipartFile);
        } catch (IllegalFileFormatException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScriptDto saveScript(@RequestBody SimpleScriptDto simpleScriptDto) throws ServiceException {
        log.info("POST {}", path);

        return scriptService.save(simpleScriptDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Stream<ScriptPreviewDto> getScriptPreviews() throws ServiceException {
        log.info("GET {}", path);

        return scriptService.findAllPreviews();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScriptDto getScriptById(@PathVariable Long id) throws ServiceException {
        log.info("GET {}/{}", path, id);

        return scriptService.findById(id);
    }
}
