package services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import tools.MySMTP;

import java.util.Properties;

public class EmailSender {

    private MySMTP smtpConfig;

    public EmailSender() {
        this.smtpConfig = MySMTP.getInstance();
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            // Configuration des propriétés SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpConfig.getSmtpHost());
            props.put("mail.smtp.port", smtpConfig.getSmtpPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Créer une session SMTP avec authentification
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpConfig.getUsername(), smtpConfig.getPassword());
                }
            });

            // Créer un message MIME
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpConfig.getUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Envoyer l'e-mail
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}