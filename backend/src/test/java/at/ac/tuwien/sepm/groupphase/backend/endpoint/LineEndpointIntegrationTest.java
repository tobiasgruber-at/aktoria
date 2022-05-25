package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            lineDtoList.add(new UpdateLineDto(null, null));
            lineDtoList.add(new UpdateLineDto("New Content.", null));
            lineDtoList.add(new UpdateLineDto("New Content.", true));
            return lineDtoList.stream();
        }

        private static Stream<UpdateLineDto> updateLineDtoInvalidProvider() {
            final List<UpdateLineDto> lineDtoList = new ArrayList<>();
            lineDtoList.add(new UpdateLineDto("new Content.", null));
            lineDtoList.add(new UpdateLineDto("New Content", null));
            lineDtoList.add(new UpdateLineDto("New \n Content.", null));
            lineDtoList.add(new UpdateLineDto("New \r Content.", null));
            lineDtoList.add(new UpdateLineDto("New \t Content.", null));
            lineDtoList.add(new UpdateLineDto("New \f Content.", null));
            return lineDtoList.stream();
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("works correctly")
        @MethodSource("updateLineDtoValidProvider")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.verified })
        void updateLineIsOk(UpdateLineDto input) throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/scripts/1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

            if (input.getContent() != null) {
                assertEquals(input.getContent(), objectMapper.readValue(body, LineDto.class).getContent());
            }
            if (input.getIsInactive() != null) {
                assertEquals(input.getIsInactive(), objectMapper.readValue(body, LineDto.class).isActive());
            }
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("returns InternalServerError")
        @MethodSource("updateLineDtoInvalidProvider")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.verified })
        void updateLineThrowsException(UpdateLineDto input) throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/scripts/1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional
        @DisplayName("returns Forbidden")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.user })
        void updateLineThrowsException() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/scripts/1/lines/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UpdateLineDto(null, null)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("deleteLine()")
    class DeleteLine {
        @Test
        @Transactional
        @DisplayName("works correctly")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.verified })
        void deleteLine() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/scripts/1/lines/2")
                ).andExpect(status().isNoContent());

            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/scripts/1/lines/2")
                ).andExpect(status().isNotFound());
        }

        @Test
        @Transactional
        @DisplayName("returns Forbidden")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.user })
        void deleteLineThrowsException() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/scripts/1/lines/2")
                ).andExpect(status().isForbidden());
        }
    }
}