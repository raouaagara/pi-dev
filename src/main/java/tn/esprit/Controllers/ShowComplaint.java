package tn.esprit.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;
import javafx.stage.Modality;
import java.sql.SQLException;
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



    @FXML
    public void updateAction(ActionEvent actionEvent) {
        Complaint selectedComplaint = tableView.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Please select a complaint to update.");
            return;
        }

        try {
            // Open the UpdateComplaint window
            UpdateComplaint updateComplaint = new UpdateComplaint();
            updateComplaint.setComplaint(selectedComplaint);
            // Call method to show the update window
        } catch (Exception e) {
            showAlert("Error opening update complaint window: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
