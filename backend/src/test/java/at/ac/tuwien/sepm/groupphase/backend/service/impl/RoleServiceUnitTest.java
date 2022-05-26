package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class for testing role services.
 *
 * @author Luke Nemeskeri
 */
@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class RoleServiceUnitTest {

    @Autowired
    RoleService roleService;

    @Autowired
    ScriptService scriptService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Transactional
    @DisplayName("mergeRoles")
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + 2 + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + 2,
        roles = {Role.verified})
    void mergeRoles() {
        List<Long> rolesToMerge = new LinkedList<Long>(Arrays.asList(4L, 2L, 3L));
        RoleDto saved = roleService.mergeRoles(new MergeRolesDto(rolesToMerge, "MERGEROLE"), 2L, 1L);
        Optional<at.ac.tuwien.sepm.groupphase.backend.entity.Role> roleOpt = roleRepository.findById(4L);
        at.ac.tuwien.sepm.groupphase.backend.entity.Role role;
        if (roleOpt.isPresent()) {
            role = roleOpt.get();
        } else {
            throw new NotFoundException("User existiert nicht!");
        }
        assertEquals(3, scriptService.findById(1L).getRoles().size());
    }
}