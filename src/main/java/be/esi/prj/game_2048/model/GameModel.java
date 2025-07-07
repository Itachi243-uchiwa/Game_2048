package be.esi.prj.game_2048.model;

import be.esi.prj.game_2048.model.command.CommandManager;
import be.esi.prj.game_2048.model.command.MoveCommand;
import be.esi.prj.game_2048.model.command.ResetCommand;
import be.esi.prj.game_2048.model.observer.BoardObserver;
import be.esi.prj.game_2048.model.observer.GameStatusObserver;
import be.esi.prj.game_2048.model.observer.Observable;
import be.esi.prj.game_2048.model.observer.ScoreObserver;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Modèle du jeu 2048 - Implémente Singleton, Observer et Command.
 */
public class GameModel implements Observable {
    private static GameModel instance;
    private final Board board;
    private final IntegerProperty score;
    private final CommandManager commandManager;
    private final List<BoardObserver> boardObservers = new ArrayList<>();
    private final List<GameStatusObserver> statusObservers = new ArrayList<>();
    private final List<ScoreObserver> scoreObservers = new ArrayList<>();

    private GameModel() {
        board = new Board();
        score = new SimpleIntegerProperty(0);
        commandManager = new CommandManager();
        board.initialize();
    }

    /**
     * Retourne l'instance unique du modèle.
     * @return instance unique de GameModel
     */
    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    @Override
    public void addBoardObserver(BoardObserver observer) {
        boardObservers.add(observer);
    }

    @Override
    public void removeBoardObserver(BoardObserver observer) {
        boardObservers.remove(observer);
    }

    @Override
    public void notifyBoardObservers() {
        for (BoardObserver observer : boardObservers) {
            observer.onBoardUpdated();
        }
    }

    @Override
    public void addGameStatusObserver(GameStatusObserver observer) {
        statusObservers.add(observer);
    }

    @Override
    public void removeGameStatusObserver(GameStatusObserver observer) {
        statusObservers.remove(observer);
    }

    @Override
    public void notifyGameStatusObservers() {
        for (GameStatusObserver observer : statusObservers) {
            observer.onGameStatusChanged(board.isWon(), board.isGameOver());
        }
    }

    public void addScoreObserver(ScoreObserver observer) {
        scoreObservers.add(observer);
    }

    public void removeScoreObserver(ScoreObserver observer) {
        scoreObservers.remove(observer);
    }

    private void notifyScoreObservers() {
        int currentScore = score.get();
        for (ScoreObserver observer : scoreObservers) {
            observer.onScoreChanged(currentScore);
        }
    }

    /**
     * Exécute un déplacement dans la direction spécifiée.
     * @param direction direction du mouvement
     */
    public void executeMove(Direction direction) {
        if (commandManager.executeCommand(new MoveCommand(board, direction))) {
            score.set(board.getScore());
            notifyBoardObservers();
            notifyScoreObservers();
            notifyGameStatusObservers();
        }
    }

    /**
     * Réinitialise le jeu.
     */
    public void resetGame() {
        commandManager.executeCommand(new ResetCommand(board));
        performReset();
    }

    /**
     * Annule le dernier mouvement.
     */
    public void undoLastMove() {
        commandManager.undo();
        notifyBoardObservers();
        notifyScoreObservers();
        notifyGameStatusObservers();
    }

    /**
     * Refait un mouvement annulé.
     */
    public void redoMove() {
        commandManager.redo();
        notifyBoardObservers();
        notifyScoreObservers();
        notifyGameStatusObservers();
    }

    public boolean canUndo() { return commandManager.canUndo(); }
    public boolean canRedo() { return commandManager.canRedo(); }

    /**
     * Réinitialise le plateau et met à jour les observateurs.
     */
    public void performReset() {
        board.initialize();
        score.set(board.getScore());
        notifyBoardObservers();
        notifyScoreObservers();
        notifyGameStatusObservers();
    }

    /**
     * Sauvegarde l'état actuel du jeu.
     * @return état du jeu sauvegardé
     */
    public GameState saveState() {
        return board.saveState();
    }

    /**
     * Restaure un état sauvegardé.
     * @param state état à restaurer
     */
    public void restoreState(GameState state) {
        board.restoreState(state);
        score.set(board.getScore());
        notifyBoardObservers();
        notifyScoreObservers();
        notifyGameStatusObservers();
    }

    public int getTileValue(int row, int col) { return board.getTileValue(row, col); }
    public int getScore() { return score.get(); }
    public IntegerProperty scoreProperty() { return score; }
    public boolean isGameOver() { return board.isGameOver(); }
    public boolean isWon() { return board.isWon(); }
    public int getSize() { return board.getSize(); }
    public Tile[][] getBoard() { return board.getTiles(); }
}
