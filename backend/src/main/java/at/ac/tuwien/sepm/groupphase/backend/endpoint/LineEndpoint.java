package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Line Endpoint.
 *
 * @author Simon Josef Kreuzpointner
 */
@RestController
@RequestMapping(LineEndpoint.path)
@Slf4j
public class LineEndpoint {
    public static final String path = "/api/v1/lines";
    private final LineService lineService;

    public LineEndpoint(LineService lineService) {
        this.lineService = lineService;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public LineDto updateLine(@RequestBody UpdateLineDto updateLineDto, @PathVariable Long id) {
        log.info("PATCH {}/{}", path, id);
        return lineService.update(updateLineDto, id);
    }
}
