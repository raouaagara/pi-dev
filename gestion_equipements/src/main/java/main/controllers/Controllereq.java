package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Category;
import models.Equipement;
import services.serviceEquipement;
import tools.MyDataBase;

import java.sql.*;
import java.time.LocalDate;

public class Controllereq {

    @FXML
    private TextField name, description, price, image, partnerId;
    @FXML
    private CheckBox availability;
    @FXML
    private DatePicker dateAdded;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TableView<Equipement> tableEquipements;
    @FXML
    private TableColumn<Equipement, Integer> colId;
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

    private Connection connection;
    private serviceEquipement service;
    private Equipement equipementSelectionne;

    public Controllereq() {
        this.connection = MyDataBase.getInstance().getConnection();
        this.service = new serviceEquipement();
    }

    @FXML
    private void initialize() {
        setupTable();
        loadEquipements();
        loadCategories();

        tableEquipements.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                equipementSelectionne = newSelection;
                populateFields(equipementSelectionne);
            }
        });
    }

    @FXML
    private void handleAdd() {
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une catégorie.");
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

        equipementSelectionne.setName(name.getText());
        equipementSelectionne.setDescription(description.getText());
        equipementSelectionne.setCategoryId(selectedCategory.getId());
        equipementSelectionne.setPrice(Float.parseFloat(price.getText()));
        equipementSelectionne.setImage(image.getText());
        equipementSelectionne.setAvailability(availability.isSelected());
        equipementSelectionne.setDateAdded(dateAdded.getValue());
        equipementSelectionne.setPartnerId(Integer.parseInt(partnerId.getText()));

        service.modifier(equipementSelectionne);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement modifié avec succès !");
        clearFields();
        loadEquipements();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDisponibilite.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    @FXML
    private void loadEquipements() {
        ObservableList<Equipement> equipementList = FXCollections.observableArrayList(service.getAll());
        tableEquipements.setItems(equipementList);
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
        availability.setSelected(equipement.getAvailability());
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
}

