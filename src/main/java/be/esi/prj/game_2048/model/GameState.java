package be.esi.prj.game_2048.model;

/**
 * Classe qui représente un état du jeu à un moment donné
 * Utilisée pour sauvegarder l'état du plateau et le score pour les fonctionnalités undo/redo
 */
public class GameState {
    private final Tile[][] boardState;
    private final int score;

    public GameState(Board board, int score) {
        this.score = score;
        int size = board.getSize();
        this.boardState = new Tile[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.boardState[i][j] = board.getTile(i, j);
            }
        }
    }

    public Tile[][] getBoard() {
        return boardState;
    }

    public int getScore() {
        return score;
    }
}