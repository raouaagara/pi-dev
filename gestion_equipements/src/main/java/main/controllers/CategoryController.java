package main.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Category;
import services.CategoryService;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CategoryController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField iconField;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> iconColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private CategoryService categoryService;
    private ObservableList<Category> categoryList;
    private Category selectedCategory; // Stocker la catégorie sélectionnée

    public void initialize() {
        categoryService = new CategoryService();
        categoryList = FXCollections.observableArrayList();

        // Initialisation des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));

        // Charger les catégories
        loadCategories();



        // Gestion de la sélection dans le tableau
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCategory = newSelection;
                nameField.setText(selectedCategory.getName());
                iconField.setText(selectedCategory.getIcon());
                updateButton.setDisable(false); // Activer "Modifier"
                deleteButton.setDisable(false); // Activer "Supprimer"

                System.out.println("Catégorie sélectionnée : " + selectedCategory.getName()); // Debug
            } else {
                updateButton.setDisable(true);  // Désactiver si aucune sélection
                deleteButton.setDisable(true);
                System.out.println("Aucune catégorie sélectionnée"); // Debug
            }
        });
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryList.setAll(categories);
        categoryTable.setItems(categoryList);

        System.out.println("Catégories chargées : " + categories.size()); // Debug
    }

    @FXML
    private void addCategory() {
        String name = nameField.getText().trim();
        String icon = iconField.getText().trim();

        if (name.isEmpty() || icon.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Category newCategory = new Category(name, icon);
        categoryService.addCategory(newCategory);
        loadCategories();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie ajoutée !");
    }

    @FXML
    private void updateCategory() {
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune catégorie sélectionnée !");
            return;
        }

        String newName = nameField.getText().trim();
        String newIcon = iconField.getText().trim();

        if (newName.isEmpty() || newIcon.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les champs ne peuvent pas être vides !");
            return;
        }

        selectedCategory.setName(newName);
        selectedCategory.setIcon(newIcon);

        categoryService.updateCategory(selectedCategory);
        loadCategories();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie modifiée avec succès !");
    }

    @FXML
    private void deleteCategory() {
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez une catégorie à supprimer !");
            return;
        }

        categoryService.deleteCategory(selectedCategory.getId());
        loadCategories();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie supprimée !");
    }

    private void clearFields() {
        nameField.clear();
        iconField.clear();
        selectedCategory = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
