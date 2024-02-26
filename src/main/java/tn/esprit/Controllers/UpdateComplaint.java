package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import tn.esprit.entities.Complaint;

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
            System.out.println("Update button clicked");
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Category: " + category);
            System.out.println("Location: " + location);
        }
    }

    public void setComplaint(Complaint selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
        // Pre-fill the fields with the complaint details
        if (selectedComplaint != null) {
            titleTextArea.setText(selectedComplaint.getTitle());
            descriptionTextArea.setText(selectedComplaint.getDescription());
            categoryTextArea.setText(selectedComplaint.getCategory()); // Set text to TextField
            locationTextArea.setText(selectedComplaint.getLocation());
            statusTextArea.setText(selectedComplaint.getLocation());
            userTextArea.setText(selectedComplaint.getLocation());
        }
    }
}
