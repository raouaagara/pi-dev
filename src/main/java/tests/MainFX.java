package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    private static BorderPane contentArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeReclamation.fxml"));
        Parent root = loader.load();

        // Store reference to content area
        contentArea = (BorderPane) ((BorderPane) root).lookup("#contentArea");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Réclamations");
        primaryStage.show();
    }

    // Centralized method to load FXML into content area
    public static void loadFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/" + fxmlFile));
            Parent newContent = loader.load();
            contentArea.setCenter(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
