package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class main extends Application { // Renommé en 'Main'
    @Override
    public void start(Stage primaryStage) {
        try {
            // Vérification et chargement du fichier FXML
            URL fxmlLocation = getClass().getResource("/fxml/Equipement.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("Fichier FXML introuvable ! Vérifiez son emplacement.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            primaryStage.setTitle("Gestion des équipements");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
