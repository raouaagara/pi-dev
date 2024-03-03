package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.io.File;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class UpdateComplaint {
    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextArea locationTextArea;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextArea userTextArea;

    @FXML
    private Button updateButton;

    @FXML
    private ImageView uploadedImageView;

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
        String category = categoryComboBox.getValue(); // Retrieve value from ComboBox
        String location = locationTextArea.getText();
        String status = statusComboBox.getValue(); // Retrieve value from ComboBox
        String user = userTextArea.getText();
        String imagePath = selectedComplaint.getImagePath();

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty() || category == null || location.isEmpty() || status == null) {
            showAlert("Please fill in all fields.");
        } else {
            // Validate title format
            if (!isValidTitle(title)) {
                showAlert("Please enter a valid title. It should contain only letters, numbers, and spaces.");
                return;
            }

            // Perform update action
            try {
                selectedComplaint.setTitle(title);
                selectedComplaint.setDescription(description);
                selectedComplaint.setCategory(category);
                selectedComplaint.setLocation(location);
                selectedComplaint.setStatus(status);
                selectedComplaint.setUser(user);
                selectedComplaint.setImagePath(imagePath);

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
            categoryComboBox.setValue(selectedComplaint.getCategory());
            locationTextArea.setText(selectedComplaint.getLocation());
            statusComboBox.setValue(selectedComplaint.getStatus());
            userTextArea.setText(selectedComplaint.getUser());

            if (selectedComplaint.getImagePath() != null && !selectedComplaint.getImagePath().isEmpty()) {
                Image image = new Image(selectedComplaint.getImagePath());
                uploadedImageView.setImage(image);
            }
        }
    }

    // Method to validate title format (only letters, numbers, and spaces)
    private boolean isValidTitle(String title) {
        return Pattern.matches("[a-zA-Z0-9\\s]+", title);
    }

    // Method to show an alert with the given message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void uploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(((Stage) titleTextArea.getScene().getWindow()));
        if (selectedFile != null) {
            // Set the image path in your Complaint object
            Complaint complaint = new Complaint();
            complaint.setImagePath(selectedFile.toURI().toString());

            // Display the selected image in the UI
            Image image = new Image(selectedFile.toURI().toString());
            uploadedImageView.setImage(image);
        }
    }
}
