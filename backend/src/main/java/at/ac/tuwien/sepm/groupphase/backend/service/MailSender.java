package at.ac.tuwien.sepm.groupphase.backend.service;

import javax.mail.MessagingException;

public interface MailSender {

    /**
     * Sends an email from aktoria.norepl@gmx.at
     *
     * @param receiver the email address of the receiver
     * @param subject  subject of the mail
     * @param content  content of the mail in html format
     * @throws MessagingException if there occurred an error during transport
     */
    void sendMail(String receiver, String subject, String content) throws MessagingException;
}
