package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing user-registration.
 *
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles({"test", "datagen"})
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
@WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
public class UserRegisterIntegrationTest {

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

    @Test
    @DirtiesContext
    @DisplayName("registration works correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    void register() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Varok", "Saurfang", "varok.saurfang@email.com", "forthehorde")))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();

        SimpleUserDto response = objectMapper.readValue(body, SimpleUserDto.class);
        final SimpleUserDto expected = new SimpleUserDto(response.getId(), "Varok", "Saurfang", "varok.saurfang@email.com", false);

        assertNotNull(response);
        assertEquals(response, expected);

        body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users?email=" + expected.getEmail())
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        response = objectMapper.readValue(body, SimpleUserDto.class);
        assertNotNull(response);
        assertEquals(response, expected);
    }
}
