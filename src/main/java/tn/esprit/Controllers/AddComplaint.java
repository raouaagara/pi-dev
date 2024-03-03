package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddComplaint {

    private final ComplaintService complaintService = new ComplaintService();

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
    private ImageView uploadedImageView; // ImageView for displaying the uploaded image

    // Method to handle adding the complaint
    @FXML
    private void add(ActionEvent event) {
        // Retrieve values from input fields
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryComboBox.getValue();
        String location = locationTextArea.getText();
        String status = statusComboBox.getValue();
        String user = userTextArea.getText();

        if (title.isEmpty() || description.isEmpty() || category == null ||
                location.isEmpty() || status == null || user.isEmpty()) {
            showAlert("Please fill in all fields.");
            return; // Stop further execution
        }

        // Validate title format
        if (!isValidTitle(title)) {
            showAlert("Please enter a valid title. It should contain only letters and numbers.");
            return; // Stop further execution
        }

        try {
            String imagePath = uploadedImageView.getImage() != null ? uploadedImageView.getImage().getUrl() : null;

            Complaint newComplaint = new Complaint(title, description, category, location, status,user,imagePath);
            complaintService.add(newComplaint);

            // Show confirmation message
            showAlert("A new complaint has been added.");

            // Clear input fields after adding the complaint
            clearFields();
        } catch (SQLException e) {
            // Show error message if an error occurs
            showAlert("Error adding complaint: " + e.getMessage());
        }
    }

    // Method to show an alert with the given message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear input fields
    private void clearFields() {
        titleTextArea.clear();
        descriptionTextArea.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        locationTextArea.clear();
        statusComboBox.getSelectionModel().clearSelection();
        userTextArea.clear();
    }

    // Method to validate title format (only letters and numbers)
    private boolean isValidTitle(String title) {
        return Pattern.matches("[a-zA-Z0-9\\s]+", title);
    }

    @FXML
    void uploadImage(ActionEvent event) {
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

    @FXML
    private void NextPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ShowComplaint.fxml"));
        titleTextArea.getScene().setRoot(root);
        System.out.println("Moved to the next page");
    }

}
