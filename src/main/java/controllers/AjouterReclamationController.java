package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import models.Event;
import models.Reclamation;
import models.User;
import services.ServiceEvent;
import services.ServiceReclamation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjouterReclamationController {

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();
    private final Map<String, Integer> eventMap = new HashMap<>(); // Map to store title -> ID

    @FXML
    private TextArea descriptionTF;

    @FXML
    private ComboBox<String> eventTF; // Keep ComboBox<String>

    @FXML
    private TextField titleTF;

    @FXML
    void ajouterReclamation(ActionEvent event) {
        try {
            // Récupération des valeurs
            String title = titleTF.getText().trim();
            String description = descriptionTF.getText().trim();
            String selectedTitle = eventTF.getValue();

            // Vérifications
            if (title.isEmpty() || title.length() <= 5) {
                throw new IllegalArgumentException("Le titre doit contenir plus de 5 caractères !");
            }
            if (description.isEmpty() || description.length() <= 5) {
                throw new IllegalArgumentException("La description doit contenir plus de 5 caractères !");
            }
            if (selectedTitle == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un événement !");
            }

            // Création des objets associés
            int eventId = eventMap.get(selectedTitle);
            Event e = new Event();
            e.setId(eventId);
            User user = new User();
            user.setId(1);

            // Création et ajout de la réclamation
            Reclamation r = new Reclamation(title, description, e, user);
            serviceReclamation.ajouter(r);

            showAlert(AlertType.INFORMATION, "Succès", "Réclamation ajoutée avec succès !");

        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Erreur de saisie", e.getMessage());

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur de base de données", "Erreur lors de l'ajout de la réclamation !");

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur inattendue", "Une erreur inattendue s'est produite.");
        }
    }

    @FXML
    void initialize() {
        ServiceEvent serviceEvent = new ServiceEvent();

        // Récupérer tous les événements de la base de données
        List<Event> events = serviceEvent.fetchAllEvents();

        // Remplir la ComboBox et la map
        for (Event event : events) {
            eventTF.getItems().add(event.getTitle()); // Ajouter le titre à la ComboBox
            eventMap.put(event.getTitle(), event.getId()); // Associer titre -> ID
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
