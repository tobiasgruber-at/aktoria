package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SmtpMailSender {
    /**
     * Sends an email from aktoria.norepl@gmx.at
     * @param to the email adress of the receive
     * @param subject subject of the mail
     * @param content content of the mail
     * @throws MessagingException
     */
    public void sendMail(String to, String subject, String content) throws MessagingException
    {
        String sender = "aktoria.norepl@gmx.at";
        String password = "RMjj3XJqM5@b45Ba";
        String receiver = to;

        Properties properties = new Properties();

        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "mail.gmx.net");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.user", sender);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.user"),
                    properties.getProperty("mail.smtp.password"));
            }
        });

        Message message = new MimeMessage(mailSession);
        InternetAddress addressTo = new InternetAddress(receiver);
        message.setRecipient(Message.RecipientType.TO, addressTo);
        message.setFrom(new InternetAddress(sender));
        message.setSubject(subject);
        message.setContent(content, "text/plain");
        Transport.send(message);
    }
}
