package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.util.List;

public class Home {

    @FXML
    private TableView<User> tableviewUser;
    @FXML
    private TableColumn<User, Integer> idUser;
    @FXML
    private TableColumn<User, String> fistName;
    @FXML
    private TableColumn<User, String> LastName;
    @FXML
    private TableColumn<User, String> avatar;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> password;
    @FXML
    private TableColumn<User, String> joinDate;
    @FXML
    private TableColumn<User, String> role;
    @FXML
    private TextField Recherche_User;
    @FXML
    private Button logOut;

    private final UserService userService = new UserService();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        afficherUtilisateurs();
        setupSearchFilter();
    }

    private void afficherUtilisateurs() {
        List<User> users = userService.getAllUsers();
        userList = FXCollections.observableArrayList(users);

        idUser.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        fistName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstname()));
        LastName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastname()));
        email.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        password.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassword()));
        joinDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJoinDate().toString()));
        role.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty((String) cellData.getValue().getRole()));

        avatar.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAvatar()));

        avatar.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String avatarPath, boolean empty) {
                        super.updateItem(avatarPath, empty);
                        if (empty || avatarPath == null) {
                            setGraphic(null);
                        } else {
                            Image image = new Image("file:" + avatarPath);
                            imageView.setImage(image);
                            imageView.setFitHeight(40);
                            imageView.setFitWidth(40);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });


        tableviewUser.setItems(userList);
    }

    @FXML
    private void ajout(ActionEvent event) {
        System.out.println("Ajout d'un utilisateur - Redirection vers le formulaire");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutUser.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter un utilisateur");
            stage.show();

            Stage currentStage = (Stage) tableviewUser.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors du chargement de l'interface AjoutUser.fxml");
        }
    }

    @FXML
    private void supprimer(ActionEvent event) {
        User selectedUser = tableviewUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.deleteUser(selectedUser.getId());
            userList.remove(selectedUser);
            System.out.println("✅ Utilisateur supprimé !");
        } else {
            System.out.println("❌ Veuillez sélectionner un utilisateur !");
        }
    }

    @FXML
    private void modifier(ActionEvent event) {
        User selectedUser = tableviewUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            System.out.println("Modification de l'utilisateur : " + selectedUser.getFirstname());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifUser.fxml"));
                Parent root = loader.load();

                ModifierUser modifierUserController = loader.getController();

                modifierUserController.setUser(selectedUser);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier un utilisateur");
                stage.show();

                Stage currentStage = (Stage) tableviewUser.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Erreur lors du chargement de l'interface ModifierUser.fxml");
            }
        } else {
            System.out.println("❌ Veuillez sélectionner un utilisateur !");
        }
    }


    private void setupSearchFilter() {
        Recherche_User.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                tableviewUser.setItems(userList);
            } else {
                ObservableList<User> filteredList = FXCollections.observableArrayList();
                for (User user : userList) {
                    if (user.getFirstname().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getLastname().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(user);
                    }
                }
                tableviewUser.setItems(filteredList);
            }
        });
    }

    public void update(ActionEvent actionEvent) {
        User selectedUser = tableviewUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            System.out.println("Modification de l'utilisateur : " + selectedUser.getFirstname());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifUser.fxml"));
                Parent root = loader.load();

                ModifierUser modifierUserController = loader.getController();

                modifierUserController.setUser(selectedUser);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier un utilisateur");
                stage.show();

                Stage currentStage = (Stage) tableviewUser.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Erreur lors du chargement de l'interface ModifierUser.fxml");
            }
        } else {
            System.out.println("❌ Veuillez sélectionner un utilisateur !");
        }
    }

    public void logOut(ActionEvent actionEvent) {
        loadPage("/SignIn.fxml", "Login");

    }


    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page !");
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
