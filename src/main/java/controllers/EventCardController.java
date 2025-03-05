package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import models.Events;
import models.Users;
import services.ServicesParticipations;
import services.UserSession; // Utilisation de UserSession au lieu de Session
import services.EmailSender;

import java.sql.SQLException;

public class EventCardController {
    @FXML
    private VBox eventCard;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button registerButton;

    @FXML
    private Button claimButton;

    public void setEventData(Events event, Users user) {
        // Mettre à jour les labels avec les données de l'événement
        titleLabel.setText(event.getTitle());
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText("Lieu : " + event.getLocation());
        startDateLabel.setText("Début : " + event.getStartDate());
        endDateLabel.setText("Fin : " + event.getEndDate());

        // Charger l'image de l'événement
        try {
            String imageName = event.getImage();
            String imagePath = "/images/" + imageName;
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageView.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Erreur : Image non trouvée ou chemin invalide : " + event.getImage());
        }

        // Configurer les actions des boutons
        registerButton.setOnAction(e -> handleRegister(event));
        claimButton.setOnAction(e -> handleClaim(event));
    }

    private void handleRegister(Events event) {
        // Récupérer l'utilisateur actuel via UserSession
        Users currentUser = UserSession.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur ou l'événement est null
        if (currentUser == null || event == null) {
            showError("Erreur : Utilisateur ou événement non défini.");
            return;
        }

        // Vérifier si l'e-mail de l'utilisateur est valide
        if (currentUser.getEmail() == null || currentUser.getEmail().isEmpty()) {
            showError("Erreur : L'adresse e-mail de l'utilisateur est manquante.");
            return;
        }

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation d'inscription");
        confirmationAlert.setHeaderText("Confirmez-vous votre inscription à l'événement ?");
        confirmationAlert.setContentText("Un e-mail de confirmation vous sera envoyé à " + currentUser.getEmail());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Enregistrer la participation de l'utilisateur
                    ServicesParticipations servicesParticipations = new ServicesParticipations();

                    if (!servicesParticipations.eventExists(event.getId())) {
                        showError("Erreur : L'événement n'existe pas.");
                        return;
                    }

                    servicesParticipations.eventParticipation(currentUser.getId(), event.getId());

                    // Envoyer un e-mail de confirmation
                    EmailSender emailSender = new EmailSender();
                    String subject = "Confirmation de participation";
                    String body = "Bonjour " + currentUser.getUsername() + ",\n\n" +
                            "Votre participation à l'événement \"" + event.getTitle() + "\" a bien été enregistrée.\n\n" +
                            "Cordialement,\nL'équipe d'organisation";

                    emailSender.sendEmail(currentUser.getEmail(), subject, body);

                    // Afficher un message de succès
                    showSuccess("Inscription réussie ! Un e-mail de confirmation a été envoyé à " + currentUser.getEmail());
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Erreur lors de l'inscription à l'événement : " + e.getMessage());
                }
            } else {
                // Afficher un message si l'utilisateur annule l'inscription
                showSuccess("Inscription annulée.");
            }
        });
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

    private void handleClaim(Events event) {
        System.out.println("Réclamation pour l'événement: " + event.getTitle());
    }
}