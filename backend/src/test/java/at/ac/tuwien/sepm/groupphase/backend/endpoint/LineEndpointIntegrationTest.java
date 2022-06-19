package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
class LineEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).apply(springSecurity()).build();
    }

    @Nested
    @DisplayName("updateLine()")
    class UpdateLine {

        private static Stream<UpdateLineDto> updateLineDtoValidProvider() {
            final List<UpdateLineDto> lineDtoList = new ArrayList<>();
            lineDtoList.add(new UpdateLineDto(null, null, null,null));
            lineDtoList.add(new UpdateLineDto("New Content 1.", null, null, null));
            lineDtoList.add(new UpdateLineDto("New Content 2.", true, null, null));
            lineDtoList.add(new UpdateLineDto("New Content 3.", true, new LinkedList<>(List.of(1L, 2L, 3L)), null));
            lineDtoList.add(new UpdateLineDto("New Content 4.", true, new LinkedList<>(), null));
            return lineDtoList.stream();
        }

        private static Stream<UpdateLineDto> updateLineDtoInvalidProvider() {
            final List<UpdateLineDto> lineDtoList = new ArrayList<>();
            lineDtoList.add(new UpdateLineDto("new Content 1.", null, null ,null));
            lineDtoList.add(new UpdateLineDto("New Content 2", null, null ,null));
            lineDtoList.add(new UpdateLineDto("New \n Content 3.", null, null ,null));
            lineDtoList.add(new UpdateLineDto("New \r Content 4.", null, null ,null));
            lineDtoList.add(new UpdateLineDto("New \t Content 5.", null, new LinkedList<>(List.of(-16L, 1L, -16L)) ,null));
            lineDtoList.add(new UpdateLineDto("New \f Content 6.", null, null ,null));
            return lineDtoList.stream();
        }

        @ParameterizedTest
        @DirtiesContext
        @DisplayName("works correctly")
        @MethodSource("updateLineDtoValidProvider")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
        void updateLineIsOk(UpdateLineDto input) throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

            if (input.getContent() != null) {
                assertEquals(input.getContent(), objectMapper.readValue(body, LineDto.class).getContent());
            }
            if (input.getActive() != null) {
                assertEquals(input.getActive(), objectMapper.readValue(body, LineDto.class).isActive());
            }
            if (input.getRoleIds() != null) {
                if (input.getRoleIds().size() <= 0) {
                    assertNull(objectMapper.readValue(body, LineDto.class).getRoles());
                } else {
                    List<Long> actual = objectMapper.readValue(body, LineDto.class).getRoles().stream().map(RoleDto::getId).toList();
                    List<Long> expected = input.getRoleIds();
                    assertTrue(actual.containsAll(expected));
                }
            }
        }

        @ParameterizedTest
        @DirtiesContext
        @DisplayName("returns InternalServerError")
        @MethodSource("updateLineDtoInvalidProvider")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
        void updateLineThrowsException(UpdateLineDto input) throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @DisplayName("returns Forbidden")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user })
        void updateLineThrowsException() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UpdateLineDto(null, null, null ,null)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden());
        }
    }
}