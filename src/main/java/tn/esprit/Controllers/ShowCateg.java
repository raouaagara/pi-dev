package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ShowCateg {
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
    private TableColumn<Complaint, ImageView> imageColumn;

    @FXML
    private TableView<Complaint> tableView;

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
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
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
}
