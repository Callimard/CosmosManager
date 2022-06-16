package org.bandrsoftwares.cosmosmanager.backend.service.mail;

public interface EmailSender {

    void sendEmail(String subject, String text, String receiverEmail);
}
