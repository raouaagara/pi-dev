package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger la page de connexion (première vue)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Configurer la scène
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Connexion"); // Titre de la fenêtre
            //primaryStage.setResizable(false); // Empêcher le redimensionnement de la fenêtre (optionnel)
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la vue de connexion.");
        }
    }

    public static void main(String[] args) {
        // Lancer l'application JavaFX
        launch(args);
    }
}