package main.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Category;
import models.Equipement;
import services.serviceEquipement;
import tools.MyDataBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.*;
import java.time.LocalDate;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.geometry.Pos;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import models.Notifications;
import javafx.fxml.FXML;
import java.io.FileOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import java.net.URL;
import java.util.Comparator;
import models.Equipement;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Alert;


 // Vérifie que ce package correspond à la structure de ton projet

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;




public class Controllereq {

    @FXML
    private TextField name, description, price, image, partnerId;
    @FXML
    private CheckBox availability;
    @FXML
    private DatePicker dateAdded;
    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TableView<Equipement> tableEquipements;

    @FXML
    private TableColumn<Equipement, String> colNom;
    @FXML
    private TableColumn<Equipement, String> colCategorie;
    @FXML
    private TableColumn<Equipement, Float> colPrix;
    @FXML
    private TableColumn<Equipement, Boolean> colDisponibilite;
    @FXML
    private Button btnSupprimer, btnModifier, btnSortByPriceDesc;  // Ajout du bouton pour trier par prix décroissant
    @FXML
    private TableColumn<Equipement, String> imageColumn;
    @FXML
    private TableColumn<Equipement, String> colDescription;


    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultList;



    private Connection connection;
    private serviceEquipement service;
    private Equipement equipementSelectionne;
    private ObservableList<Equipement> originalEquipementsList;


    public Controllereq() {
        this.connection = MyDataBase.getInstance().getConnection();
        this.service = new serviceEquipement();
    }

    @FXML
    private void initialize() {
        setupTable();
        tableEquipements.getColumns().setAll( colNom, colCategorie, colPrix, colDisponibilite, colDescription, imageColumn);


        loadEquipements();
        loadCategories();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        tableEquipements.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                equipementSelectionne = newSelection;
                populateFields(equipementSelectionne);
            }
            afficherImagesDansTable();
        });
    }

    @FXML
    private void handleAdd() {
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une catégorie.");
            return;
        }


            // Vérifier que le nom n'est pas vide
        if (name.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom ne peut pas être vide.");
                return;
            }
        if (description.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas être vide.");
            return;
        }
            // Vérifier que le prix est valide
        // Vérification du prix (ne doit pas être vide et doit être un nombre positif)
        if (price.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix ne peut pas être vide.");
            return;
        }

        float priceValue; // Déclaration de la variable priceValue
        try {
            priceValue = Float.parseFloat(price.getText().trim()); // Convertit le texte en float
            if (priceValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un prix valide (ex: 10.5).");
            return;
        }



        // Vérifier que l'image est renseignée
        if (image.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une image.");
                return;
            }
        if (dateAdded.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date d'ajout.");
            return;
        }

        // Vérification de l'ID partenaire (doit être un entier positif)
        int partnerIdValue;
        try {
            partnerIdValue = Integer.parseInt(partnerId.getText());
            if (partnerIdValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID partenaire doit être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un ID partenaire valide.");
            return;
        }

        int categoryId = categoryComboBox.getSelectionModel().getSelectedItem().getId();
        String sql = "INSERT INTO equipement (name, description, categoryId, price, image, availability, dateAdded, partnerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name.getText());
            statement.setString(2, description.getText());
            statement.setInt(3, categoryId);
            statement.setFloat(4, Float.parseFloat(price.getText()));
            statement.setString(5, image.getText());
            statement.setBoolean(6, availability.isSelected());
            statement.setDate(7, Date.valueOf(dateAdded.getValue()));
            statement.setInt(8, Integer.parseInt(partnerId.getText()));

            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement ajouté avec succès !");
            clearFields();
            loadEquipements();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Problème : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (equipementSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun équipement sélectionné", "Veuillez sélectionner un équipement à modifier.");
            return;
        }

        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une catégorie.");
            return;
        }

        try {
            equipementSelectionne.setName(name.getText());
            equipementSelectionne.setDescription(description.getText());
            equipementSelectionne.setCategory(selectedCategory); // ✅ Utilisation correcte d'un objet Category
            equipementSelectionne.setPrice(Float.parseFloat(price.getText()));
            equipementSelectionne.setImage(image.getText());
            equipementSelectionne.setAvailability(availability.isSelected()); // ✅ Remplace `setAvailable()`
            equipementSelectionne.setDateAdded(dateAdded.getValue());
            equipementSelectionne.setPartnerId(Integer.parseInt(partnerId.getText()));

            service.modifier(equipementSelectionne);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement modifié avec succès !");
            clearFields();
            loadEquipements();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour le prix et l'ID partenaire.");
        }
    }


    @FXML
    private void handleDelete() {
        Equipement equipementSelectionne = tableEquipements.getSelectionModel().getSelectedItem();

        if (equipementSelectionne != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer l'équipement ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    service.supprimer(equipementSelectionne.getId());
                    tableEquipements.getItems().remove(equipementSelectionne);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement supprimé !");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun équipement sélectionné", "Veuillez sélectionner un équipement à supprimer.");
        }
    }

    @FXML
    private void handleSortByPriceDesc() {
        ObservableList<Equipement> equipementList = FXCollections.observableArrayList(service.getEquipementsSortedByPriceDesc());
        tableEquipements.setItems(equipementList);
    }

    private void setupTable() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Éviter NullPointerException pour la catégorie
        colCategorie.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCategory() != null) {
                return new SimpleStringProperty(cellData.getValue().getCategory().getName());
            } else {
                return new SimpleStringProperty("Aucune catégorie");
            }
        });

        // ✅ Utilisation correcte de `availabilityProperty()`
        colDisponibilite.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty());
        colDisponibilite.setCellFactory(CheckBoxTableCell.forTableColumn(colDisponibilite));
    }


    @FXML
    private void loadEquipements() {
        ObservableList<Equipement> equipementList = FXCollections.observableArrayList(service.getAll());

        for (Equipement eq : equipementList) {
            if (eq.getDescription() == null || eq.getDescription().trim().isEmpty()) {
                eq.setDescription("Aucune description disponible");
            }
        }

        tableEquipements.setItems(equipementList);
        afficherImagesDansTable();
    }


    @FXML
    private void loadCategories() {
        String sql = "SELECT id, name FROM category";
        ObservableList<Category> categories = FXCollections.observableArrayList();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }
            categoryComboBox.setItems(categories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les catégories : " + e.getMessage());
        }
    }

    private void populateFields(Equipement equipement) {
        name.setText(equipement.getName());
        description.setText(equipement.getDescription());
        price.setText(String.valueOf(equipement.getPrice()));
        image.setText(equipement.getImage());
        availability.setSelected(equipement.isAvailable()); // ✅ Correction ici
        dateAdded.setValue(equipement.getDateAdded());
        partnerId.setText(String.valueOf(equipement.getPartnerId()));
    }


    private void clearFields() {
        name.clear();
        description.clear();
        price.clear();
        image.clear();
        partnerId.clear();
        availability.setSelected(false);
        dateAdded.setValue(null);
        categoryComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    private void openCategoryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CategoryView.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestion des Catégories");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afficherImagesDansTable() {
        imageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));

        imageColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Equipement, String> call(TableColumn<Equipement, String> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    {
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(80);
                    }

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);

                        if (empty || imagePath == null || imagePath.trim().isEmpty()) {
                            setGraphic(null);
                            return;
                        }

                        try {
                            String fullPath = "/images/" + imagePath; // Correction du chemin
                            URL imageUrl = getClass().getResource(fullPath);

                            if (imageUrl != null) {
                                imageView.setImage(new Image(imageUrl.toExternalForm()));
                            } else {
                                System.err.println("Image introuvable : " + fullPath);
                                chargerImageParDefaut();
                            }
                        } catch (Exception e) {
                            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                            chargerImageParDefaut();
                        }

                        setGraphic(imageView);
                    }

                    private void chargerImageParDefaut() {
                        URL defaultImageUrl = getClass().getResource("/images/default.png");
                        if (defaultImageUrl != null) {
                            imageView.setImage(new Image(defaultImageUrl.toExternalForm()));
                        } else {
                            System.err.println("Image par défaut introuvable !");
                        }
                    }
                };
            }
        });


    }
    @FXML
    private void trierParPrixAscendant() {
        tableEquipements.getItems().sort(Comparator.comparing(Equipement::getPrice));
    }
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim(); // Récupère et nettoie le texte

        // Si la recherche est vide, on affiche toute la liste
        if (searchText.isEmpty()) {
            tableEquipements.setItems(FXCollections.observableArrayList(service.getAll()));
            return;
        }

        // Filtrer la liste en gardant uniquement les équipements qui contiennent le texte recherché
        ObservableList<Equipement> filteredList = FXCollections.observableArrayList(
                service.getAll().stream()
                        .filter(eq -> eq.getName().toLowerCase().contains(searchText) ||
                                eq.getDescription().toLowerCase().contains(searchText))
                        .toList()
        );

        tableEquipements.setItems(filteredList); // Met à jour la TableView
    }
    @FXML

    public void sendNotification() {
        String recipient = emailField.getText();

        if (recipient.isEmpty() || !recipient.contains("@")) {
            System.out.println("Email invalide !");
            return;
        }

        // ✅ Un seul compte expéditeur configuré
        final String senderEmail = "ton_email@gmail.com";
        final String senderPassword = "ton_mot_de_passe"; // Mot de passe d'application

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // Expéditeur fixe
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Notification");
            message.setText("Votre notification a été envoyée avec succès !");

            Transport.send(message);
            System.out.println("Email envoyé à " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

