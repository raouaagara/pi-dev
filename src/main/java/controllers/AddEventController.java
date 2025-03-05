package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Events;
import services.ServicesEvents;

import java.io.File;
import java.sql.SQLException;

public class AddEventController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ImageView imageView;

    private File selectedImageFile;
    private ServicesEvents servicesEvents;

    public void initialize() {
        servicesEvents = new ServicesEvents();
    }

    @FXML
    private void handleAddEvent() {
        if (titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || locationField.getText().isEmpty()
                || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        try {
            Events event = new Events();
            event.setTitle(titleField.getText());
            event.setDescription(descriptionField.getText());
            event.setLocation(locationField.getText());
            event.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
            event.setEndDate(java.sql.Date.valueOf(endDatePicker.getValue()));

            if (selectedImageFile != null) {
                String fileName = selectedImageFile.getName();
                event.setImage(fileName);
            }

            servicesEvents.ajouter(event);
            System.out.println("Événement ajouté avec succès !");

            titleField.getScene().getWindow().hide();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        titleField.getScene().getWindow().hide();
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedImageFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        }
    }
}