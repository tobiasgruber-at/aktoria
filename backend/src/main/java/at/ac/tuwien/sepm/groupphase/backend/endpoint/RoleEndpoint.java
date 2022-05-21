package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = RoleEndpoint.path)
@Slf4j
public class RoleEndpoint {
    public static final String path = "/api/v1/roles";
    private final RoleService roleService;

    public RoleEndpoint(RoleService roleService) {
        this.roleService = roleService;
    }

    @PatchMapping(path = "/{id}")
    public RoleDto mergeRoles(@RequestBody MergeRolesDto mergeRolesDto, @PathVariable Long id) {
        log.info("PATCH {}/{}", path, id);
        return roleService.mergeRoles(mergeRolesDto, id);
    }
}
