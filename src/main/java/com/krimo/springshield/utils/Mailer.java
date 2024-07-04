package com.krimo.springshield.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Mailer {
    private final JavaMailSender mailSender;

    /**
     * This method creates a mail message to be sent to user's email account.
     *
     * @param to        the email address of the user
     * @param subject   the subject of email
     * @param body      the email body that contains the verification token
     */
    public void sendMaiL(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }


}