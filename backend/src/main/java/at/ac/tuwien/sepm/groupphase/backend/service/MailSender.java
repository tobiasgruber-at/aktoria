package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.exception.UnprocessableEmailException;

/**
 * Interface for sending mails.
 *
 * @author Nikolaus Peter
 */
public interface MailSender {

    /**
     * Sends an email from aktoria.norepl@gmx.at
     *
     * @param receiver the email address of the receiver
     * @param subject  subject of the mail
     * @param content  content of the mail in html format
     * @throws UnprocessableEmailException if there occurred an error during transport
     */
    void sendMail(String receiver, String subject, String content);
}
