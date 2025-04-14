package tn.esprit.services;

import tn.esprit.Entity.User;
import tn.esprit.utils.MyDatabase;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class UserService {

    private Connection con;

    public UserService() {
        this.con = MyDatabase.getInstance().getCon();
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO user (firstname, lastname, email, password, joinDate, avatar, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setDate(5, new Date(user.getJoinDate().getTime()));
            stmt.setString(6, user.getAvatar());
            stmt.setString(7, String.valueOf(user.getRole()));

            stmt.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de l'ajout de l'utilisateur !");
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("joinDate"),
                        rs.getString("avatar"),
                        rs.getString("role")
                );
                users.add(user);


            }
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la récupération des utilisateurs !");
            e.printStackTrace();
        }

        return users;
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la suppression de l'utilisateur !");
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE user SET firstname=?, lastname=?, email=?, password=?, avatar=?, role=? WHERE id=?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getAvatar());
            stmt.setString(6, user.getRole());
            stmt.setInt(7, user.getId());

            stmt.executeUpdate();
            System.out.println("✅ Utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la mise à jour de l'utilisateur !");
            e.printStackTrace();
        }
    }


    public User authenticateUser(String email, String password) {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("joinDate"),
                        rs.getString("avatar"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("🚨 Erreur d'authentification !");
            e.printStackTrace();
        }

        return null;
    }

    public boolean resetPassword(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String newPassword = generateTemporaryPassword();

                String updateSql = "UPDATE user SET password = ? WHERE email = ?";
                try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, email);
                    updateStmt.executeUpdate();
                }

                sendEmail(email, "Réinitialisation de votre mot de passe",
                        "Votre nouveau mot de passe est : " + newPassword);

                System.out.println("📧 Un e-mail a été envoyé à " + email + " avec le nouveau mot de passe.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la réinitialisation du mot de passe !");
            e.printStackTrace();
        }
        return false;
    }




    private void sendEmail(String recipient, String subject, String newPassword) {
        final String username = "amelacho3@gmail.com";
        final String password = "oyrvejtfihnzdqsa";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Contenu HTML de l'email
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    ".container { background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: auto; }" +
                    "h2 { color: #333; }" +
                    "p { font-size: 16px; color: #555; }" +
                    ".password { font-weight: bold; color: #d9534f; font-size: 18px; }" +
                    ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h2>🔐 Réinitialisation de votre mot de passe</h2>" +
                    "<p>Bonjour,</p>" +
                    "<p>Vous avez demandé la réinitialisation de votre mot de passe. Voici votre nouveau mot de passe temporaire :</p>" +
                    "<p class='password'>" + newPassword + "</p>" +
                    "<p>Nous vous recommandons de changer ce mot de passe dès que possible via les paramètres de votre compte.</p>" +
                    "<p>Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet e-mail ou contacter notre support.</p>" +
                    "<p class='footer'>Merci,<br>📧 L'équipe de support</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("✅ Email envoyé avec succès à " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("🚨 Erreur lors de l'envoi de l'email !");
        }
    }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 8 caractères
            password.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return password.toString();
    }



    public Map<String, Integer> getUserStatisticsByRole() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM user GROUP BY role";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.put(rs.getString("role"), rs.getInt("count"));
            }

        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la récupération des statistiques !");
            e.printStackTrace();
        }

        return stats;
    }


}