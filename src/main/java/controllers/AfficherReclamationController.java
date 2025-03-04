package controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.Reclamation;

public class AfficherReclamationController {

    @FXML
    private Text descId;

    @FXML
    private Text eventId;

    @FXML
    private Text titreId;

    private Reclamation reclamation;

    public void initData(Reclamation reclamation) {
        this.reclamation = reclamation;
        descId.setText(String.valueOf(reclamation.getDescription()));
        eventId.setText(String.valueOf(reclamation.getEvent().getTitle()));
        titreId.setText(reclamation.getTitle());
    }
}
