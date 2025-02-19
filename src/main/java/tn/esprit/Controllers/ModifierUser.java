package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ModifierUser {

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField role;
    @FXML
    private TextField idUser;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Button backButton;

    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;

        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        role.setText(user.getRole());
        idUser.setText(String.valueOf(user.getId()));

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            File avatarFile = new File(user.getAvatar());
            if (avatarFile.exists()) {
                avatarImageView.setImage(new Image(avatarFile.toURI().toString()));
            }
        }
    }

    private boolean isPasswordStrong(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return password.matches(passwordPattern);
    }

    @FXML
    public void Update(ActionEvent actionEvent) {
        String firstnameText = firstname.getText();
        String lastnameText = lastname.getText();
        String emailText = email.getText();
        String passwordText = password.getText();
        String roleText = role.getText();

        if (firstnameText.isEmpty() || lastnameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || roleText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Tous les champs doivent être remplis");
            alert.showAndWait();
            return;
        }

        if (!emailText.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("L'email n'est pas valide");
            alert.showAndWait();
            return;
        }

        if (!(roleText.equals("ADMIN") || roleText.equals("CLIENT") || roleText.equals("PARTNER"))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Le rôle doit être ADMIN, CLIENT ou PARTNER");
            alert.showAndWait();
            return;
        }

        if (!isPasswordStrong(passwordText)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial");
            alert.showAndWait();
            return;
        }

        if (currentUser != null) {
            currentUser.setFirstname(firstnameText);
            currentUser.setLastname(lastnameText);
            currentUser.setEmail(emailText);
            currentUser.setPassword(passwordText);
            currentUser.setRole(roleText);

            UserService userService = new UserService();
            userService.updateUser(currentUser);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText("Utilisateur mis à jour avec succès");
            successAlert.showAndWait();

            firstname.clear();
            lastname.clear();
            email.clear();
            password.clear();
            role.clear();
            idUser.clear();
        }
    }

    @FXML
    public void handleAvatarUpload(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                currentUser.setAvatar(selectedFile.getAbsolutePath());

                avatarImageView.setImage(new Image(new FileInputStream(selectedFile)));

                System.out.println("✅ Avatar mis à jour avec succès !");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'upload de l'avatar !");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Problème de navigation");
            errorAlert.setContentText("Une erreur est survenue lors du changement de scène.");
            errorAlert.showAndWait();
        }
    }
}
