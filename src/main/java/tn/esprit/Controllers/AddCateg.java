package tn.esprit.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AddCateg {

    @FXML
    private ComboBox<String> nameCategComboBox;

    @FXML
    private TextField userIdTextField;

    @FXML
    public void addbutton(ActionEvent actionEvent) {
        // Check if any of the fields are empty
        if (nameCategComboBox.getValue() == null || nameCategComboBox.getValue().isEmpty() ||
                userIdTextField.getText().isEmpty()) {
            // If any field is empty, display an alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return;
        }


        try {
            // Load the FXML file for AddComplaint
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddComplaint.fxml"));
            Parent root = loader.load();

            // Access the AddComplaint controller if needed
            AddComplaint addComplaintController = loader.getController();
            // Pass data to the AddComplaint controller
           // addComplaintController.setCategoryId(nameCategComboBox.getValue());
           // addComplaintController.setUserId(userIdTextField.getText());

            // Create a new scene
            Scene scene = new Scene(root);

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showComplaints(ActionEvent actionEvent)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ShowCateg.fxml"));
        userIdTextField.getScene().setRoot(root);
        System.out.println("Moved to the next page");
    }

}
