package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import org.springframework.stereotype.Service;

/**
 * Service for roles.
 *
 * @author Luke Nemeskeri
 */
@Service
public interface RoleService {

    /**
     * Merge multiple roles into one.
     *
     * @param mergeRolesDto filled with all roles to merge and the name of the new role
     * @param id            of the role to merge every role into
     * @return the role every role was merged into
     */
    RoleDto mergeRoles(MergeRolesDto mergeRolesDto, Long id);
}
