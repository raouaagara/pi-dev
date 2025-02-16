package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Category;
import services.CategoryService;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

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

    private CategoryService categoryService;
    private ObservableList<Category> categoryList;

    public void initialize() {
        categoryService = new CategoryService();
        categoryList = FXCollections.observableArrayList();

        // Initialisation des colonnes avec les bonnes valeurs
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));

        // Charger les catégories dans le tableau
        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryList.setAll(categories);
        categoryTable.setItems(categoryList);
    }

    @FXML
    private void addCategory() {
        String name = nameField.getText();
        String icon = iconField.getText();

        if (name.isEmpty() || icon.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Category newCategory = new Category(name, icon); // Pas besoin d'ID car AUTO_INCREMENT
        categoryService.addCategory(newCategory);
        loadCategories();
        nameField.clear();
        iconField.clear();
    }

    @FXML
    private void deleteCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            categoryService.deleteCategory(selectedCategory.getId());
            loadCategories();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sélectionnez une catégorie à supprimer !");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
