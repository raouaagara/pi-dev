package main.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.InputStream;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;

import java.io.File;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javafx.stage.FileChooser;
import java.io.File;


import javafx.scene.image.ImageView;
import com.google.zxing.WriterException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import javafx.stage.FileChooser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Label;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.Paragraph;



import javafx.application.Platform;


import javafx.stage.Stage;
import java.util.ArrayList;  // Ajouter cet import en haut du fichier
import java.util.List;        // Si tu utilises List au lieu de ArrayList


import java.io.File;

import services.*;
import javafx.geometry.Insets;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.stage.FileChooser;
import java.io.File;
import models.QRCodeGenerator;

import models.Category;
import models.Equipement;
import tools.MyDataBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import javafx.scene.control.TableCell;

import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;


import javafx.scene.control.Alert;
import java.io.IOException;


import javafx.event.ActionEvent;

import java.util.Comparator;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;

import java.time.LocalDate;

import javafx.scene.chart.PieChart;
import javafx.animation.SequentialTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;


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
    private ImageView imageView;
    @FXML
    private ComboBox<Category> categoryFilter;
    @FXML
    private ImageView runnerImage;

    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TableView<Equipement> tableEquipements;
    private static final String API_KEY = "9a86d831a9694cec8ed30bf5ccf70878";


    @FXML
    private Label temperatureLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private TextField temperatureField;
    @FXML
    private TextField weatherDescriptionField;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private TableColumn<Equipement, String> colNom;
    @FXML
    private TableColumn<Equipement, String> colCategorie;
    @FXML
    private TableColumn<Equipement, Float> colPrix;
    @FXML
    private TableColumn<Equipement, Boolean> colDisponibilite;
    @FXML
    private Button btnSupprimer, btnModifier, btnSortByPriceDesc, btnExportPDF,generateQRCodeButton;;
    // Ajout du bouton pour trier par prix décroissant,
    @FXML
    private TableColumn<Equipement, String> imageColumn;
    @FXML
    private TableColumn<Equipement, String> colDescription;

    // Label pour la température

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
        tableEquipements.getColumns().setAll(colNom, colCategorie, colPrix, colDisponibilite, colDescription, imageColumn);

        dateAdded.setValue(LocalDate.now());
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
        // Vérification des champs requis
        if (name.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom ne peut pas être vide.");
            return;
        }
        if (description.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas être vide.");
            return;
        }
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une catégorie.");
            return;
        }

        // Vérification du prix
        if (price.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix ne peut pas être vide.");
            return;
        }

        float priceValue;
        try {
            priceValue = Float.parseFloat(price.getText().trim());
            if (priceValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un prix valide (ex: 10.5).");
            return;
        }

        // Vérification de l'image
        if (image.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une image.");
            return;
        }

        // Vérification de l'ID partenaire
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

        // Préparer la requête d'ajout à la base de données
        int categoryId = categoryComboBox.getSelectionModel().getSelectedItem().getId();
        String sql = "INSERT INTO equipement (name, description, categoryId, price, image, availability, dateAdded, partnerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name.getText());
            statement.setString(2, description.getText());
            statement.setInt(3, categoryId);
            statement.setFloat(4, Float.parseFloat(price.getText()));
            statement.setString(5, image.getText());
            statement.setBoolean(6, availability.isSelected());
            statement.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            statement.setInt(8, Integer.parseInt(partnerId.getText()));

            // Exécution de la requête d'ajout
            statement.executeUpdate();

            // Récupérer l'ID généré pour l'équipement


            // Afficher un message de succès et réinitialiser les champs
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement ajouté avec succès !");
            clearFields();
            loadEquipements();  // Rafraîchir la liste des équipements

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

    private List<String> getUtilisateursEmails() {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email FROM user"; // Assurez-vous que la colonne 'email' existe bien

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
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
                            Image image = chargerImage(imagePath);
                            imageView.setImage(image);
                        } catch (Exception e) {
                            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                            imageView.setImage(chargerImageParDefaut());
                        }

                        setGraphic(imageView);
                    }

                    private Image chargerImage(String imagePath) {
                        try {
                            System.out.println("Tentative de chargement de l'image : " + imagePath);

                            File file = new File(imagePath.replace("file:/", ""));
                            if (file.exists() && file.isFile()) {
                                System.out.println("Image trouvée : " + file.getAbsolutePath());
                                return new Image(file.toURI().toString());
                            }

                            // Vérifier dans resources/images/
                            URL imageUrl = getClass().getResource("/images/" + imagePath);
                            if (imageUrl != null) {
                                return new Image(imageUrl.toExternalForm());
                            }

                            System.err.println("Image non trouvée : " + imagePath);
                        } catch (Exception e) {
                            System.err.println("Erreur de chargement d'image : " + e.getMessage());
                        }

                        return chargerImageParDefaut();
                    }

                    private Image chargerImageParDefaut() {
                        try {
                            String defaultPath = "file:/C:/Users/Lenovo/Downloads/default.png";
                            File defaultFile = new File(defaultPath.replace("file:/", ""));
                            if (defaultFile.exists()) {
                                System.out.println("Image par défaut trouvée !");
                                return new Image(defaultFile.toURI().toString());
                            }

                            InputStream defaultImageStream = getClass().getResourceAsStream("/images/default.png");
                            if (defaultImageStream != null) {
                                System.out.println("Image par défaut chargée depuis resources !");
                                return new Image(defaultImageStream);
                            }

                            System.err.println("Image par défaut introuvable !");
                            return new Image("https://via.placeholder.com/80"); // Image en ligne de secours

                        } catch (Exception e) {
                            System.err.println("Erreur de chargement de l'image par défaut : " + e.getMessage());
                            return null;
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
    private void handleExportPDF(ActionEvent event) {
        // Récupérer la scène à partir du bouton cliqué
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Remplace ceci par la vraie liste d'équipements que tu veux exporter
        List<Equipement> equipements = service.getAll();

        // Appeler la méthode d'exportation
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.handleExportPDF(stage, equipements);

    }

    public void handleUploadPhoto() {
        // Créer un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        // Ouvrir la fenêtre de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Afficher l'image sélectionnée dans l'ImageView
            Image img = new Image(selectedFile.toURI().toString());
            imageView.setImage(img);

            // Optionnel : Stocker le chemin du fichier dans le TextField 'image' (si vous en avez besoin pour la base de données)
            image.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            image.setText(file.toURI().toString()); // Stocke le chemin de l'image dans le champ texte
        }
    }

    public void afficherStatistiques() {
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Équipements");

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des Équipements");

        // Vérifier si le TableView n'est pas vide
        ObservableList<Equipement> equipements = tableEquipements.getItems();
        if (equipements.isEmpty()) {
            System.out.println("Aucune donnée disponible.");
            return;
        }

        // Compter les équipements disponibles et non disponibles
        int nbDispo = 0;
        int nbNonDispo = 0;
        int total = equipements.size();

        for (Equipement e : equipements) {
            if (e.isAvailable()) {
                nbDispo++;
            } else {
                nbNonDispo++;
            }
        }

        // Calculer les pourcentages
        double pourcentageDispo = (nbDispo * 100.0) / total;
        double pourcentageNonDispo = (nbNonDispo * 100.0) / total;

        // Ajouter les données au PieChart avec pourcentage
        PieChart.Data dispo = new PieChart.Data("Disponible (" + String.format("%.1f", pourcentageDispo) + "%)", nbDispo);
        PieChart.Data nonDispo = new PieChart.Data("Non Disponible (" + String.format("%.1f", pourcentageNonDispo) + "%)", nbNonDispo);

        pieChart.getData().addAll(dispo, nonDispo);

        // Appliquer les couleurs du graphique
        String couleurDisponible = "#007bff"; // Bleu clair
        String couleurNonDisponible = "#ff5555"; // Rouge clair

        dispo.getNode().setStyle("-fx-pie-color: " + couleurDisponible + ";");
        nonDispo.getNode().setStyle("-fx-pie-color: " + couleurNonDisponible + ";");

        // Appliquer une animation d'apparition progressive
        SequentialTransition sequentialTransition = new SequentialTransition();
        for (PieChart.Data data : pieChart.getData()) {
            ScaleTransition st = new ScaleTransition(Duration.millis(800), data.getNode());
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            st.setInterpolator(Interpolator.EASE_BOTH);
            sequentialTransition.getChildren().add(st);
        }

        sequentialTransition.play(); // Lancer l'animation

        VBox root = new VBox(pieChart);
        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ComboBox<Integer> equipementIdComboBox;  // ComboBox pour les IDs des équipements

    private Category getCategoryById(int categoryId) {
        Category category = null;
        String sql = "SELECT * FROM category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                category = new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer la catégorie : " + e.getMessage());
        }
        return category;
    }

    @FXML
    private void handleFilterById(ActionEvent Event) {
        Integer selectedEquipementId = equipementIdComboBox.getSelectionModel().getSelectedItem();

        if (selectedEquipementId == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un ID d'équipement.");
            return;
        }

        ObservableList<Equipement> filteredEquipements = FXCollections.observableArrayList();

        String sql = "SELECT * FROM equipement WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, selectedEquipementId);  // Passer l'ID sélectionné comme paramètre
            ResultSet resultSet = statement.executeQuery();

            // Si aucun résultat n'est trouvé, afficher une alerte
            if (!resultSet.next()) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun équipement trouvé avec cet ID.");
                return;
            }

            // Créer l'objet Equipement avec les résultats de la requête
            Equipement equipement = new Equipement(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    getCategoryById(resultSet.getInt("categoryId")),  // Récupérer l'objet Category
                    resultSet.getFloat("price"),
                    resultSet.getString("image"),
                    resultSet.getBoolean("availability"),
                    resultSet.getDate("dateAdded").toLocalDate(),
                    resultSet.getInt("partnerId")
            );

            filteredEquipements.add(equipement);  // Ajouter l'équipement trouvé à la liste

            tableEquipements.setItems(filteredEquipements);  // Mettre à jour la table avec les équipements filtrés

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de filtrer l'équipement : " + e.getMessage());
        }
    }

    private void runAnimation() {
        TranslateTransition run = new TranslateTransition(Duration.seconds(3), runnerImage);
        run.setByX(600);  // Déplacement horizontal de 600px
        run.setCycleCount(TranslateTransition.INDEFINITE); // Animation en boucle
        run.setAutoReverse(true); // Repart en arrière après l'aller

        run.play(); // Démarrer l'animation
    }


    public void getWeather(String city) {
        System.out.println("Fetching weather data for " + city);
        try {
            String apiKey = "9a86d831a9694cec8ed30bf5ccf70878";  // Remplace par ta clé API
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Utilisation de Jackson pour parser le JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.toString());

            // Extraction des informations météo
            JsonNode weather = jsonResponse.get("weather").get(0);
            String weatherDescription = weather.get("description").asText();
            JsonNode main = jsonResponse.get("main");
            double temperature = main.get("temp").asDouble();

            // Récupération de l'icône météo
            String iconCode = weather.get("icon").asText();
            String iconUrl;

            // Déterminer l'icône selon l'état de la météo
            if (iconCode.equals("01d")) {
                // Ciel clair (ensoleillé)
                iconUrl = "http://openweathermap.org/img/wn/01d@2x.png";
            } else if (iconCode.equals("02d") || iconCode.equals("03d") || iconCode.equals("04d")) {
                // Quelques nuages
                iconUrl = "http://openweathermap.org/img/wn/02d@2x.png";
            } else if (iconCode.equals("09d") || iconCode.equals("10d")) {
                // Pluie
                iconUrl = "http://openweathermap.org/img/wn/10d@2x.png";
            } else {
                // Par défaut pour d'autres types d'icônes
                iconUrl = "http://openweathermap.org/img/wn/01d@2x.png";
            }

            // Mise à jour des labels dans le thread JavaFX
            Platform.runLater(() -> {
                temperatureField.setText("Température : " + temperature + "°C");
                weatherDescriptionField.setText("Condition : " + weatherDescription);

                // Mettre à jour l'icône météo
                Image icon = new Image(iconUrl);
                weatherIcon.setImage(icon);
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving weather data");
        }
    }
    public void handleGetWeather(ActionEvent event) {
        // Récupérer la ville saisie dans le champ
        String city = cityLabel.getText().trim();  // Enlever les espaces superflus
        if (city.isEmpty()) {
            temperatureField.setText("Veuillez entrer une ville");
            weatherDescriptionField.setText("");
            return;
        }

        // Vérifie et retire un préfixe comme "Ville : " s'il existe
        if (city.startsWith("Ville : ")) {
            city = city.substring(7).trim();  // Retirer le préfixe "Ville : " (7 caractères)
        }

        // Appeler la méthode getWeather pour récupérer et afficher les données
        getWeather(city);
    }

    @FXML
    public void handleGenerateQRCode(ActionEvent event) {
        // Vérifier si un équipement est sélectionné
        Equipement selectedEquipement = tableEquipements.getSelectionModel().getSelectedItem();
        if (selectedEquipement == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un équipement.");
            return;
        }

        // 🔹 Utiliser le nom du produit pour générer l'URL
        String equipementName = selectedEquipement.getName();
        String qrData = QRCodeGenerator.generateProductUrl(equipementName);

        // Définir le chemin du fichier QR Code
        String qrPath = "qrcodes/equipment_" + equipementName.replaceAll("\\s+", "_") + ".png";

        try {
            // Générer le QR Code
            QRCodeGenerator.generateQRCode(qrData, qrPath, 300, 300);

            // Charger et afficher l'image du QR Code
            File qrCodeFile = new File(qrPath);
            if (qrCodeFile.exists()) {
                Image qrCodeImage = new Image(qrCodeFile.toURI().toString());
                ImageView qrCodeImageView = new ImageView(qrCodeImage);
                qrCodeImageView.setFitWidth(300);
                qrCodeImageView.setFitHeight(300);

                // Créer un VBox pour afficher l'image du QR Code
                VBox vbox = new VBox(qrCodeImageView);
                vbox.setAlignment(javafx.geometry.Pos.CENTER);

                // Créer une nouvelle fenêtre pour afficher le QR Code
                Stage qrStage = new Stage();
                qrStage.setTitle("QR Code de l'Équipement");
                qrStage.setScene(new Scene(vbox, 350, 350));
                qrStage.show();

                // 🔹 Effet d'apparition du QR Code
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), qrCodeImageView);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();

            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le QR code n'a pas pu être généré.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la génération du QR code : " + e.getMessage());
            e.printStackTrace();
        }
    }

}




























