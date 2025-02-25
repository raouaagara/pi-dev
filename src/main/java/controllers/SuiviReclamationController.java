package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import models.Reclamation;
import models.Status;
import services.ServiceReclamation;

import java.sql.SQLException;

public class SuiviReclamationController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea reponseTF;

    @FXML
    private ComboBox<String> etatTF;

    private Reclamation reclamation;
    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    // Initialize the data for the reclamation
    public void initData(Reclamation reclamation) {
        this.reclamation = reclamation;
        updateUI();
        populateEtatComboBox();
    }

    // Update the UI elements with the reclamation's data
    private void updateUI() {
        if (reclamation != null) {
            titleLabel.setText(reclamation.getTitle());
            descriptionLabel.setText(reclamation.getDescription());
            reponseTF.setText(reclamation.getAnswer() != null ? reclamation.getAnswer() : "Pas encore de réponse");
            etatTF.setValue(reclamation.getStatus().getFrenchLabel());
        }
    }

    // Populate the ComboBox with the possible status labels (French)
    private void populateEtatComboBox() {
        for (Status status : Status.values()) {
            etatTF.getItems().add(status.getFrenchLabel());
        }
    }

    // Update the reclamation status and answer
    public void updateReclamationStatus() {
        String selectedFrenchStatus = etatTF.getValue();  // Get the selected status from ComboBox
        String answer = reponseTF.getText();  // Get the answer text

        // Map the selected French label to the corresponding Status enum
        Status selectedStatus = null;
        for (Status status : Status.values()) {
            if (status.getFrenchLabel().equals(selectedFrenchStatus)) {
                selectedStatus = status;
                break;
            }
        }

        if (selectedStatus != null) {
            try {
                serviceReclamation.modifierEtat(reclamation, selectedStatus.name(), answer);
                System.out.println("Reclamation status updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid status selected.");
        }
    }
}
