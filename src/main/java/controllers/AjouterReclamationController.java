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
    private final Map<String, Integer> eventMap = new HashMap<>(); // Stocke les titres d'événements et leurs IDs

    @FXML
    private TextArea descriptionTF;

    @FXML
    private ComboBox<String> eventTF;

    @FXML
    private TextField titleTF;

    /**
     * Ajoute une réclamation après validation des champs.
     * Affiche des alertes en cas d'erreur.
     * @param event l'événement déclencheur de l'action
     */
    @FXML
    void ajouterReclamation(ActionEvent event) {
        try {
            String title = titleTF.getText().trim();
            String description = descriptionTF.getText().trim();
            String selectedTitle = eventTF.getValue();

            // Vérification des champs obligatoires
            if (title.isEmpty() || title.length() <= 5) {
                throw new IllegalArgumentException("Le titre doit contenir plus de 5 caractères !");
            }
            if (description.isEmpty() || description.length() <= 5) {
                throw new IllegalArgumentException("La description doit contenir plus de 5 caractères !");
            }
            if (selectedTitle == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un événement !");
            }

            int eventId = eventMap.get(selectedTitle);
            Event e = new Event();
            e.setId(eventId);
            User user = new User();
            user.setId(1); // À remplacer par l'ID utilisateur dynamique

            Reclamation r = new Reclamation(title, description, e, user);
            serviceReclamation.ajouter(r);

            showAlert(AlertType.INFORMATION, "Succès", "Réclamation ajoutée avec succès !");

        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Erreur de saisie", e.getMessage());
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur de base de données", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur inattendue", "Une erreur inattendue s'est produite.");
        }
    }

    /**
     * Initialise le contrôleur en récupérant les événements de la base de données.
     * Remplit la ComboBox avec les titres des événements disponibles.
     */
    @FXML
    void initialize() {
        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> events = serviceEvent.fetchAllEvents();

        for (Event event : events) {
            eventTF.getItems().add(event.getTitle());
            eventMap.put(event.getTitle(), event.getId());
        }
    }

    /**
     Affiche une alerte à l'utilisateur.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
