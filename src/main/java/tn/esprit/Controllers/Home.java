package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.Entity.User;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;


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

    @FXML
    private ChoiceBox<String> TrierFirstNameLastname;
    @FXML
    private ChoiceBox<String> TrierRole;





    private final UserService userService = new UserService();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        afficherUtilisateurs();
        setupSearchFilter();
        setupChoiceBoxes();

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

    private void setupChoiceBoxes() {
        TrierFirstNameLastname.setItems(FXCollections.observableArrayList("FirstName", "LastName"));
        TrierFirstNameLastname.setValue("FirstName");
        TrierFirstNameLastname.setOnAction(event -> trierUtilisateurs());

        TrierRole.setItems(FXCollections.observableArrayList("Tous", "Admin", "Client", "Partner"));
        TrierRole.setValue("Tous"); // Valeur par défaut
        TrierRole.setOnAction(event -> trierUtilisateurs());
    }

    private void trierUtilisateurs() {
        String selectedSort = TrierFirstNameLastname.getValue();
        String selectedRole = TrierRole.getValue();

        List<User> users = userService.getAllUsers();

        if (!selectedRole.equals("Tous")) {
            users.removeIf(user -> !user.getRole().equalsIgnoreCase(selectedRole));
        }

        users.sort((u1, u2) -> {
            if (selectedSort.equals("FirstName")) {
                return u1.getFirstname().compareToIgnoreCase(u2.getFirstname());
            } else {
                return u1.getLastname().compareToIgnoreCase(u2.getLastname());
            }
        });

        userList.setAll(users);
    }

    @FXML
    private void statistique(ActionEvent event) {
        Map<String, Integer> stats = userService.getUserStatisticsByRole();

        int totalUsers = stats.values().stream().mapToInt(Integer::intValue).sum();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Répartition des rôles des utilisateurs\nTotal utilisateurs: " + totalUsers);

        Stage stage = new Stage();
        Scene scene = new Scene(pieChart, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Statistiques des utilisateurs");
        stage.show();
    }


    @FXML
    private void pdf(ActionEvent event) {
        List<User> users = userService.getAllUsers();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {


                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 750);
                    contentStream.showText("Liste des Utilisateurs : ");
                    contentStream.endText();

                    float yPosition = 720;
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

                    String[] roles = {"Admin", "Client", "Partner"};
                    for (String role : roles) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText(role + "s");
                        contentStream.endText();
                        yPosition -= 20;

                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        for (User user : users) {
                            if (user.getRole().equalsIgnoreCase(role)) {
                                contentStream.beginText();
                                contentStream.newLineAtOffset(50, yPosition);
                                contentStream.showText(user.getId() + " - " + user.getFirstname() + " " + user.getLastname() + " - " + user.getEmail());
                                contentStream.endText();
                                yPosition -= 20;
                            }
                        }
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                        yPosition -= 10; // Espace entre les rôles
                    }
                }

                document.save(file);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "PDF généré avec succès");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le PDF");
            }
        }
    }


}
