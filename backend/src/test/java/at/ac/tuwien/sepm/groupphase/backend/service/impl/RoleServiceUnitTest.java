package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing role services.
 *
 * @author Luke Nemeskeri
 */
@ActiveProfiles({ "test", "datagen" })
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
    @DirtiesContext
    @DisplayName("mergeRoles")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    void mergeRoles() {
        List<Long> rolesToMerge = new LinkedList<>(List.of(2L, 4L, 3L));
        List<Long> rolesToMergeFail = new LinkedList<>(List.of(7L, 4L, 3L));
        assertThrows(ValidationException.class, () -> roleService.mergeRoles(new MergeRolesDto(rolesToMergeFail, "MEROLE"), 1L));
        roleService.mergeRoles(new MergeRolesDto(rolesToMerge, "MERGEROLE"), 1L);

        assertEquals(3, scriptService.findById(1L).getRoles().size());
        assertThrows(NotFoundException.class, () -> {
            Optional<at.ac.tuwien.sepm.groupphase.backend.entity.Role> role = roleRepository.findById(4L);
            if (role.isEmpty()) {
                throw new NotFoundException();
            }
        });
    }

    @Test
    @DirtiesContext
    @DisplayName("mergeRoles with only 1 role")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    void mergeRolesIsNotNeeded() {
        List<Long> rolesToMerge = new LinkedList<>(List.of(5L));
        assertEquals(5L, roleService.mergeRoles(new MergeRolesDto(rolesToMerge, "MERGE"), 1L).getId());
    }
}