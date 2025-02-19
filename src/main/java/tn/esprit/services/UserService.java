package tn.esprit.services;

import tn.esprit.Entity.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

                    System.out.println("📧 Un e-mail a été envoyé à " + email + " avec le nouveau mot de passe : " + newPassword);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("🚨 Erreur lors de la réinitialisation du mot de passe !");
            e.printStackTrace();
        }

        return false;
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

}
