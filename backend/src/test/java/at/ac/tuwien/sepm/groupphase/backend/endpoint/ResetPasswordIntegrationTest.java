package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test", "datagen"})
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class ResetPasswordIntegrationTest {
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
    @DisplayName("reset password works correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.user, Role.verified, Role.admin})
    void resetPassword() throws Exception {
        //request password reset
        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/api/v1/users/forgot-password")
                .accept(MediaType.APPLICATION_JSON)
                .content("test1@test.com")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isAccepted());

        //check email inbox for token
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);

        final MimeMessage message = receivedMessages[0];
        assertEquals("test1@test.com", message.getAllRecipients()[0].toString());

        String token = GreenMailUtil.getBody(message).split("restore/")[1].substring(0, 23);
        token = token.substring(0, 19) + token.charAt(22);
        //assertEquals(null, token);

        //change password
        mockMvc
            .perform(MockMvcRequestBuilders
                .put("/api/v1/users/reset-password")
                .accept(MediaType.APPLICATION_JSON)
                .content("{"
                    + "\"token\": \"" + token + "\","
                    + "\"newPassword\": \"pass1234\""
                    + "}")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isAccepted());
    }
}