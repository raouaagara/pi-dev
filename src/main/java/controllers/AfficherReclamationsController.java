package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Event;
import models.Reclamation;
import services.ServiceReclamation;
import java.io.IOException;
import java.sql.SQLException;

public class AfficherReclamationsController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, Integer> idCol;
    @FXML
    private TableColumn<Reclamation, String> titleCol;
    @FXML
    private TableColumn<Reclamation, String> eventCol;
    @FXML
    private TableColumn<Reclamation, Void> modifyCol;
    @FXML
    private TableColumn<Reclamation, Void> deleteCol;

    private ServiceReclamation serviceReclamation = new ServiceReclamation();

    /**
     * Initialise le tableau des réclamations avec les colonnes et les données.
     * Configure également les boutons de modification et de suppression.
     */
    public void initialize() {
        // Configuration des colonnes
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Récupération et affichage du titre de l'événement associé à chaque réclamation
        eventCol.setCellValueFactory(param -> {
            Event event = param.getValue().getEvent();
            if (event != null && event.getTitle() != null) {
                return new SimpleStringProperty(event.getTitle());
            } else {
                return new SimpleStringProperty("Aucun événement");
            }
        });

        // Chargement des réclamations depuis la base de données
        try {
            reclamationTable.getItems().addAll(serviceReclamation.recupererAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Configuration des boutons de modification
        modifyCol.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    loadModifyView(reclamation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });

        // Configuration des boutons de suppression
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    try {
                        serviceReclamation.supprimer(reclamation.getId());
                        reclamationTable.getItems().remove(reclamation);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    /**
     * Charge l'interface de modification d'une réclamation sélectionnée.
     */
    private void loadModifyView(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReclamation.fxml"));
            Parent root = loader.load();
            ModifierReclamationController controller = loader.getController();
            controller.initData(reclamation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
