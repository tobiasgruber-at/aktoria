package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class for testing section services.
 *
 * @author Julia Bernold
 */

@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class SectionServiceUnitTest {

    //TODO implement tests

    @Autowired
    SectionService sectionService;


    @Nested
    @SpringBootTest
    @DisplayName("getSection()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class GetSectionTest {
        @Test
        @Transactional
        @DisplayName("Get a section successfully")
        void getSection() {

        }

        @Test
        @Transactional
        @DisplayName("Get a nonexistent section")
        void getNonexistentSection() {

        }

    }


    @Nested
    @DisplayName("createSection()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class CreateSectionTest {
        @Test
        @Transactional
        @DisplayName("Create new section successfully")
        void createNewSection() {
        }
        //TODO: add all the different test cases
    }


    @Nested
    @DisplayName("deleteSection()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class DeleteSectionTest {
        @Test
        @Transactional
        @DisplayName("Delete section successfully")
        void deleteSection() {
        }

        @Test
        @Transactional
        @DisplayName("Delete nonexistent section")
        void deleteNonexistentSection() {
        }
    }
}