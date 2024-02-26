package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.sql.SQLException;

public class UpdateComplaint {
    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea categoryTextArea;

    @FXML
    private TextArea locationTextArea;

    @FXML
    private TextArea statusTextArea;

    @FXML
    private TextArea userTextArea;

    @FXML
    private Button updateButton;

    private Complaint selectedComplaint;
    private ComplaintService complaintService;

    public UpdateComplaint() {
        complaintService = new ComplaintService();
    }

    @FXML
    private void handleUpdateButtonAction() {
        // Retrieve values from input fields
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryTextArea.getText(); // Retrieve text from TextField
        String location = locationTextArea.getText();
        String status = statusTextArea.getText();
        String user = userTextArea.getText();

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || location.isEmpty()) {
            System.out.println("Please fill in all fields.");
        } else {
            // Perform update action
            try {
                selectedComplaint.setTitle(title);
                selectedComplaint.setDescription(description);
                selectedComplaint.setCategory(category);
                selectedComplaint.setLocation(location);
                selectedComplaint.setStatus(status);
                selectedComplaint.setUser(user);

                // Update the complaint in the database
                complaintService.update(selectedComplaint);

                // Close the update window after successful update
                updateButton.getScene().getWindow().hide();
            } catch (SQLException e) {
                System.out.println("Error updating complaint: " + e.getMessage());
            }
        }
    }

    public void setComplaint(Complaint selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
        // Pre-fill the fields with the complaint details
        if (selectedComplaint != null) {
            titleTextArea.setText(selectedComplaint.getTitle());
            descriptionTextArea.setText(selectedComplaint.getDescription());
            categoryTextArea.setText(selectedComplaint.getCategory());
            locationTextArea.setText(selectedComplaint.getLocation());
            statusTextArea.setText(selectedComplaint.getStatus());
            userTextArea.setText(selectedComplaint.getUser());
        }
    }
}

