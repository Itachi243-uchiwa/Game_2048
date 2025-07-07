package be.esi.prj.game_2048.controller;

import be.esi.prj.game_2048.view.TileView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

/**
 * Contrôleur de l'écran d'accueil du jeu 2048.
 */
public class WelcomeController {
    @FXML private Label developerNameLabel;
    @FXML private GridPane demoGrid;

    private TileView[][] demoTiles;
    private Timeline demoAnimation;
    private final Random random = new Random();
    private final int[] possibleValues = {2, 4, 8, 16, 32, 64, 128};

    /**
     * Initialise le contrôleur, configure la grille de démonstration et démarre l'animation.
     */
    @FXML
    public void initialize() {
        String developerName = "Martinez Muzela";
        developerNameLabel.setText(developerName);
        setupDemoGrid();
        startDemoAnimation();
    }

    /**
     * Configure la grille de démonstration 2x2.
     */
    private void setupDemoGrid() {
        demoTiles = new TileView[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                TileView tile = new TileView();
                tile.setPrefSize(80, 80);
                tile.setValue(getRandomTileValue());
                demoGrid.add(tile, col, row);
                demoTiles[row][col] = tile;
            }
        }
    }

    /**
     * Démarre l'animation de démonstration des tuiles.
     */
    private void startDemoAnimation() {
        demoAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1.5), e -> animateTiles())
        );
        demoAnimation.setCycleCount(Timeline.INDEFINITE);
        demoAnimation.play();
    }

    /**
     * Anime une séquence aléatoire de mouvements de tuiles.
     */
    private void animateTiles() {
        int operation = random.nextInt(3);
        switch (operation) {
            case 0 -> simulateTileMerge();
            case 1 -> simulateNewTile();
            case 2 -> randomizeAllTiles();
        }
    }

    /**
     * Simule la fusion de deux tuiles adjacentes.
     */
    private void simulateTileMerge() {
        int row = random.nextInt(2);
        int col = random.nextInt(2);
        int adjRow = random.nextBoolean() ? row : (row + 1) % 2;
        int adjCol = adjRow == row ? (col + 1) % 2 : col;
        int value = getRandomTileValue();
        demoTiles[row][col].setValue(value);
        demoTiles[adjRow][adjCol].setValue(value);
        Timeline mergeTimeline = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    demoTiles[adjRow][adjCol].setValue(0);
                    demoTiles[row][col].setValue(value * 2);
                })
        );
        mergeTimeline.play();
    }

    /**
     * Simule l'apparition d'une nouvelle tuile.
     */
    private void simulateNewTile() {
        int row = random.nextInt(2);
        int col = random.nextInt(2);
        demoTiles[row][col].setValue(0);
        Timeline appearTimeline = new Timeline(
                new KeyFrame(Duration.millis(500), e ->
                        demoTiles[row][col].setValue(getRandomTileValue())
                )
        );
        appearTimeline.play();
    }

    /**
     * Change les valeurs de toutes les tuiles de manière aléatoire.
     */
    private void randomizeAllTiles() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                demoTiles[row][col].setValue(getRandomTileValue());
            }
        }
    }

    /**
     * Retourne une valeur aléatoire parmi les valeurs possibles.
     * @return une valeur aléatoire parmi {2, 4, 8, 16, 32, 64, 128}
     */
    private int getRandomTileValue() {
        return possibleValues[random.nextInt(possibleValues.length)];
    }

    /**
     * Démarre le jeu en chargeant la scène principale.
     */
    @FXML
    private void startGame() {
        try {
            if (demoAnimation != null) {
                demoAnimation.stop();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
            Parent gameRoot = loader.load();
            Scene currentScene = developerNameLabel.getScene();
            Stage primaryStage = (Stage) currentScene.getWindow();
            primaryStage.setScene(new Scene(gameRoot));
            primaryStage.setTitle("2048 - Le Jeu");
            primaryStage.show();
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Affiche les options (fonctionnalité à venir).
     */
    @FXML
    private void showOptions() {
        showInfo("Options");
    }

    /**
     * Affiche les meilleurs scores (fonctionnalité à venir).
     */
    @FXML
    private void showHighScores() {
        showInfo("Meilleurs scores");
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
     *
     * @param message Message à afficher.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur lors du chargement du jeu");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'information.
     *
     * @param title Titre de la boîte de dialogue.
     */
    private void showInfo(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText("Cette fonctionnalité sera disponible dans une future mise à jour.");
        alert.showAndWait();
    }
}
