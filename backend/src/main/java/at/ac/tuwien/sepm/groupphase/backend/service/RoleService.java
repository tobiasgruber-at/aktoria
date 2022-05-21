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

    RoleDto mergeRoles(MergeRolesDto mergeRolesDto, Long id);
}
