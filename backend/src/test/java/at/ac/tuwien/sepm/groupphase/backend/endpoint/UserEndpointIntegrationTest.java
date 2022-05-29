package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test", "datagen"})
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
    PasswordEncoder passwordEncoder;
    @Autowired
    SecureTokenService secureTokenService;
    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDataGenerator userDataGenerator;
    private MockMvc mockMvc;
    private List<User> userList;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).apply(springSecurity()).build();
        int count = 0;
        while (userList == null || userList.isEmpty()) {
            this.userList = userRepository.findAll();
            count++;
            if (count > 500) {
                userDataGenerator.generateUser();
            }
        }
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
        @DirtiesContext
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
        @DirtiesContext
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
        @DirtiesContext
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
        @DirtiesContext
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
        @DirtiesContext
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
        @DirtiesContext
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
        @DirtiesContext
        @DisplayName("return BadRequest for empty body")
        void postUserBodyNull() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
        }
    }

    //TESTING GET
    @Nested
    @DisplayName("getUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.user, Role.verified, Role.admin})
    class GetUser {
        @Test
        @DirtiesContext

        @DisplayName("gets a user correctly")
        void getUserSuccessful() throws Exception {
            User input = userList.get(0);

            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .get("/api/v1/users?email=" + input.getEmail())
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

            SimpleUserDto actual = objectMapper.readValue(body, SimpleUserDto.class);
            SimpleUserDto expected = userMapper.userToSimpleUserDto(input);

            assertNotNull(actual);
            assertEquals(expected, actual);
        }

        @Test
        @DirtiesContext
        @DisplayName("returns NotFound on non existing user")
        void getNonexistentUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users?email=someobscureemailthatwillneverbeused@email.com")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        }
    }

    //TESTING PATCH
    @Nested
    @DisplayName("patchUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class PatchUser {
        private static Stream<UpdateUserDto> invalidUpdateUserDtoProvider() {
            final List<UpdateUserDto> temp = new LinkedList<>();
            temp.add(new UpdateUserDto(1L, "NewName", "newLastName", "a".repeat(101) + "@mail.com", "PASSWORD", "PASSWORD", true));
            temp.add(new UpdateUserDto(1L, "a".repeat(101), "a".repeat(101), "admin@email.com", "PASSWORD", "", true));
            temp.add(new UpdateUserDto(1L, "", "", "admin@email.com", "PASSWORD", "", true));
            temp.add(new UpdateUserDto(1L, "   ", "   ", "admin@email.com", "PASSWORD", "", true));
            return temp.stream();
        }

        @Test
        @DirtiesContext
        @DisplayName("changes user and password correctly")
        void patchUserAndPassword() throws Exception {
            User u = userList.get(0);
            if (u.getId() > 20L) {
                u.setPasswordHash(passwordEncoder.encode("password"));
                userRepository.save(u);
            }
            String password = u.getId() > 20L ? "password" : UserDataGenerator.TEST_USER_PASSWORD + u.getId();
            UpdateUserDto input = new UpdateUserDto(
                u.getId(),
                "NewName",
                "newLastName",
                "a".repeat(50) + "@mail.com",
                password,
                "PASSWORD",
                false
            );

            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .patch("/api/v1/users/" + input.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            DetailedUserDto actual = objectMapper.readValue(body, DetailedUserDto.class);
            DetailedUserDto expected = userMapper.userToDetailedUserDto(User.builder()
                .id(u.getId())
                .firstName("NewName")
                .lastName("newLastName")
                .email("a".repeat(50) + "@mail.com")
                .passwordHash(actual.getPasswordHash())
                .verified(false)
                .build()
            );

            assertNotNull(actual);
            assertEquals(expected, actual);
        }

        @Test
        @DirtiesContext
        @DisplayName("returns BadRequest on empty body")
        void patchUserBodyNull() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/-1")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
        }

        @Test
        @DirtiesContext
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
        @DirtiesContext
        @DisplayName("returns UnprocessableEntity")
        @MethodSource("invalidUpdateUserDtoProvider")
        void patchUserInvalidEmail(UpdateUserDto input) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/{id}", -1L)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @DisplayName("changes user and password correctly for edge cases")
        void patchUserEdgeCase() throws Exception {
            User u = userList.get(0);
            UpdateUserDto input = new UpdateUserDto(
                u.getId(),
                "a".repeat(100),
                "a".repeat(100),
                "a".repeat(91) + "@mail.com",
                null,
                null,
                false
            );

            byte[] body = mockMvc.perform(MockMvcRequestBuilders
                    .patch("/api/v1/users/" + input.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(input))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            DetailedUserDto actual = objectMapper.readValue(body, DetailedUserDto.class);
            DetailedUserDto expected = userMapper.userToDetailedUserDto(User.builder()
                .id(u.getId())
                .firstName("a".repeat(100))
                .lastName("a".repeat(100))
                .email("a".repeat(91) + "@mail.com")
                .passwordHash(actual.getPasswordHash())
                .verified(false)
                .build()
            );

            assertNotNull(actual);
            assertEquals(expected, actual);
        }
    }


    //TESTING DELETE
    @Nested
    @DisplayName("deleteUser()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    class DeleteUser {

        @Disabled
        @Test
        @DirtiesContext
        @DisplayName("deletes a user correctly")
        void deleteUserSuccessful() throws Exception {
            User u = userList.get(0);
            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/users/" + u.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent());

            mockMvc
                .perform(MockMvcRequestBuilders
                    .delete("/api/v1/users/" + u.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound());
        }

        @Test
        @DirtiesContext
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
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user})
    class ForgottenPassword {
        private static Stream<PasswordChangeDto> forgottenPasswordInvalidProvider() {
            final List<PasswordChangeDto> temp = new LinkedList<>();
            temp.add(new PasswordChangeDto(null, null, "invalid"));
            temp.add(new PasswordChangeDto(null, UserTestHelper.dummyUserEmail, "invalid"));
            return temp.stream();
        }

        private static Stream<PasswordChangeDto> forgottenPasswordValidProvider() {
            final List<PasswordChangeDto> temp = new LinkedList<>();
            temp.add(new PasswordChangeDto(null, UserTestHelper.dummyUserEmail, "validPassword"));
            return temp.stream();
        }

        @ParameterizedTest
        @DirtiesContext
        @DisplayName("returns UnprocessableEntity")
        @MethodSource("forgottenPasswordInvalidProvider")
        void forgottenPasswordInvalidEmail(PasswordChangeDto input) throws Exception {
            SecureToken secureToken = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD, 15);
            secureToken.setAccount(userList.get(0));
            secureTokenService.saveSecureToken(secureToken);
            input.setToken(secureToken.getToken());
            mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/users/reset-password")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
            secureTokenService.removeToken(secureToken.getToken());
        }

        @ParameterizedTest
        @DirtiesContext
        @DisplayName("changes password correctly")
        @MethodSource("forgottenPasswordValidProvider")
        void forgottenPasswordSuccessful(PasswordChangeDto input) throws Exception {
            SecureToken secureToken = secureTokenService.createSecureToken(TokenType.RESET_PASSWORD, 15);
            secureToken.setAccount(userList.get(0));
            secureTokenService.saveSecureToken(secureToken);
            input.setToken(secureToken.getToken());
            mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/users/reset-password")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isAccepted());
            secureTokenService.removeToken(secureToken.getToken());
        }
    }
}