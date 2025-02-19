package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

import java.io.IOException;

public class SignIn {

    @FXML
    private TextField email_signin;
    @FXML
    private PasswordField password_signin;
    @FXML
    private Button login_btn;
    @FXML
    private Hyperlink create_acc;
    @FXML
    private Hyperlink mdp_oub;
    @FXML
    private Button exit;

    private final UserService userService = new UserService();

    @FXML
    public void login(ActionEvent actionEvent) {
        String email = email_signin.getText();
        String password = password_signin.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un email valide !");
            return;
        }

        User user = userService.authenticateUser(email, password);
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Connexion réussie !");
            loadDashboard(user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Email ou mot de passe incorrect !");
        }
    }

    @FXML
    public void changeForm(ActionEvent actionEvent) {
        loadPage("/SignUp.fxml", "Créer un compte");
    }

    @FXML
    public void sendPaswword_btn(ActionEvent actionEvent) {
        String email = email_signin.getText();

        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un email valide !");
            return;
        }

        boolean emailSent = userService.resetPassword(email);
        if (emailSent) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Un email de récupération a été envoyé !");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Cet email n'existe pas !");
        }
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) login_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page !");
        }
    }

    private void loadDashboard(User user) {
        String role = user.getRole();

        if ("ADMIN".equalsIgnoreCase(role)) {
            loadPage("/ListeUser.fxml", "Tableau de bord Admin");
        } else if ("CLIENT".equalsIgnoreCase(role) || "PARTNER".equalsIgnoreCase(role)) {
            loadPage("/Accueil.fxml", "Page d'accueil");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle inconnu !");
        }
    }
}
