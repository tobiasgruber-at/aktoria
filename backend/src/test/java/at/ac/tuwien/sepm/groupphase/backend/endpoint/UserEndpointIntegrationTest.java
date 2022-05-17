package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
class UserEndpointIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("tester", "password"))
        .withPerMethodLifecycle(true);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    //TESTING POST
    @Nested
    @DisplayName("postUser()")
    class PostUser {
        private static Stream<UserRegistrationDto> userRegistrationDtoNullProvider() {
            final List<UserRegistrationDto> temp = new LinkedList<>();
            temp.add(new UserRegistrationDto(null, "lastName", "admin@email.com", "Password"));
            temp.add(new UserRegistrationDto("firstName", null, "admin@email.com", "Password"));
            temp.add(new UserRegistrationDto("firstName", "lastName", null, "Password"));
            temp.add(new UserRegistrationDto("firstName", "lastName", "admin@email.com", null));
            return temp.stream();
        }

        private static Stream<UserRegistrationDto> userRegistrationDtoProvider() {
            final List<UserRegistrationDto> temp = new LinkedList<>();
            temp.add(new UserRegistrationDto("Name", "lastName", "adminemailcom", "Password"));
            temp.add(new UserRegistrationDto("Name", "lastName", "", "Password"));
            temp.add(new UserRegistrationDto("Name", "lastName", "   ", "Password"));
            temp.add(new UserRegistrationDto("Name", "lastName", "admin@email.com" + "a".repeat(101), "Password"));
            return temp.stream();
        }

        @Test
        @Transactional
        @DisplayName("post a user correctly")
        void postUserSuccessful() throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .post("/api/v1/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("firstName", "lastName", "email@email.com", "Password")))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
            SimpleUserDto userResult = objectMapper.readValue(body, SimpleUserDto.class);
            SimpleUserDto expected = new SimpleUserDto(userResult.getId(), "firstName", "lastName", "email@email.com", false);

            assertNotNull(userResult);
            assertEquals(expected, userResult);
        }

        @Test
        @Transactional
        @DisplayName("returns UnprocessableEntity for too short password")
        void postUserFailedPassword() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Name", "lastName", "admin@email.com", "Pass")))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (pw too short)
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("returns UnprocessableEntity for null values")
        @MethodSource("userRegistrationDtoNullProvider")
        void postUserFailedName(UserRegistrationDto input) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("returns UnprocessableEntity for invalid email")
        @MethodSource("userRegistrationDtoProvider")
        void postUserFailedEmail(UserRegistrationDto input) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional
        @DisplayName("return UnprocessableEntity for total invalid values")
        void postUserTooLong() throws Exception {
            String s = "a".repeat(101);
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto(s, s, s, s)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (first and last name too long, email invalid, email too long, password too long)
        }

        @Test
        @Transactional
        @DisplayName("posts user with edge values correctly")
        void postUserEdgeCase() throws Exception {
            String s1 = "a".repeat(100);
            String s2 = "e".repeat(90) + "@email.com";
            byte[] body = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/v1/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserRegistrationDto(s1, s1, s2, s1)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
            UserRegistrationDto userResult = objectMapper.readValue(body, UserRegistrationDto.class);

            assertNotNull(userResult);
            assertEquals(s1, userResult.getFirstName());
            assertEquals(s1, userResult.getLastName());
            assertEquals(s2, userResult.getEmail());
        }

        @Test
        @Transactional
        @DisplayName("return BadRequest for empty body")
        void postUserBodyNull() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
        }
    }

    //TESTING GET
    @Disabled
    @Nested
    @DisplayName("getUser()")
    class GetUser {
        @Test
        @Transactional
        @DisplayName("gets a user correctly")
        void getUserSuccessful() throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .get("/api/v1/users/-1")
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

            SimpleUserDto userResult = objectMapper.readValue(body, SimpleUserDto.class);

            assertNotNull(userResult);
            assertEquals(-1L, userResult.getId());
            // TODO: add missing assertions
        }

        @Test
        @Transactional
        @DisplayName("returns NotFound on non existing user")
        void getNonexistentUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/0")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        }
    }

    //TESTING PATCH
    @Nested
    @DisplayName("patchUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    class PatchUser {
        private static Stream<UpdateUserDto> updateUserDtoProvider() {
            final List<UpdateUserDto> temp = new LinkedList<>();
            temp.add(new UpdateUserDto(-1L, "NewName", "newLastName", "a".repeat(101) + "@mail.com", "PASSWORD", "PASSWORD", true));
            temp.add(new UpdateUserDto(-1L, "a".repeat(101), "a".repeat(101), "admin@email.com", "PASSWORD", "", true));
            temp.add(new UpdateUserDto(-1L, "", "", "admin@email.com", "PASSWORD", "", true));
            temp.add(new UpdateUserDto(-1L, "   ", "   ", "admin@email.com", "PASSWORD", "", true));
            return temp.stream();
        }

        @Disabled
        @Test
        @Transactional
        @DisplayName("changes user and password correctly")
        void patchUserAndPassword() throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/users/{id}", -1L)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, "NewFirstName", "newLastName", "new@email.com", "oldPassword", "newPassword", true)))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            DetailedUserDto userResult = objectMapper.readValue(body, DetailedUserDto.class);

            assertNotNull(userResult);
            assertEquals("NewFirstName", userResult.getFirstName());
            assertEquals("newLastName", userResult.getLastName());
            assertEquals("new@email.com", userResult.getEmail());
            assertEquals("newPassword", userResult.getPasswordHash());
            assertEquals(true, userResult.getVerified());
        }

        @Test
        @Transactional
        @DisplayName("returns BadRequest on empty body")
        void patchUserBodyNull() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/-1")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
        }

        @Disabled
        @Test
        @Transactional
        @DisplayName("returns NotFound on non existing user")
        void patchNonexistentUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UpdateUserDto(0L, "NewName", "newWow", "admin@email.com", "oldPassword", "newPassword", true)))
            ).andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("returns UnprocessableEntity")
        @MethodSource("updateUserDtoProvider")
        void patchUserInvalidEmail(UpdateUserDto input) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/{id}", -1L)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Disabled
        @Test
        @Transactional
        @DisplayName("changes user and password correctly for edge cases")
        void patchUserEdgeCase() throws Exception {
            String name = "a".repeat(100);
            byte[] body = mockMvc.perform(MockMvcRequestBuilders
                    .patch("/api/v1/users/{id}", -1L)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, name, name, "admin@email.com", null, null, true)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            DetailedUserDto userResult = objectMapper.readValue(body, DetailedUserDto.class);

            assertNotNull(userResult);
            assertEquals(name, userResult.getFirstName());
            assertEquals(name, userResult.getLastName());
            assertEquals("admin@email.com", userResult.getEmail());
            assertEquals(true, userResult.getVerified());
        }
    }


    //TESTING DELETE
    @Nested
    @DisplayName("deleteUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
    class DeleteUser {

        @Disabled
        @Test
        @Transactional
        @DisplayName("deletes a user correctly")
        void deleteUserSuccessful() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/users/-1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent());

            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/users/-1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound());
        }

        @Test
        @Transactional
        @DisplayName("returns NotFound on non existing user")
        void deleteNonexistentUser() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/users/0")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound());
        }
    }

    //TESTING FORGOTTEN PASSWORD
    @Nested
    @DisplayName("forgottenPassword()")
    class ForgottenPassword {
        private static Stream<String> forgottenPasswordInvalidEmailProvider() {
            final List<String> temp = new LinkedList<>();
            temp.add("invalid");
            temp.add("s".repeat(100) + "admin@email.com");
            return temp.stream();
        }

        @Disabled
        @Test
        @Transactional
        @DisplayName("changes password correctly")
        void forgottenPasswordSuccessful() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users/reset-password")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes("admin@email.com"))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isAccepted());
        }

        @Disabled
        @ParameterizedTest
        @Transactional
        @DisplayName("returns UnprocessableEntity")
        @MethodSource("forgottenPasswordInvalidEmailProvider")
        void forgottenPasswordInvalidEmail(String input) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users/reset-password")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }
    }
}