package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.validation.RoleValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Service for roles.
 *
 * @author Luke Nemeskeri
 */
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
    private final ScriptRepository scriptRepository;
    private final SessionService sessionService;
    private final SessionRepository sessionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleValidation roleValidation, LineRepository lineRepository, RoleMapper roleMapper, AuthorizationService authorizationService,
                           ScriptService scriptService, UserMapper userMapper, ScriptRepository scriptRepository, SessionRepository sessionRepository, SessionService sessionService) {
        this.roleRepository = roleRepository;
        this.roleValidation = roleValidation;
        this.lineRepository = lineRepository;
        this.roleMapper = roleMapper;
        this.authorizationService = authorizationService;
        this.scriptService = scriptService;
        this.userMapper = userMapper;
        this.scriptRepository = scriptRepository;
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public RoleDto mergeRoles(MergeRolesDto mergeRolesDto, Long sid) {
        log.trace("merge roles into {}", mergeRolesDto.getIds().get(0));

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> scriptOptional = scriptRepository.findById(sid);
        Script script;
        if (scriptOptional.isPresent()) {
            script = scriptOptional.get();
        } else {
            throw new NotFoundException();
        }
        if (!script.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("Dieser User ist nicht berechtigt diese Datei zu bearbeiten");
        }
        List<Role> allReplaceRoles = roleRepository.findAllById(mergeRolesDto.getIds());
        if (!(script.getRoles().containsAll(allReplaceRoles))) {
            throw new ValidationException("Eine oder mehrere Rollen sind nicht in diesem Script enthalten");
        }
        Long idToKeep = mergeRolesDto.getIds().get(0);
        Optional<Role> keepOptional = roleRepository.findById(idToKeep);
        Role keep;
        if (keepOptional.isPresent()) {
            keep = keepOptional.get();
        } else {
            throw new NotFoundException("Rolle existiert nicht!");
        }
        if (allReplaceRoles.size() == 1
            && allReplaceRoles.get(0).getId().equals(idToKeep)) {
            return roleMapper.roleToRoleDto(keep);
        }
        List<Line> lines = new LinkedList<>();
        for (Role r : allReplaceRoles) {
            lines.addAll(r.getLines());
        }
        for (int i = 0; i < lines.size(); i++) {
            allReplaceRoles.forEach(lines.get(i).getSpokenBy()::remove);
            lines.get(i).getSpokenBy().add(keep);
            lineRepository.save(lines.get(i));
            if (!(keep.getLines().contains(lines.get(i)))) {
                keep.getLines().add(lineRepository.getById(lines.get(i).getId()));
            }
        }
        allReplaceRoles.remove(keep);
        List<Long> idsToDelete = new LinkedList<>();
        for (int i = 0; i < allReplaceRoles.size(); i++) {
            idsToDelete.add(allReplaceRoles.get(i).getId());
        }
        for (int i = 0; i < allReplaceRoles.size(); i++) {
            allReplaceRoles.get(i).setLines(null);
        }

        List<Session> sessions = new LinkedList<>();
        for (int i = 0; i < allReplaceRoles.size(); i++) {
            sessions.addAll(allReplaceRoles.get(i).getSessions());
        }

        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setRole(keep);
            sessionRepository.save(sessions.get(i));
        }

        Script script = scriptRepository.getById(sid);

        allReplaceRoles.forEach(script.getRoles()::remove);
        scriptRepository.save(script);
        roleRepository.deleteAllById(idsToDelete);
        roleValidation.validateRoleName(mergeRolesDto.getNewName());
        keep.setName(mergeRolesDto.getNewName());
        roleRepository.saveAndFlush(keep);
        sessionService.deprecateAffected(script.getId());
        return roleMapper.roleToRoleDto(keep);
    }

    @Override
    public RoleDto getById(Long sid, Long id) {
        log.trace("getById(sid = {}, id = {})", sid, id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
        Role role = roleOpt.get();
        if (!role.getScript().getOwner().getId().equals(user.getId()) && !role.getScript().getParticipants().contains(user)) {
            throw new UnauthorizedException("User is not authorized to view this Role");
        }
        if (!role.getScript().getId().equals(sid)) {
            throw new ConflictException("Role does not belong to this script");
        }
        return roleMapper.roleToRoleDto(role);
    }

}