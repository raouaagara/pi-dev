package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;
import java.io.IOException;
import java.sql.SQLException;

public class AddComplaint {

    private final ComplaintService complaintService = new ComplaintService();

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

    // Method to handle adding the complaint
    @FXML
    private void add(ActionEvent event) {
        // Retrieve values from input fields
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryTextArea.getText();
        String location = locationTextArea.getText();
        String status = statusTextArea.getText();
        String user = userTextArea.getText();

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() ||
                location.isEmpty() || status.isEmpty() || user.isEmpty()) {
            showAlert("Please fill in all fields.");
            return; // Stop further execution
        }

        try {
            // Create and save the complaint using the provided data
            Complaint newComplaint = new Complaint(title, description, category, location, status, user);
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

    // Method to navigate to the next page
    @FXML
    private void NextPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ShowComplaint.fxml"));
        titleTextArea.getScene().setRoot(root);
        System.out.println("Moved to the next page");
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
        categoryTextArea.clear();
        locationTextArea.clear();
        statusTextArea.clear();
        userTextArea.clear();
    }

   /* public void setCategoryId(String value) {
        // Set the category ID received from AddCateg into the nameCategComboBox
        nameCategComboBox.setValue(value);
    }

    public void setUserId(String text) {
        // Set the user ID received from AddCateg into the userIdTextField
        userIdTextField.setText(text);
    }*/
}




