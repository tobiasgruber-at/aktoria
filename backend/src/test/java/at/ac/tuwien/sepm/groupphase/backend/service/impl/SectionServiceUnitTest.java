package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.SectionDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing section services.
 *
 * @author Julia Bernold
 */

@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
class SectionServiceUnitTest {

    @Autowired
    SectionService sectionService;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    SectionMapper sectionMapper;

    @Nested
    @DisplayName("getSection()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    class GetSectionTest {
        @Test
        @DirtiesContext
        @DisplayName("Get a section successfully")
        void getSection() {
            SectionDto received = sectionService.getSection(1L);
            assertEquals(received.getId(), 1L);
            assertEquals(received.getName() + 1, SectionDataGenerator.TEST_SECTION_NAME + 1);
        }

        @Test
        @DirtiesContext
        @DisplayName("Get section throws NotFoundException")
        void getNonexistentSection() {
            assertThrows(NotFoundException.class, () -> sectionService.getSection(0L));
        }
    }


    @Nested
    @DisplayName("createSection()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    class CreateSectionTest {
        @Test
        @DirtiesContext
        @DisplayName("Create new section successfully")
        void createSection() {
            SectionDto section = new SectionDto(null, "Section Name", 1L, 1L, 5L, null);
            section = sectionService.createSection(section);
            assertNotNull(section.getId());
            assertEquals("Section Name", section.getName());
            assertEquals(1L, section.getOwner());
            assertEquals(1L, section.getStartLine());
            assertEquals(5L, section.getEndLine());
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with an invalid user")
        void createSectionInvalidUser() {
            SectionDto section = new SectionDto(null, "Section Name", 0L, 1L, 2L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with an invalid name")
        void createSectionInvalidName() {
            SectionDto section = new SectionDto(null, "   ", 0L, 1L, 2L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with an invalid starting line")
        void createSectionInvalidStart() {
            SectionDto section = new SectionDto(null, "Section Name", 1L, 0L, 2L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with an invalid ending line")
        void createSectionInvalidEnd() {
            SectionDto section = new SectionDto(null, "Section Name", 1L, 1L, 0L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with starting and ending lines from different scripts")
        void createSectionDifferentScripts() {
            SectionDto section = new SectionDto(null, "Section Name", 1L, 1L, 200L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }

        @Test
        @DirtiesContext
        @DisplayName("Create new section with a too long name")
        void createSectionTooLongName() {
            SectionDto section = new SectionDto(null, "a".repeat(101), 1L, 1L, 2L, null);
            assertThrows(ValidationException.class, () -> sectionService.createSection(section));
        }
    }


    @Nested
    @DisplayName("deleteSection()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    class DeleteSectionTest {
        @Test
        @DirtiesContext
        @DisplayName("Delete section successfully")
        void deleteSection() {
            Optional<Section> section = sectionRepository.findById(1L);
            assertTrue(section.isPresent());
            sectionService.deleteSection(section.get().getId());
            section = sectionRepository.findById(1L);
            assertTrue(section.isEmpty());
        }

        @Test
        @DirtiesContext
        @DisplayName("Delete section throws NotFoundException")
        void deleteNonexistentSection() {
            assertThrows(NotFoundException.class, () -> sectionService.deleteSection(0L));
        }
    }

    @Nested
    @DisplayName("getAllSections()")
    @SpringBootTest
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class AllSectionsTest {
        @Test
        @Transactional
        @DisplayName("Gets all sections")
        void getAllSections() {
            List<SectionDto> expected = sectionMapper.sectionListToSectionDtoList(sectionRepository.findAll());
            List<SectionDto> received = sectionService.getAllSections();
            assertTrue(received.containsAll(expected));
        }
    }

}