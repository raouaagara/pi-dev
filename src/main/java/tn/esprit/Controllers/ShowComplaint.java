package tn.esprit.Controllers;

import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.entities.Complaint;
import tn.esprit.services.ComplaintService;

import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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

    @FXML
    private TableColumn<Complaint, ImageView> imageColumn;

    @FXML
    private Label averageScoreLabel;

    private final ComplaintService complaintService = new ComplaintService();


    @FXML
    void initialize() {
        loadComplaints();
        showAverageScore();
    }

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

    public void generatePDF(ActionEvent actionEvent) {
        // Get the list of complaints from your table view
        List<Complaint> complaints = tableView.getItems();

        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a new content stream for adding content to the PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 4);
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 800);
                contentStream.showText("List of Complaints:");
                contentStream.endText();

                // Initialize table column positions
                int xTitle = 20;
                int xDescription = 80;
                int xCategory = 140;
                int xLocation = 200;
                int xStatus = 260;
                int xDatePosted = 320;
                int xUser = 380;
                int xImage = 440;

                int y = 780; // Initial y-coordinate for complaint content

                // Add table headers
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 4);
                contentStream.newLineAtOffset(xTitle, y);
                contentStream.showText("Title");
                contentStream.newLineAtOffset(xDescription - xTitle, 0);
                contentStream.showText("Description");
                contentStream.newLineAtOffset(xCategory - xDescription, 0);
                contentStream.showText("Category");
                contentStream.newLineAtOffset(xLocation - xCategory, 0);
                contentStream.showText("Location");
                contentStream.newLineAtOffset(xStatus - xLocation, 0);
                contentStream.showText("Status");
                contentStream.newLineAtOffset(xDatePosted - xStatus, 0);
                contentStream.showText("Date Posted");
                contentStream.newLineAtOffset(xUser - xDatePosted, 0);
                contentStream.showText("User");
                contentStream.newLineAtOffset(xImage - xUser, 0);
                contentStream.showText("Image");
                contentStream.endText();

                y -= 10; // Adjust y-coordinate for the next complaint

                // Add each complaint to the PDF table
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (Complaint complaint : complaints) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 4);
                    contentStream.newLineAtOffset(xTitle, y);
                    contentStream.showText(complaint.getTitle());
                    contentStream.newLineAtOffset(xDescription - xTitle, 0);
                    contentStream.showText(complaint.getDescription());
                    contentStream.newLineAtOffset(xCategory - xDescription, 0);
                    contentStream.showText(complaint.getCategory());
                    contentStream.newLineAtOffset(xLocation - xCategory, 0);
                    contentStream.showText(complaint.getLocation());
                    contentStream.newLineAtOffset(xStatus - xLocation, 0);
                    contentStream.showText(complaint.getStatus());
                    contentStream.newLineAtOffset(xDatePosted - xStatus, 0);
                    contentStream.showText(dateFormat.format(complaint.getDatePosted()));
                    contentStream.newLineAtOffset(xUser - xDatePosted, 0);
                    contentStream.showText(complaint.getUser());
                    contentStream.newLineAtOffset(xImage - xUser, 0);
                    contentStream.showText(complaint.getImagePath());
                    contentStream.endText();

                    y -= 10; // Adjust y-coordinate for the next complaint
                }
            }

            // Save the PDF document to a file
            File file = new File("complaints.pdf");
            document.save(file);
            System.out.println("PDF saved to: " + file.getAbsolutePath());

            // Show alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Generated");
            alert.setHeaderText(null);
            alert.setContentText("PDF saved to: " + file.getAbsolutePath());
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAverageScore() {
        try {
            double averageScore = complaintService.calculateAverageScore();
            averageScoreLabel.setText("Average Score: " + averageScore);
        } catch (SQLException e) {
            showAlert("Error calculating average score: " + e.getMessage());
        }
    }
}