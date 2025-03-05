package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Users;
import services.UserSession; // Importation mise à jour
import services.ServicesUsers;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        ServicesUsers servicesUsers = new ServicesUsers();
        Users user = servicesUsers.authenticate(email, password);

        if (user != null) {
            // Définir l'utilisateur dans la session
            UserSession session = UserSession.getInstance(); // Utilisation de UserSession
            session.setCurrentUser(user);

            // Afficher un message de succès
            showSuccess("Connexion réussie !");

            // Rediriger vers la page des événements
            redirectToEventsPage();
        } else {
            showError("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToEventsPage() {
        try {
            // Charger la page des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/frontend.fxml"));
            Parent root = loader.load();
            emailField.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la redirection.");
        }
    }
}