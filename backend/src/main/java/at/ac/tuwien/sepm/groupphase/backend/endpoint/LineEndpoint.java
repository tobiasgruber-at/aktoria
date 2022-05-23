package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public static final String path = "/api/v1/script/{sid}/lines";
    private final LineService lineService;

    public LineEndpoint(LineService lineService) {
        this.lineService = lineService;
    }

    @PatchMapping("/{id}")
    @Secured(Permission.verified)
    public LineDto updateLine(@RequestBody UpdateLineDto updateLineDto, @PathVariable Long sid, @PathVariable Long id) {
        log.info("PUT {}/{}", path, id);
        return lineService.update(updateLineDto, sid, id);
    }

    @DeleteMapping("/{id}")
    @Secured(Permission.verified)
    public void deleteLine(@PathVariable Long sid, @PathVariable Long id) {
        log.info("DELETE {}/{}", path, id);
        lineService.delete(sid, id);
    }
}
