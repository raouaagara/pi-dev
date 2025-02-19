package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.util.Date;

public class SignUp {

    @FXML
    private TextField firstname_signin;
    @FXML
    private TextField lastname_signin;
    @FXML
    private TextField email_signin;
    @FXML
    private PasswordField password_signin;
    @FXML
    private TextField role_signin;
    @FXML
    private Button exit;
    @FXML
    private Hyperlink login_acc;

    private UserService userService = new UserService();

    @FXML
    private void signup(ActionEvent event) {
        String firstname = firstname_signin.getText();
        String lastname = lastname_signin.getText();
        String email = email_signin.getText();
        String password = password_signin.getText();
        String role = role_signin.getText();

        // Avatar par défaut si l'utilisateur n'en fournit pas
        String avatar = "default_avatar.png";

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide.");
            return;
        }

        if (!isStrongPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit comporter au moins 8 caractères, une majuscule, un chiffre et un symbole.");
            return;
        }

        if (!role.equals("ADMIN") && !role.equals("CLIENT") && !role.equals("PARTNER")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle doit être 'ADMIN', 'CLIENT' ou 'PARTNER'.");
            return;
        }

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setAvatar(avatar);  // Ajout de l'avatar
        user.setJoinDate(new Date());

        userService.addUser(user);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur inscrit avec succès !");
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) login_acc.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page !");
        }
    }

    public void login_acc(ActionEvent actionEvent) {
        loadPage("/SignIn.fxml", "Créer un compte");

    }
}
