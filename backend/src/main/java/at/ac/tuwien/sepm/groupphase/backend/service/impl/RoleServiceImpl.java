package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.validation.RoleValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleValidation roleValidation;
    private final LineRepository lineRepository;
    private final RoleMapper roleMapper;
    private final AuthorizationService authorizationService;
    private final ScriptService scriptService;
    private final UserMapper userMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleValidation roleValidation, LineRepository lineRepository, RoleMapper roleMapper, AuthorizationService authorizationService,
                           ScriptService scriptService, UserMapper userMapper) {
        this.roleRepository = roleRepository;
        this.roleValidation = roleValidation;
        this.lineRepository = lineRepository;
        this.roleMapper = roleMapper;
        this.authorizationService = authorizationService;
        this.scriptService = scriptService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public RoleDto mergeRoles(MergeRolesDto mergeRolesDto, Long id, Long sid) {
        log.trace("merge roles into {}", id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        if (!(userMapper.userToSimpleUserDto(user).equals(scriptService.findById(sid).getOwner()))) {
            throw new UnauthorizedException("Dieser User ist nicht berechtigt diese Datei zu bearbeiten");
        }

        Optional<Role> keepOptional = roleRepository.findById(id);
        Role keep;
        if (keepOptional.isPresent()) {
            keep = keepOptional.get();
        } else {
            throw new NotFoundException("Rolle exisitiert nicht!");
        }

        List<Role> allReplaceRoles = roleRepository.findAllById(mergeRolesDto.getIds());
        Set<Role> removeFromLine = new HashSet<>(allReplaceRoles);

        for (Role r : allReplaceRoles) {
            for (Line i : r.getLines()) {
                i.getSpokenBy().removeAll(removeFromLine);
                i.getSpokenBy().add(keep);
                lineRepository.saveAndFlush(i);
            }
        }

        allReplaceRoles.remove(keep);
        roleRepository.deleteAll(allReplaceRoles);

        roleValidation.validateRoleName(mergeRolesDto.getNewName());
        keep.setName(mergeRolesDto.getNewName());
        roleRepository.saveAndFlush(keep);
        return roleMapper.roleToRoleDto(keep);
    }
}
