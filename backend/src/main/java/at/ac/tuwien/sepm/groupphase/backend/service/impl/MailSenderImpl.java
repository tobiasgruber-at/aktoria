package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.exception.UnprocessableEmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
public class MailSenderImpl implements MailSender {
    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Transactional
    @Override
    public void sendMail(String receiver, String subject, String content) {
        Properties properties = new Properties();

        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.user", sender);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        };

        Session mailSession = Session.getInstance(properties, auth);

        try {
            Message message = new MimeMessage(mailSession);
            InternetAddress addressTo = new InternetAddress(receiver);
            message.setRecipient(Message.RecipientType.TO, addressTo);
            try {
                message.setFrom(new InternetAddress(sender, "Noreply Aktoria"));
            } catch (UnsupportedEncodingException e) {
                throw new MessagingException();
            }

            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new UnprocessableEmailException(e);
        }
    }
}
