package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ActiveProfiles({"test", "datagen"})
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
class UserEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }


    //TESTING GET
    @Test
    @Transactional
    @DisplayName("getUserSuccessful() Get a User correctly")
    void getUserSuccessful() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users/-1")
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        SimpleUserDto userResult = objectMapper.readValue(body, SimpleUserDto.class);

        assertNotNull(userResult);
        assertEquals(-1L, userResult.getId());
    }

    @Test
    @Transactional
    @DisplayName("getNonexistentUser() Get a User that does not exist")
    void getNonexistentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/users/0")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    //TESTING POST
    @Test
    @Transactional
    @DisplayName("postUserSuccessful() Post a new User correctly")
    void postUserSuccessful() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Name", "lastName", "admin@email.com", "Password")))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();
        UserRegistrationDto userResult = objectMapper.readValue(body, UserRegistrationDto.class);

        assertNotNull(userResult);
        assertEquals("Name", userResult.getFirstName());
        assertEquals("lastName", userResult.getLastName());
        assertEquals("admin@email.com", userResult.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("postUserFailedPassword() Post a new User with too short Password")
    void postUserFailedPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Name", "lastName", "admin@email.com", "Pass")))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (pw too short)
    }

    @Test
    @Transactional
    @DisplayName("postUserFailedName() Post a new User with Name == null")
    void postUserFailedName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UserRegistrationDto(null, "lastName", "admin@email.com", "Password")))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (name is null)
    }

    @Test
    @Transactional
    @DisplayName("postUserFailedName() Post a new User with invalid email")
    void postUserFailedEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Name", "lastName", "adminemailcom", "Password")))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (email format is invalid)
    }

    @Test
    @Transactional
    @DisplayName("postUserTooLongEmail() Post a new User with too long email")
    void postUserTooLongEmail() throws Exception {
        String s = "admin@email.com" + "a".repeat(101);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Name", "lastName", s, "Password")))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity()); // Unprocessable Entity due to Validation Exception (email too long)
    }

    @Test
    @Transactional
    @DisplayName("postUserTooLong() Post a new User with too long first and last name and Invalid and Too long Email and too long password")
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
    @DisplayName("postUserEdgeCase() Post a new User with values that are exactly at the limit")
    void postUserEdgeCase() throws Exception {
        String s = "a".repeat(100);
        byte[] body = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto(s, s, "admin@email.com", s)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();
        UserRegistrationDto userResult = objectMapper.readValue(body, UserRegistrationDto.class);

        assertNotNull(userResult);
        assertEquals(s, userResult.getFirstName());
        assertEquals(s, userResult.getLastName());
        assertEquals("admin@email.com", userResult.getFirstName());
    }

    @Test
    @Transactional
    @DisplayName("postUserBodyNull() Post a new User with null as body")
    void postUserBodyNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
    }


    //TESTING PATCH

    @Test
    @Transactional
    @DisplayName("patchUserAndPassword() Change an existing User and their password correctly")
    void patchUserAndPassword() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/api/v1/users/{id}?passwordChange=true", -1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, "NewName", "newWow", "admin@email.com", "oldPassword", "newPassword", true)))
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();
        DetailedUserDto userResult = objectMapper.readValue(body, DetailedUserDto.class);

        assertNotNull(userResult);
        assertEquals("NewName", userResult.getFirstName());
        assertEquals("newWow", userResult.getLastName());
        assertEquals("admin@email.com", userResult.getEmail());
        assertEquals("newPassword", userResult.getPassword());
        assertEquals(false, userResult.getVerified());
    }

    @Test
    @Transactional
    @DisplayName("patchUserBodyNull() Update an existing User with null as body")
    void patchUserBodyNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/-1?passwordChange=true")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()); //Spring automatically throws 400 Bad Request when Request Body is empty
    }

    @Test
    @Transactional
    @DisplayName("patchNonexistentUser() Update a User that does not exist")
    void patchNonexistentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/0?passwordChange=true")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, "NewName", "newWow", "admin@email.com", "oldPassword", "newPassword", true)))
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("patchUserInvalidEmail() Change an existing User with invalid email")
    void patchUserInvalidEmail() throws Exception {
        String s = "a".repeat(101);
        mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/v1/users/{id}?passwordChange=true", -1L)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, "NewName", "newLastName", s, "PASSWORD", "PASSWORD", true)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @DisplayName("patchUserInvalidName() Change an existing User with invalid name")
    void patchUserInvalidName() throws Exception {
        String s = "a".repeat(101);
        mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/v1/users/{id}?passwordChange=true", -1L)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, s, s, "admin@email.com", "PASSWORD", "", true)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @DisplayName("patchUserEdgeCase() Change an existing User with values that are at the limit")
    void patchUserEdgeCase() throws Exception {
        String s = "a".repeat(100);
        byte[] body = mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/users/{id}?passwordChange=false", -1L)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, s, s, "admin@email.com", "", "", true)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();
        DetailedUserDto userResult = objectMapper.readValue(body, DetailedUserDto.class);

        assertNotNull(userResult);
        assertEquals(s, userResult.getFirstName());
        assertEquals(s, userResult.getLastName());
        assertEquals("admin@email.com", userResult.getEmail());
        assertEquals(false, userResult.getVerified());
    }

    @Test
    @Transactional
    @DisplayName("patchUserPasswordEdgeCase() Change an existing User's password that is at the limit")
    void patchUserPasswordEdgeCase() throws Exception {
        String s = "a".repeat(100);
        mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/v1/users/{id}?passwordChange=true", -1L)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UpdateUserDto(-1L, "NewName", "newLastName", "admin@email.com", "Password", s, true)))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

    //TESTING DELETE
    @Test
    @Transactional
    @DisplayName("deleteUser() Delete a User correctly")
    void deleteUserSuccessful() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/v1/users/-1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNoContent());
    }


    @Test
    @Transactional
    @DisplayName("deleteNonexistentUser() Delete a User with that does not exist")
    void deleteNonexistentUser() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .delete("/api/v1/users/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
    }


    //TESTING FORGOTTEN PASSWORD

    @Test
    @Transactional
    @DisplayName("forgottenPasswordSuccessful() successfully change a password")
    void forgottenPasswordSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/forgotten-password")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes("admin@email.com"))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @DisplayName("forgottenPasswordInvalidEmail() try to change a password with an invalid email")
    void forgottenPasswordInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/forgotten-password")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes("invalid"))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @DisplayName("forgottenPasswordTooLongEmail() try to change a password with a too long email")
    void forgottenPasswordTooLongEmail() throws Exception {
        String s = "admin@email.com" + "s".repeat(100);
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/forgotten-password")
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(s))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

}