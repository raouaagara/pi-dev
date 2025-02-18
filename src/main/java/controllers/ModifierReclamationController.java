package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private Map<String, Integer> eventMap = new HashMap<>(); // Map to store event titles and their IDs

    // Initialize the eventMap and ComboBox
    public void initialize() {
        List<Event> events = serviceEvent.fetchAllEvents();
        for (Event event : events) {
            eventTF.getItems().add(event.getTitle()); // Add event titles to ComboBox
            eventMap.put(event.getTitle(), event.getId()); // Store event title -> ID mapping
        }
    }

    // Initialize the form with the data from the current Reclamation object
    public void initData(Reclamation reclamation) {
        this.currentReclamation = reclamation;
        titleTF.setText(reclamation.getTitle());
        descriptionTF.setText(reclamation.getDescription());
        eventTF.setValue(reclamation.getEvent().getTitle()); // Set the current event title in ComboBox
    }

    // Modify the current Reclamation based on the updated form data
    @FXML
    private void modifyReclamation() {
        if (currentReclamation != null) {
            try {
                // Get the event ID from the ComboBox selection
                String selectedEventTitle = eventTF.getValue();
                int eventId = eventMap.get(selectedEventTitle); // Get event ID using eventMap

                // Update the Reclamation object
                Event updatedEvent = new Event();
                updatedEvent.setId(eventId);
                currentReclamation.setTitle(titleTF.getText());
                currentReclamation.setDescription(descriptionTF.getText());
                currentReclamation.setEvent(updatedEvent); // Set the updated event

                // Call the service to update the reclamation in the database
                serviceReclamation.modifier(currentReclamation);
                closeWindow();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        stage.close();
    }
}
