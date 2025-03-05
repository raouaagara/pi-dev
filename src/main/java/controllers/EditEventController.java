package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Events;
import services.ServicesEvents;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public class EditEventController {
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

    private Events eventToEdit;
    private File selectedImageFile;
    private ServicesEvents servicesEvents;

    public void setEventToEdit(Events event) {
        this.eventToEdit = event;

        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());
        locationField.setText(event.getLocation());
        startDatePicker.setValue(event.getStartDate().toLocalDate());
        endDatePicker.setValue(event.getEndDate().toLocalDate());

        if (event.getImage() != null && !event.getImage().isEmpty()) {
            String imagePath = "/images/" + event.getImage();
            imageView.setImage(new Image(getClass().getResource(imagePath).toString()));
        }
    }

    public void initialize() {
        servicesEvents = new ServicesEvents();
    }

    @FXML
    private void handleSave() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la modification");
        alert.setHeaderText("Êtes-vous sûr de vouloir enregistrer les modifications ?");
        alert.setContentText("Les modifications seront appliquées à cet événement.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                eventToEdit.setTitle(titleField.getText());
                eventToEdit.setDescription(descriptionField.getText());
                eventToEdit.setLocation(locationField.getText());
                eventToEdit.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
                eventToEdit.setEndDate(java.sql.Date.valueOf(endDatePicker.getValue()));

                if (selectedImageFile != null) {
                    String fileName = selectedImageFile.getName();
                    eventToEdit.setImage(fileName);
                }

                servicesEvents.modifier(
                        eventToEdit.getId(),
                        eventToEdit.getTitle(),
                        eventToEdit.getDescription(),
                        eventToEdit.getLocation(),
                        eventToEdit.getStartDate().toString(),
                        eventToEdit.getEndDate().toString(),
                        eventToEdit.getImage()
                );

                System.out.println("Événement modifié avec succès !");

                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
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