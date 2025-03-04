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


    private Reclamation reclamation;
    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    // Initialize the data for the reclamation
    public void initData(Reclamation reclamation) {
        this.reclamation = reclamation;
        updateUI();
    }

    // Update the UI elements with the reclamation's data
    private void updateUI() {
        if (reclamation != null) {
            titleLabel.setText(reclamation.getTitle());
            descriptionLabel.setText(reclamation.getDescription());
            reponseTF.setText(reclamation.getAnswer() != null ? reclamation.getAnswer() : "Pas encore de réponse");
        }
    }



    // Update the reclamation status and answer
    public void updateReclamationStatus() {
        String answer = reponseTF.getText();  // Get the answer text

            try {
                serviceReclamation.modifierEtat(reclamation, answer);
                System.out.println("Reclamation status updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}
