package be.esi.prj.game_2048;

/**
 * Classe principale qui lance l'application JavaFX.
 * Cette classe est responsable de:
 * - Charger l'écran d'accueil
 * - Configurer la fenêtre principale
 * - Permettre la transition vers le jeu
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Game2048 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charge le fichier FXML de l'écran d'accueil au lieu du jeu directement
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/welcome.fxml")));

        // Création de la scène avec dimensions optimisées pour l'écran d'accueil
        Scene scene = new Scene(root, 600, 800);

        primaryStage.setTitle("2048");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}