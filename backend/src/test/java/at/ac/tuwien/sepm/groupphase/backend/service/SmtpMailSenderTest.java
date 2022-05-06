package at.ac.tuwien.sepm.groupphase.backend.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SmtpMailSenderTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("tester","password"))
        .withPerMethodLifecycle(false);

    @Autowired
    private SmtpMailSender mailSender;

    @Test
    void sendMail() {
        try {
            mailSender.sendMail("test@email.com", "some subject", "some content");

            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertEquals(1, receivedMessages.length);

            MimeMessage message = receivedMessages[0];
            assertEquals("some content", GreenMailUtil.getBody(message));
            assertEquals("test@email.com", message.getAllRecipients()[0].toString());
        } catch (MessagingException e){
            fail(e);
        }
    }
}