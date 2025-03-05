package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Events;
import services.ServicesEvents;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BackController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Events> eventsTable;
    @FXML
    private TableColumn<Events, String> titleColumn;
    @FXML
    private TableColumn<Events, String> descriptionColumn;
    @FXML
    private TableColumn<Events, String> locationColumn;
    @FXML
    private TableColumn<Events, Date> startDateColumn;
    @FXML
    private TableColumn<Events, Date> endDateColumn;
    @FXML
    private TableColumn<Events, String> imageColumn;
    @FXML
    private TableColumn<Events, Void> actionsColumn;

    private ServicesEvents servicesEvents;

    public void initialize() {
        servicesEvents = new ServicesEvents();

        // Configurer les colonnes de la table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setCellFactory(new ImageTableCellFactory());

        actionsColumn.setCellFactory(new ActionsTableCellFactory());

        // Charger les événements
        loadEvents();
    }

    private void loadEvents() {
        try {
            List<Events> events = servicesEvents.recuperer();
            eventsTable.getItems().setAll(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String title = searchField.getText();
        try {
            List<Events> events = servicesEvents.rechercherParNom(title);
            eventsTable.getItems().setAll(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSortByDate() {
        try {
            List<Events> events = servicesEvents.trierParDate();
            eventsTable.getItems().setAll(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddEvent.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouvel événement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ImageTableCellFactory implements Callback<TableColumn<Events, String>, TableCell<Events, String>> {
        @Override
        public TableCell<Events, String> call(TableColumn<Events, String> param) {
            return new TableCell<Events, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        try {
                            String imagePath = "/images/" + item;
                            System.out.println("Chemin de l'image : " + imagePath);
                            String imageUrl = getClass().getResource(imagePath).toString();
                            imageView.setImage(new Image(imageUrl));
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                            setGraphic(null);
                        }
                    }
                }
            };
        }
    }

    private class ActionsTableCellFactory implements Callback<TableColumn<Events, Void>, TableCell<Events, Void>> {
        @Override
        public TableCell<Events, Void> call(TableColumn<Events, Void> param) {
            return new TableCell<Events, Void>() {
                private final Button editButton = new Button("Modifier");
                private final Button deleteButton = new Button("Supprimer");

                {
                    editButton.setOnAction(event -> {
                        Events eventToEdit = getTableView().getItems().get(getIndex());
                        try {
                            System.out.println("Chargement du formulaire de modification...");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditEvent.fxml"));
                            Parent root = loader.load();
                            System.out.println("Formulaire chargé avec succès.");

                            EditEventController controller = loader.getController();
                            controller.setEventToEdit(eventToEdit);

                            Stage stage = new Stage();
                            stage.setTitle("Modifier un événement");
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.err.println("Erreur lors du chargement du formulaire : " + e.getMessage());
                        }
                    });

                    deleteButton.setOnAction(event -> {
                        Events eventToDelete = getTableView().getItems().get(getIndex());

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmer la suppression");
                        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet événement ?");
                        alert.setContentText("Cette action est irréversible.");

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            try {
                                servicesEvents.supprimer(eventToDelete.getTitle());

                                getTableView().getItems().remove(eventToDelete);

                                System.out.println("Événement supprimé avec succès !");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(editButton, deleteButton);
                        buttons.setSpacing(5);
                        setGraphic(buttons);
                    }
                }
            };
        }
    }
}