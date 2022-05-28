package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.enums.Permission;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;

    @Autowired
    public AuthorizationServiceImpl(UserRepository userRepository, ScriptRepository scriptRepository) {
        this.userRepository = userRepository;
        this.scriptRepository = scriptRepository;
    }

    @Transactional
    @Override
    public void checkBasicAuthorization(Long id) {
        if (isAdmin()) {
            return;
        }
        if (isLoggedInAs(id)) {
            return;
        }
        throw new UnauthorizedException();
    }

    @Override
    @Transactional
    public void checkBasicAuthorization(String email) {
        if (isAdmin()) {
            return;
        }
        if (isLoggedInAs(email)) {
            return;
        }
        throw new UnauthorizedException();
    }

    @Override
    public void isOwnerOfScript(Long scriptId) {
        if (isAdmin()) {
            return;
        }
        Optional<Script> scriptOptional = scriptRepository.findById(scriptId);
        if (scriptOptional.isPresent()) {
            Long ownerId = scriptOptional.get().getOwner().getId();
            if (isLoggedInAs(ownerId)) {
                return;
            }
            throw new UnauthorizedException();

        }
        throw new NotFoundException("Skript existiert nicht");
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        Collection<GrantedAuthority> authorities;
        authorities = (Collection<GrantedAuthority>) auth.getAuthorities();

        return authorities.containsAll(AuthorityUtils.createAuthorityList(Permission.admin));
    }

    @Override
    @Transactional
    public boolean isLoggedInAs(Long id) {
        User user = getLoggedInUser();
        if (user == null) {
            return false;
        }
        return Objects.equals(user.getId(), id);
    }

    @Override
    @Transactional
    public boolean isLoggedInAs(String email) {
        User user = getLoggedInUser();
        if (user == null) {
            return false;
        }
        return Objects.equals(user.getEmail(), email);
    }

    @Override
    @Transactional
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        String userEmail;
        if (auth.getPrincipal() instanceof String) {
            userEmail = (String) auth.getPrincipal();
        } else {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) (auth.getPrincipal());
            userEmail = user.getUsername();
        }
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.orElse(null);
    }
}
