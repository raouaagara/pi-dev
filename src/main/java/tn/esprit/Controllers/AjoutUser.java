package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.Entity.User;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

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
    private ComboBox<String> roleComboBox;
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
    @FXML
    private Button backButton;

    private UserService userService = new UserService();
    private File avatarFile;

    @FXML
    public void initialize() {
        // Ajout des rôles disponibles dans le ComboBox
        roleComboBox.getItems().addAll("ADMIN", "CLIENT", "PARTNER");
    }

    @FXML
    public void ajouter(ActionEvent event) {
        // Réinitialiser les erreurs
        firstnameError.setText("");
        lastnameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        roleError.setText("");

        String firstname = this.firstname.getText();
        String lastname = this.lastname.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String role = roleComboBox.getValue();

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
        if (role == null || role.isEmpty()) {
            roleError.setText("❌ Veuillez sélectionner un rôle.");
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

        User user = new User(firstname, lastname, email, password, role, new Date(), avatarFile != null ? avatarFile.getAbsolutePath() : "");
        userService.addUser(user);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Utilisateur ajouté avec succès!");
        successAlert.showAndWait();

        this.firstname.clear();
        this.lastname.clear();
        this.email.clear();
        this.password.clear();
        roleComboBox.getSelectionModel().clearSelection();
        this.avatarImageView.setImage(null);
    }

    @FXML
    public void handleAvatarUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", ".png", ".jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            avatarFile = selectedFile;
            avatarImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du changement de scène.").showAndWait();
        }
    }



    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isStrongPassword(String password) {
        // Au moins 8 caractères, une majuscule, un chiffre et un symbole
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

}