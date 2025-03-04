package controllers;

import javafx.beans.property.SimpleStringProperty;
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

public class AfficherReclamationsAdminController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, Integer> idCol;
    @FXML
    private TableColumn<Reclamation, String> titleCol;
    @FXML
    private TableColumn<Reclamation, String> eventCol;
    @FXML
    private TableColumn<Reclamation, Button> modifyCol;
    @FXML
    private TableColumn<Reclamation, Button> deleteCol;
    @FXML
    private TableColumn<Reclamation, Button> suivreCol; // Nouvelle colonne pour "Suivre"

    private ServiceReclamation serviceReclamation = new ServiceReclamation();

    public void initialize() {
        // Configuration des colonnes
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        eventCol.setCellValueFactory(param -> {
            Event event = param.getValue().getEvent();
            if (event != null && event.getTitle() != null) {
                return new SimpleStringProperty(event.getTitle());
            } else {
                return new SimpleStringProperty("Aucun événement");
            }
        });

        try {
            reclamationTable.getItems().addAll(serviceReclamation.recupererAll());
        } catch (SQLException e) {
            e.printStackTrace(); // Assurez-vous que l'exception est correctement gérée et afficher un message si nécessaire
        }

        // Configuration des boutons de modification
        modifyCol.setCellFactory(param -> new TableCell<Reclamation, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button modifyButton = new Button("Modifier");
                    modifyButton.setOnAction(event -> {
                        Reclamation reclamation = getTableView().getItems().get(getIndex());
                        loadModifyView(reclamation);
                    });
                    setGraphic(modifyButton);
                }
            }
        });

        // Configuration des boutons de suppression
        deleteCol.setCellFactory(param -> new TableCell<Reclamation, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setOnAction(event -> {
                        Reclamation reclamation = getTableView().getItems().get(getIndex());
                        try {
                            serviceReclamation.supprimer(reclamation.getId());
                            reclamationTable.getItems().remove(reclamation);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    setGraphic(deleteButton);
                }
            }
        });

        // Configuration des boutons "Suivre"
        suivreCol.setCellFactory(param -> new TableCell<Reclamation, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button suivreButton = new Button("Suivre");
                    suivreButton.setOnAction(event -> {
                        Reclamation reclamation = getTableView().getItems().get(getIndex());
                        loadSuiviReclamationView(reclamation);
                    });
                    setGraphic(suivreButton);
                }
            }
        });
    }

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

    private void loadSuiviReclamationView(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SuiviReclamation.fxml"));
            Parent root = loader.load();

            // Vérifier que le contrôleur est bien initialisé
            SuiviReclamationController controller = loader.getController();
            if (controller != null) {
                controller.initData(reclamation);
            } else {
                System.out.println("Le contrôleur est null");
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Suivi de Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
