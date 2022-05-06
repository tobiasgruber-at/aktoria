package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;

@RestController
@RequestMapping(path = "/api/v1/scripts")
public class ScriptEndpoint {

    private final ScriptService scriptService;

    public ScriptEndpoint(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public StagedScriptDto uploadScript(@RequestBody File file) {
        try {
            return scriptService.newScript(file);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
