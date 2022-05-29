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
     * @param sid           id of the script
     * @return the role every role was merged into
     */
    RoleDto mergeRoles(MergeRolesDto mergeRolesDto, Long sid);

    /**
     * Gets a role by id in a script.
     *
     * @param sid id of the script
     * @param id id of the role
     * @return a role
     */
    RoleDto getById(Long sid, Long id);
}
