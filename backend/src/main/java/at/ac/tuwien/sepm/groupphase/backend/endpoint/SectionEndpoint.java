package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Section endpoint.
 *
 * @author Julia Bernold
 */
@RestController
@RequestMapping(path = SectionEndpoint.path)
@Slf4j
public class SectionEndpoint {
    public static final String path = "/api/v1/scripts/sections";
    private final SectionService sectionService;

    public SectionEndpoint(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public SectionDto getSectionById(@PathVariable Long id) {
        log.info("GET {}/{}", path, id);
        return sectionService.getSection(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(Permission.verified)
    public SectionDto saveSection(@RequestBody SectionDto sectionDto) {
        log.info("POST {}", path);
        return sectionService.createSection(sectionDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(Permission.verified)
    public void deleteSection(@PathVariable Long id) {
        log.info("DELETE {}/{}", path, id);
        sectionService.deleteSection(id);
    }
}
