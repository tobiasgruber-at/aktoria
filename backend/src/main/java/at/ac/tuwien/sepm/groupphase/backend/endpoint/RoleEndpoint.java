package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Role endpoint.
 *
 * @author Luke Nemeskeri
 */
@RestController
@RequestMapping(path = RoleEndpoint.path)
@Slf4j
public class RoleEndpoint {
    public static final String path = "/api/v1/scripts/{sid}/roles";
    private final RoleService roleService;

    public RoleEndpoint(RoleService roleService) {
        this.roleService = roleService;
    }

    @PatchMapping()
    @Secured(Permission.verified)
    public RoleDto mergeRoles(@RequestBody MergeRolesDto mergeRolesDto, @PathVariable Long sid) {
        log.info("PATCH {}", path);
        return roleService.mergeRoles(mergeRolesDto, sid);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(Permission.verified)
    public RoleDto getRoleById(@PathVariable Long sid, @PathVariable Long id) {
        log.info("GET {}/{}", path, id);
        return roleService.getById(sid, id);
    }
}
