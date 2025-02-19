package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import tests.MainFX;

public class HomeReclamationController {

    @FXML
    private BorderPane contentArea; // Assurez-vous qu'il correspond à l'ID dans HomeReclamation.fxml

    @FXML
    private void loadAjouterReclamation() {
        MainFX.loadFXML("AjouterReclamation.fxml");
    }

    @FXML
    private void loadAfficherReclamation() {
        MainFX.loadFXML("AfficherReclamations.fxml");
    }
}
