package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import tests.MainFX;

public class HomeReclamationController {

    @FXML
    private BorderPane contentArea;

    @FXML
    private void loadAjouterReclamation() {
        MainFX.loadFXML("AjouterReclamation.fxml");
    }

    @FXML
    public void loadAfficherReclamationsClient() {
        MainFX.loadFXML("AfficherReclamationsClient.fxml");
    }
    @FXML
    public void loadAfficherReclamationsAdmin() {
        MainFX.loadFXML("AfficherReclamationsAdmin.fxml");
    }
}
