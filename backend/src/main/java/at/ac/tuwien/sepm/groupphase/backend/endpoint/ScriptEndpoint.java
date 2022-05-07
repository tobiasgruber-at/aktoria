package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(path = ScriptEndpoint.path)
public class ScriptEndpoint {
    public static final String path = "/api/v1/scripts";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ScriptService scriptService;

    public ScriptEndpoint(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @PostMapping(path = "/upload")
    @ResponseStatus(HttpStatus.OK)
    public StagedScriptDto uploadScript(@RequestBody File file) {
        LOGGER.info("POST {}/upload", path);

        try {
            return scriptService.newScript(file);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
