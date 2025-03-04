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
    private TableColumn<Reclamation, Void> infoCol;

    private User user;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    public void initialize() throws SQLException {
        User user = new User();
        user.setId(1);
        loadNotifications(user);

        // Lier les colonnes aux attributs de Reclamation
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        eventCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getTitle()));
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        loadReclamations();

        // Ajouter des boutons dans les colonnes
        addButtonToTable(modifyCol, "Modifier", this::loadModifyView);
        addButtonToTable(deleteCol, "Supprimer", this::supprimerReclamation);
        addButtonToTable(infoCol, "Informations", this::visualiserReclamation);
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
            Reclamation latestReclamation = serviceReclamation.recupererParId(reclamation.getId());

            if (latestReclamation.getStatus() == Status.EN_COURS || latestReclamation.getStatus() == Status.RESOLU) {
                showAlert(Alert.AlertType.INFORMATION, "Modification Impossible", "Cette réclamation ne peut pas être modifiée car elle est en cours de traitement ou résolue.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReclamation.fxml"));
            Parent root = loader.load();
            ModifierReclamationController controller = loader.getController();
            controller.initData(latestReclamation);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réclamation");
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void visualiserReclamation(Reclamation reclamation) {
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
