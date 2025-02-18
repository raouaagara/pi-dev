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

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventCol.setCellValueFactory(param -> {
            Event event = param.getValue().getEvent();  // Get the Event object from the Reclamation
            if (event != null && event.getTitle() != null) {
                return new SimpleStringProperty(event.getTitle());
            } else {
                return new SimpleStringProperty("No event");  // Default text when event is null or has no title
            }
        });



        try {
            reclamationTable.getItems().addAll(serviceReclamation.recupererAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modifyCol.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

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

        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

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
