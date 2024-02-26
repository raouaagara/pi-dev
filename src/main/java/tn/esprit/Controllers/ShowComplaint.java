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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

    private final ComplaintService complaintService = new ComplaintService();

    @FXML
    void initialize() {
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
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
            tableView.getItems().remove(selectedComplaint);
        } catch (SQLException e) {
            showAlert("Error deleting complaint: " + e.getMessage());
        }
    }

    private void showAlert(String s) {
    }

    @FXML
    public void updateAction(ActionEvent actionEvent) {
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Please select a complaint to update.");
            return;
        }

        try {
            openUpdateWindow(selectedComplaint); // Call the method to open the update window
        } catch (Exception e) {
            showAlert("Error opening update complaint window: " + e.getMessage());
        }
    }

    // Method to open the update interface with the selected complaint
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error opening update complaint window: " + e.getMessage());
        }
    }


    public void updateSelectedComplaint(ActionEvent actionEvent) {
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Please select a complaint to update.");
            return;
        }

        try {
            // Open the UpdateComplaint window
            openUpdateWindow(selectedComplaint);
        } catch (Exception e) {
            showAlert("Error opening update complaint window: " + e.getMessage());
        }
    }
}