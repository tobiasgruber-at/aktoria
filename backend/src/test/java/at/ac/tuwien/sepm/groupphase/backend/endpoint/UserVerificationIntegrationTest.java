package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
@WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { "USER", "VERIFIED", "ADMIN" })
public class UserVerificationIntegrationTest {
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
    @Transactional
    @DisplayName("verification works correctly")
    void verify() throws Exception {
        //register new user
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserRegistrationDto("Varok", "Saurfang", "varok.saurfang@email.com", "forthehorde")))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();

        SimpleUserDto response = objectMapper.readValue(body, SimpleUserDto.class);
        final SimpleUserDto expectedUnverfied = new SimpleUserDto(response.getId(), "Varok", "Saurfang", "varok.saurfang@email.com", false);

        assertNotNull(response);
        assertEquals(response, expectedUnverfied);

        //assert that new user is saved with verified false

        body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users?email=" + expectedUnverfied.getEmail())
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        response = objectMapper.readValue(body, SimpleUserDto.class);
        assertNotNull(response);
        assertEquals(response, expectedUnverfied);

        //check email inbox for token
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);

        final MimeMessage message = receivedMessages[0];
        assertEquals("varok.saurfang@email.com", message.getAllRecipients()[0].toString());

        String token = GreenMailUtil.getBody(message).split("verifyEmail/")[1].split("'>Em=")[0];

        //send token for verification
        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/users/verification")
                .content(token)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        //assert that user is verified
        final SimpleUserDto expectedVerfied = new SimpleUserDto(response.getId(), "Varok", "Saurfang", "varok.saurfang@email.com", true);

        body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users?email=" + expectedUnverfied.getEmail())
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        response = objectMapper.readValue(body, SimpleUserDto.class);
        assertNotNull(response);
        assertEquals(response, expectedVerfied);
    }
}
