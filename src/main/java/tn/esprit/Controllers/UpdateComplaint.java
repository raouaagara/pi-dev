package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import tn.esprit.entities.Complaint;

public class UpdateComplaint {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField locationField;

    @FXML
    private Button updateButton;
    private Complaint selectedComplaint;


    @FXML
    private void handleUpdateButtonAction() {
        // Retrieve values from input fields
        String title = titleField.getText();
        String description = descriptionTextArea.getText();
        String category = categoryTextField.getText(); // Retrieve text from TextField
        String location = locationField.getText();

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
            titleField.setText(selectedComplaint.getTitle());
            descriptionTextArea.setText(selectedComplaint.getDescription());
            categoryTextField.setText(selectedComplaint.getCategory()); // Set text to TextField
            locationField.setText(selectedComplaint.getLocation());
        }
    }
}
