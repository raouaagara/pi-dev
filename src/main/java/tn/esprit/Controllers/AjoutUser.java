package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.Entity.User;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AjoutUser {
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
    private Button ajout;
    @FXML
    private Button uploadAvatarButton;
    @FXML
    private ImageView avatarImageView;

    @FXML
    private Label firstnameError;
    @FXML
    private Label lastnameError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label roleError;

    private UserService userService = new UserService();
    private File avatarFile;


    @FXML
    private Button backButton;


    @FXML
    public void ajouter(ActionEvent event) {
        firstnameError.setText("");
        lastnameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        roleError.setText("");

        String firstname = this.firstname.getText();
        String lastname = this.lastname.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String role = this.role.getText();

        if (firstname.isEmpty()) {
            firstnameError.setText("❌ Veuillez saisir un prénom.");
            return;
        }
        if (lastname.isEmpty()) {
            lastnameError.setText("❌ Veuillez saisir un nom.");
            return;
        }
        if (email.isEmpty()) {
            emailError.setText("❌ Veuillez saisir un email.");
            return;
        }
        if (password.isEmpty()) {
            passwordError.setText("❌ Veuillez saisir un mot de passe.");
            return;
        }
        if (role.isEmpty()) {
            roleError.setText("❌ Veuillez saisir un rôle.");
            return;
        }

        if (!isValidEmail(email)) {
            emailError.setText("❌ L'email n'est pas valide.");
            return;
        }

        if (!isStrongPassword(password)) {
            passwordError.setText("❌ Le mot de passe doit comporter au moins 8 caractères, une majuscule, un chiffre et un symbole.");
            return;
        }

        if (!role.equals("ADMIN") && !role.equals("CLIENT") && !role.equals("PARTNER")) {
            roleError.setText("❌ Le rôle doit être 'ADMIN', 'CLIENT' ou 'PARTNER'.");
            return;
        }

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setJoinDate(new Date());

        if (avatarFile != null) {
            user.setAvatar(avatarFile.getAbsolutePath());
        } else {
            user.setAvatar("");
        }

        userService.addUser(user);
        System.out.println("✅ L'utilisateur a été ajouté avec succès!");

        Alert successAlert = new Alert(AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Utilisateur ajouté avec succès!");
        successAlert.showAndWait();

        this.firstname.clear();
        this.lastname.clear();
        this.email.clear();
        this.password.clear();
        this.role.clear();
        this.avatarImageView.setImage(null);
    }

    @FXML
    public void handleAvatarUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            avatarFile = selectedFile;
            Image avatarImage = new Image(selectedFile.toURI().toString());
            avatarImageView.setImage(avatarImage);
        }
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

    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Problème de navigation");
            errorAlert.setContentText("Une erreur est survenue lors du changement de scène.");
            errorAlert.showAndWait();
        }
    }
}
