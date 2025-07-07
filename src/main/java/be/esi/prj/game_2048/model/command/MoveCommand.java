package be.esi.prj.game_2048.model.command;

import be.esi.prj.game_2048.model.Board;
import be.esi.prj.game_2048.model.Direction;
import be.esi.prj.game_2048.model.GameModel;
import be.esi.prj.game_2048.model.GameState;

public class MoveCommand implements Command {
    private final Board board;
    private final Direction direction;
    private GameState previousState;
    
    public MoveCommand(Board board, Direction direction) {
        this.board = board;
        this.direction = direction;
    }
    
    @Override
    public boolean execute() {
        // Sauvegarde l'état actuel avant le mouvement
        previousState = new GameState(board, board.getScore());
        // Effectue le mouvement
       return board.move(direction);
    }
    
    @Override
    public void undo() {
        if (previousState != null) {
            board.restoreState(previousState);
        }
    }
}