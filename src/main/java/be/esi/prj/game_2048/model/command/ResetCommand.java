package be.esi.prj.game_2048.model.command;

import be.esi.prj.game_2048.model.Board;
import be.esi.prj.game_2048.model.GameModel;
import be.esi.prj.game_2048.model.GameState;

public class ResetCommand implements Command {
    private final Board board;
    private GameState previousState;
    
    public ResetCommand(Board board) {
        this.board = board;
    }
    
    @Override
    public boolean execute() {
        // Sauvegarde l'état actuel avant la réinitialisation
        previousState = new GameState(board, board.getScore());
        // Réinitialise le jeu
        return board.resetGame();
    }
    
    @Override
    public void undo() {
        if (previousState != null) {
            board.restoreState(previousState);
        }
    }
}