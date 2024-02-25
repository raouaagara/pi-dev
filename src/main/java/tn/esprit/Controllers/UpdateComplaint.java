package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.entities.Complaint;

public class UpdateComplaint {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField locationField;

    @FXML
    private Button updateButton;
    private Complaint selectedComplaint;

    @FXML
    public void initialize() {
        // Initialize the combo box with categories
        categoryComboBox.setItems(FXCollections.observableArrayList("pollution", "path hole", "other"));
        categoryComboBox.setPromptText("Select category");
    }

    @FXML
    private void handleUpdateButtonAction() {
        // Retrieve values from input fields
        String title = titleField.getText();
        String description = descriptionTextArea.getText();
        String category = categoryComboBox.getSelectionModel().getSelectedItem(); // Retrieve selected item
        String location = locationField.getText();

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty() || category == null || location.isEmpty()) {
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
            categoryComboBox.setValue(selectedComplaint.getCategory());
            locationField.setText(selectedComplaint.getLocation());
        }
    }
}
