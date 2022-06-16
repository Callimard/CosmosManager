package org.bandrsoftwares.cosmosmanager.backend.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bandrsoftwares.cosmosmanager.backend.UserManagementConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailSenderImpl implements EmailSender {

    // Variables.

    private final JavaMailSender emailSender;

    private final UserManagementConfiguration userManagementConfiguration;

    // Methods.

    @Override
    public void sendEmail(String subject, String text, String receiverEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(userManagementConfiguration.getEmailSendFrom());
        message.setTo(receiverEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
