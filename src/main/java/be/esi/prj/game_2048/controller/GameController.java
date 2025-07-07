package be.esi.prj.game_2048.controller;

import be.esi.prj.game_2048.model.Direction;
import be.esi.prj.game_2048.model.GameModel;
import be.esi.prj.game_2048.model.Point;
import be.esi.prj.game_2048.model.observer.BoardObserver;
import be.esi.prj.game_2048.model.observer.GameStatusObserver;
import be.esi.prj.game_2048.model.observer.ScoreObserver;
import be.esi.prj.game_2048.view.TileView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Gère l'interaction entre le modèle et la vue.
 * Implémente les interfaces Observer pour réagir aux changements du modèle.
 */
public class GameController implements BoardObserver, GameStatusObserver, ScoreObserver {
    @FXML private GridPane gameGrid;
    @FXML private Label scoreLabel;
    @FXML private Label gameStatusLabel;
    @FXML private VBox gameOverPanel;
    @FXML private Label finalScoreLabel;
    @FXML private VBox gameContainer;

    private final GameModel gameModel = GameModel.getInstance();
    private final Map<Point, TileView> tileViews = new HashMap<>();
    private Direction lastMoveDirection = null;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        setupGrid();
        bindUiElements();
        gameModel.addBoardObserver(this);
        gameModel.addGameStatusObserver(this);
        gameModel.addScoreObserver(this);
        updateBoard();
        gameContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyPress);
                gameContainer.requestFocus();
            }
        });
    }

    /**
     * Lie les éléments de l'interface aux propriétés du modèle.
     */
    private void bindUiElements() {
        scoreLabel.textProperty().bind(gameModel.scoreProperty().asString("Score: %d"));
        }

    /**
     * Gère les entrées clavier pour déplacer les tuiles.
     */
    private void handleKeyPress(KeyEvent event) {
        if (gameModel.isGameOver() || gameModel.isWon()) return;
        Direction direction = switch (event.getCode()) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            default -> null;
        };
        if (direction != null) {
            lastMoveDirection = direction;
            gameModel.executeMove(direction);
            event.consume();
        }
    }


    @FXML
    private void handleRestart() {
        gameModel.resetGame();
        initialize();

        if (gameContainer.getScene() != null) {
            gameContainer.getScene().setOnKeyPressed(this::handleKeyPress);
        }
        gameContainer.requestFocus();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), gameContainer);
        fadeOut.setToValue(0.5);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), gameContainer);
        fadeIn.setToValue(1.0);
        fadeIn.setDelay(Duration.millis(200));
        fadeOut.setOnFinished(e -> {
            gameStatusLabel.setText("");
            gameStatusLabel.getStyleClass().setAll("status-label");
            gameOverPanel.setVisible(false);
            gameOverPanel.setManaged(false);
            fadeIn.play();
        });
        fadeOut.play();
    }


    @Override
    public void onBoardUpdated() {
        Platform.runLater(this::updateBoard);
    }

    @Override
    public void onGameStatusChanged(boolean isWon, boolean isGameOver) {
        Platform.runLater(() -> {
            if (isWon) showGameWonMessage();
            else if (isGameOver) showGameOverMessage();
            else {
                gameStatusLabel.setText("");
                gameOverPanel.setVisible(false);
                gameOverPanel.setManaged(false);
            }
        });
    }

    @Override
    public void onScoreChanged(int newScore) {
        Platform.runLater(() -> {});
    }

    private void updateBoard() {
        if (lastMoveDirection != null) playMoveAnimation(lastMoveDirection);
        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                TileView tileView = tileViews.get(new Point(i, j));
                int newValue = gameModel.getTileValue(i, j);
                if (newValue != tileView.getValue()) {
                    if (newValue != 0 && tileView.getValue() == 0) playAppearAnimation(tileView);
                    else if (newValue != 0 && tileView.getValue() != 0 && newValue != tileView.getValue()) playMergeAnimation(tileView);
                    tileView.setValue(newValue);
                }
            }
        }
    }

    private void playMoveAnimation(Direction direction) {
        double distance = 15.0;
        double translateX = switch (direction) {
            case LEFT -> -distance;
            case RIGHT -> distance;
            default -> 0;
        };
        double translateY = switch (direction) {
            case UP -> -distance;
            case DOWN -> distance;
            default -> 0;
        };
        ParallelTransition parallelTransition = new ParallelTransition();
        for (TileView tileView : tileViews.values()) {
            if (tileView.getValue() > 0) {
                TranslateTransition moveOut = new TranslateTransition(Duration.millis(75), tileView);
                moveOut.setByX(translateX);
                moveOut.setByY(translateY);
                TranslateTransition moveBack = new TranslateTransition(Duration.millis(75), tileView);
                moveBack.setByX(-translateX);
                moveBack.setByY(-translateY);
                parallelTransition.getChildren().add(new SequentialTransition(moveOut, moveBack));
            }
        }
        parallelTransition.play();
    }

// Animation d'apparition des nouvelles tuiles (améliorée)
    private void playAppearAnimation(TileView tileView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), tileView);
        scaleTransition.setFromX(0.1);
        scaleTransition.setFromY(0.1);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), tileView);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();
    }

    // Animation de fusion des tuiles (améliorée)
    private void playMergeAnimation(TileView tileView) {
        // Animation plus visible pour les fusions
        ScaleTransition scaleGrow = new ScaleTransition(Duration.millis(125), tileView);
        scaleGrow.setToX(1.2);
        scaleGrow.setToY(1.2);

        ScaleTransition scaleShrink = new ScaleTransition(Duration.millis(125), tileView);
        scaleShrink.setToX(1.0);
        scaleShrink.setToY(1.0);
        scaleShrink.setDelay(Duration.millis(125));

        ParallelTransition parallelTransition = new ParallelTransition(scaleGrow, scaleShrink);
        parallelTransition.play();
    }

    private void showGameWonMessage() {
        gameStatusLabel.setText("Félicitations ! Vous avez gagné !");
        gameStatusLabel.getStyleClass().setAll("status-label", "status-won");

        ScaleTransition scaleGrow = new ScaleTransition(Duration.millis(400), gameStatusLabel);
        scaleGrow.setToX(1.2);
        scaleGrow.setToY(1.2);

        ScaleTransition scaleShrink = new ScaleTransition(Duration.millis(200), gameStatusLabel);
        scaleShrink.setToX(1.0);
        scaleShrink.setToY(1.0);
        scaleShrink.setDelay(Duration.millis(400));

        new ParallelTransition(scaleGrow, scaleShrink).play();
    }

    private void showGameOverMessage() {
        gameStatusLabel.setText("Partie terminée !");
        gameStatusLabel.getStyleClass().setAll("status-label", "status-lost");

        finalScoreLabel.setText("Score final: " + gameModel.getScore());
        gameOverPanel.setVisible(true);
        gameOverPanel.setManaged(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), gameOverPanel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    private void setupGrid() {
        gameGrid.getChildren().clear();
        tileViews.clear();

        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                TileView tileView = new TileView();
                tileView.setValue(gameModel.getTileValue(i, j));
                gameGrid.add(tileView, j, i);
                tileViews.put(new Point(i, j), tileView);
            }
        }
    }
}