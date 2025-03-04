package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Notification;
import models.Reclamation;
import models.Status;
import models.User;
import services.ServiceNotification;
import services.ServiceReclamation;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherReclamationsClientController {

    @FXML
    private MenuButton notificationIcon;

    @FXML
    private TableView<Reclamation> reclamationTable;

    @FXML
    private TableColumn<Reclamation, String> titleCol;

    @FXML
    private TableColumn<Reclamation, String> eventCol;

    @FXML
    private TableColumn<Reclamation, String> statusCol;

    @FXML
    private TableColumn<Reclamation, Void> modifyCol;

    @FXML
    private TableColumn<Reclamation, Void> deleteCol;

    @FXML
    private TableColumn<Reclamation, Void> suivreCol;

    @FXML
    private TableColumn<Reclamation, Void> suivreCol1;

    @FXML
    private TableColumn<Reclamation, Void> suivreCol11;

    private User user;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    public void initialize() throws SQLException {
        User user = new User();
        user.setId(1);
        loadNotifications(user);


        // Lier les colonnes aux attributs de Reclamation
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));  // Title column binds to getTitle()
        eventCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getTitle()));  // Event column binds to event title
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));  // Status column binds to getStatus()

        loadReclamations();

        // Ajouter des boutons dans les colonnes
        addButtonToTable(modifyCol, "Modifier", this::loadModifyView);
        addButtonToTable(deleteCol, "Supprimer", this::supprimerReclamation);
        addButtonToTable(suivreCol, "Suivre", this::suivreReclamation);
        addButtonToTable(suivreCol1, "En cours", this::mettreEnCours);
        addButtonToTable(suivreCol11, "Informations", this::visualiserReclamation);
    }



    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = serviceReclamation.recupererAll();

            if (reclamations == null || reclamations.isEmpty()) {
                System.out.println("Aucune réclamation trouvée.");
                reclamationTable.getItems().clear();
                return;
            }

            reclamationTable.getItems().setAll(reclamations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mettreEnCours(Reclamation reclamation) {
        try {
            serviceReclamation.mettreAJourStatut(reclamation); // Assuming this method changes the status in the DB
            reclamation.setStatus(Status.EN_COURS); // Local update of status
            reclamationTable.refresh(); // Refresh the table to show the updated status
            loadReclamations(); // Reload the table to ensure all rows are updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable(TableColumn<Reclamation, Void> column, String buttonText, java.util.function.Consumer<Reclamation> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button(buttonText);

            {
                button.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation != null) {
                        action.accept(reclamation);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }

    private void supprimerReclamation(Reclamation reclamation) {
        try {
            serviceReclamation.supprimer(reclamation.getId());
            reclamationTable.getItems().remove(reclamation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadModifyView(Reclamation reclamation) {
        try {
            // Fetch the latest status from the database using the reclamation ID
            Reclamation latestReclamation = serviceReclamation.recupererParId(reclamation.getId());
            System.out.println(latestReclamation);

            // Check if the status is EN_COURS or RESOLU before opening the modify view
            if (latestReclamation.getStatus() == Status.EN_COURS) {
                // Show alert if the status is EN_COURS
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Modification Impossible");
                alert.setContentText("Cette réclamation ne peut pas être modifiée car elle est en cours de traitement.");
                alert.showAndWait();
                return;  // Prevent opening the modify view
            }

            if (latestReclamation.getStatus() == Status.RESOLU) {
                // Show alert if the status is RESOLU
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Modification Impossible");
                alert.setContentText("Cette réclamation ne peut pas être modifiée car elle est résolue.");
                alert.showAndWait();
                return;  // Prevent opening the modify view
            }

            // If the status is EN_ATTENTE (or other statuses), open the modify view as usual
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReclamation.fxml"));
            Parent root = loader.load();
            ModifierReclamationController controller = loader.getController();
            controller.initData(latestReclamation);  // Use the latestReclamation to pass to the controller
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réclamation");
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }



    private void suivreReclamation(Reclamation reclamation) {
        System.out.println("Suivi de la réclamation : " + reclamation.getTitle());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SuiviReclamation.fxml"));
            Parent root = loader.load();
            SuiviReclamationController controller = loader.getController();
            controller.initData(reclamation);  // Assuming you need to pass the Reclamation data
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Suivi de la Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action to open the "Informations" view
    private void visualiserReclamation(Reclamation reclamation) {
        System.out.println("Visualiser la réclamation : " + reclamation.getTitle());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamation.fxml"));
            Parent root = loader.load();
            AfficherReclamationController controller = loader.getController();
            controller.initData(reclamation);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Informations Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotifications(User user) throws SQLException {
        ServiceNotification serviceNotification = new ServiceNotification();
        List<Notification> notifications = serviceNotification.recupererParUtilisateur(user);
        notificationIcon.getItems().clear();

        if (notifications.isEmpty()) {
            MenuItem noNotif = new MenuItem("Aucune notification");
            noNotif.setDisable(true);
            notificationIcon.getItems().add(noNotif);
        } else {
            for (Notification notif : notifications) {
                MenuItem item = new MenuItem(notif.getTitle());
                notificationIcon.getItems().add(item);
            }
        }
    }

}
