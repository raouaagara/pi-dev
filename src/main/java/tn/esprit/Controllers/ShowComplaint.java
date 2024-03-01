package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowComplaint {

    @FXML
    private TableColumn<Complaint, String> titleColumn;

    @FXML
    private TableColumn<Complaint, String> descriptionColumn;

    @FXML
    private TableColumn<Complaint, String> categoryColumn;

    @FXML
    private TableColumn<Complaint, String> locationColumn;

    @FXML
    private TableColumn<Complaint, String> statusColumn;

    @FXML
    private TableColumn<Complaint, Date> datePostedColumn;

    @FXML
    private TableColumn<Complaint, String> userColumn;

    @FXML
    private TableView<Complaint> tableView;
    @FXML
    private TextField titleSearchField;

    @FXML
    private TextField locationSearchField;

    @FXML
    private TextField statusSearchField;
    @FXML
    private ComboBox<String> sortComboBox;


    private final ComplaintService complaintService = new ComplaintService();

    @FXML
    void initialize() {
        loadComplaints();
    }

    // Method to load complaints into TableView
    private void loadComplaints() {
        try {
            List<Complaint> complaints = complaintService.displayList();
            ObservableList<Complaint> observableList = FXCollections.observableList(complaints);
            tableView.setItems(observableList);

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
            datePostedColumn.setCellValueFactory(new PropertyValueFactory<>("datePosted"));
        } catch (SQLException e) {
            showAlert("Error loading complaints: " + e.getMessage());
        }
    }

    @FXML
    public void deleteAction(ActionEvent actionEvent) {
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Please select a complaint to delete.");
            return;
        }

        try {
            // Delete the selected complaint
            complaintService.delete(selectedComplaint);

            // Refresh the table view to reflect the changes
            loadComplaints(); // Reload complaints after deletion
        } catch (SQLException e) {
            showAlert("Error deleting complaint: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to open the update interface with the selected complaint
    @FXML
    public void updateSelectedComplaint(ActionEvent actionEvent) {
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Please select a complaint to update.");
            return;
        }

        // Call the method to open the update window
        openUpdateWindow(selectedComplaint);
    }

    // Method to open the update window
    private void openUpdateWindow(Complaint complaint) {
        try {
            // Load the update interface FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateComplaint.fxml"));
            Parent root = loader.load();

            // Pass the selected complaint to the update controller
            UpdateComplaint updateController = loader.getController();
            updateController.setComplaint(complaint);

            // Display the update interface
            Stage stage = new Stage();
            stage.setTitle("Update Complaint");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // After the update window is closed, refresh the complaints list
            loadComplaints();
        } catch (IOException e) {
            showAlert("Error opening update complaint window: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String titleKeyword = titleSearchField.getText().trim();
        String locationKeyword = locationSearchField.getText().trim();
        String statusKeyword = statusSearchField.getText().trim();

        try {
            ComplaintService complaintService = new ComplaintService();
            List<Complaint> complaints = complaintService.displayList(); // Get the list of all complaints

            // Filter the list of complaints based on the provided criteria
            List<Complaint> searchResults = complaints.stream()
                    .filter(complaint -> titleKeyword.isEmpty() || complaint.getTitle().contains(titleKeyword))
                    .filter(complaint -> locationKeyword.isEmpty() || complaint.getLocation().contains(locationKeyword))
                    .filter(complaint -> statusKeyword.isEmpty() || complaint.getStatus().contains(statusKeyword))
                    .collect(Collectors.toList());

            ObservableList<Complaint> observableList = FXCollections.observableList(searchResults);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Error searching complaints: " + e.getMessage());
        }
    }


    // Method to navigate back to the previous page (AddComplaint.fxml)
    @FXML
    private void previousPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AddComplaint.fxml"));
        tableView.getScene().setRoot(root);
        System.out.println("Returned to the previous page (AddComplaint.fxml)");
    }

    @FXML
    public void sortComboBox(ActionEvent actionEvent) {
        String sortBy = sortComboBox.getValue();
        if (sortBy != null) {
            try {
                ComplaintService complaintService = new ComplaintService();
                List<Complaint> complaints = complaintService.displayList();

                // Create an instance of ComplaintService and call the sort method on that instance
                complaintService.sort(complaints, sortBy);

                ObservableList<Complaint> observableList = FXCollections.observableList(complaints);
                tableView.setItems(observableList);
            } catch (SQLException e) {
                showAlert("Error sorting complaints: " + e.getMessage());
            }
        }
    }
}