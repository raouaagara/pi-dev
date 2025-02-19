package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Reclamation;
import models.Event;
import services.ServiceEvent;
import services.ServiceReclamation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierReclamationController {

    @FXML
    private TextField titleTF;

    @FXML
    private TextArea descriptionTF;

    @FXML
    private ComboBox<String> eventTF;

    @FXML
    private Button modifyButton;

    private ServiceReclamation serviceReclamation = new ServiceReclamation();
    private ServiceEvent serviceEvent = new ServiceEvent();

    private Reclamation currentReclamation;
    private Map<String, Integer> eventMap = new HashMap<>(); // Stocke les titres d'événements et leurs IDs

    /**
     * Initialise le contrôleur en récupérant les événements de la base de données.
     * Remplit la ComboBox avec les titres des événements disponibles.
     */
    public void initialize() {
        List<Event> events = serviceEvent.fetchAllEvents();
        for (Event event : events) {
            eventTF.getItems().add(event.getTitle()); // Ajoute les titres d'événements à la ComboBox
            eventMap.put(event.getTitle(), event.getId()); // Associe les titres aux IDs des événements
        }
    }

    /**
     * Initialise le formulaire avec les données de la réclamation actuelle.
     */
    public void initData(Reclamation reclamation) {
        this.currentReclamation = reclamation;
        titleTF.setText(reclamation.getTitle()); // Remplit le champ titre
        descriptionTF.setText(reclamation.getDescription()); // Remplit la description
        eventTF.setValue(reclamation.getEvent().getTitle()); // Sélectionne l'événement associé
    }

    /**
     * Modifie la réclamation actuelle avec les nouvelles données saisies après validation.
     */
    @FXML
    private void modifyReclamation() {
        if (currentReclamation != null) {
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

                int eventId = eventMap.get(selectedTitle); // Récupère l'ID de l'événement sélectionné
                Event updatedEvent = new Event();
                updatedEvent.setId(eventId);

                currentReclamation.setTitle(title);
                currentReclamation.setDescription(description);
                currentReclamation.setEvent(updatedEvent); // Met à jour l'événement associé

                // Appelle le service pour mettre à jour la réclamation en base de données
                serviceReclamation.modifier(currentReclamation);
                showAlert(AlertType.INFORMATION, "Succès", "Réclamation modifiée avec succès !");
            } catch (IllegalArgumentException e) {
                showAlert(AlertType.ERROR, "Erreur de saisie", e.getMessage());
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Erreur de base de données", "Erreur lors de la modification de la réclamation !");
            } catch (Exception e) {
                e.printStackTrace(); // Debug
                showAlert(AlertType.ERROR, "Erreur inattendue", "Une erreur inattendue s'est produite.");
            }
        }
    }


    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
