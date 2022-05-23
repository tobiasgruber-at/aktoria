package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MergeRolesDto;
import at.ac.tuwien.sepm.groupphase.backend.service.RoleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    @Transactional
    @DisplayName("mergeRoles")
    void mergeRoles() {
        List<Long> rolesToMerge = new LinkedList<Long>(Arrays.asList(1L, 2L, 3L));
        roleService.mergeRoles(new MergeRolesDto(rolesToMerge, "Mergerole"), 2L, 1L);
        assertEquals(3, scriptService.findById(1L).getRoles().size());
    }
}