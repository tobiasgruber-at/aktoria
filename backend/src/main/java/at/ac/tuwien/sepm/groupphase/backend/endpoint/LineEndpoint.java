package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(LineEndpoint.path)
@Slf4j
public class LineEndpoint {
    public static final String path = "/api/v1/lines";
    private final LineService lineService;

    public LineEndpoint(LineService lineService) {
        this.lineService = lineService;
    }

    @PutMapping("/{id}")
    public LineDto updateLine(@RequestBody String content, @PathVariable Long id) {
        log.info("PUT {}/{}", path, id);
        return lineService.update(content, id);
    }
}
