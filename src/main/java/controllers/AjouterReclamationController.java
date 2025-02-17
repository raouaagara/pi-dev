package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.ServiceReclamation;

public class AjouterReclamationController {

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    private TextArea descriptionTF;

    @FXML
    private ComboBox<?> eventTF;

    @FXML
    private TextField titleTF;

    @FXML
    void ajouterReclamation(ActionEvent event) {

    }

}
