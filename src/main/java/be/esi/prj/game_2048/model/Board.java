package be.esi.prj.game_2048.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Représente le plateau de jeu et contient la logique des mouvements et fusion des tuiles
 */
public class Board {
    private final int SIZE = 4;  // Taille du plateau (4x4)
    private final Tile[][] tiles;  // Plateau de jeu
    private boolean won;
    private boolean gameOver;
    private int score;

    /**
     * Constructeur initialisant un plateau vide
     */
    public Board() {
        tiles = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j] = new Tile(0);
            }
        }
        score = 0;
        won = false;
        gameOver = false;
    }

    /**
     * Initialise le plateau avec deux tuiles aléatoires
     */
    public void initialize() {
        // Réinitialise le plateau
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setValue(0);
            }
        }
        score = 0;
        won = false;
        gameOver = false;
        
        // Ajoute deux tuiles au départ
        addNewTile();
        addNewTile();
    }

    /**
     * Effectue un mouvement dans la direction spécifiée
     * @param direction Direction du mouvement
     * @return true si le mouvement a modifié le plateau, false sinon
     */
    public boolean move(Direction direction) {
        boolean moved = false;

        switch (direction) {
            case UP:
                moved = moveUp();
                break;
            case DOWN:
                moved = moveDown();
                break;
            case LEFT:
                moved = moveLeft();
                break;
            case RIGHT:
                moved = moveRight();
                break;
        }

        if (moved) {
            addNewTile();  // Ajoute une nouvelle tuile si mouvement valide
            checkGameStatus();  // Vérifie si partie gagnée ou perdue
        }
        
        return moved;
    }

    /**
     * Implémentation du mouvement vers le haut
     * @return true si le mouvement a modifié le plateau, false sinon
     */
    private boolean moveUp() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = 1; i < SIZE; i++) {
                if (tiles[i][j].getValue() != 0) {
                    int row = i;
                    // Déplace la tuile tant qu'il y a de l'espace ou une fusion possible
                    while (row > 0 && (tiles[row-1][j].getValue() == 0 ||
                            tiles[row-1][j].getValue() == tiles[row][j].getValue())) {
                        if (tiles[row-1][j].getValue() == 0) {
                            // Déplacement simple
                            tiles[row-1][j].setValue(tiles[row][j].getValue());
                            tiles[row][j].setValue(0);
                            moved = true;
                        } else if (tiles[row-1][j].getValue() == tiles[row][j].getValue() &&
                                !tiles[row-1][j].isMerged() && !tiles[row][j].isMerged()) {
                            // Fusion de tuiles
                            tiles[row-1][j].setValue(tiles[row][j].getValue() * 2);
                            tiles[row-1][j].setMerged(true);
                            tiles[row][j].setValue(0);
                            score += tiles[row-1][j].getValue();
                            moved = true;
                        }
                        row--;
                    }
                }
            }
        }
        resetMergeFlags();
        return moved;
    }

    /**
     * Implémentation du mouvement vers le bas
     * @return true si le mouvement a modifié le plateau, false sinon
     */
    private boolean moveDown() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = SIZE - 2; i >= 0; i--) {
                if (tiles[i][j].getValue() != 0) {
                    int row = i;
                    // Déplace la tuile tant qu'il y a de l'espace ou une fusion possible
                    while (row < SIZE - 1 && (tiles[row+1][j].getValue() == 0 ||
                            tiles[row+1][j].getValue() == tiles[row][j].getValue())) {
                        if (tiles[row+1][j].getValue() == 0) {
                            // Déplacement simple
                            tiles[row+1][j].setValue(tiles[row][j].getValue());
                            tiles[row][j].setValue(0);
                            moved = true;
                        } else if (tiles[row+1][j].getValue() == tiles[row][j].getValue() &&
                                !tiles[row+1][j].isMerged() && !tiles[row][j].isMerged()) {
                            // Fusion de tuiles
                            tiles[row+1][j].setValue(tiles[row][j].getValue() * 2);
                            tiles[row+1][j].setMerged(true);
                            tiles[row][j].setValue(0);
                            score += tiles[row+1][j].getValue();
                            moved = true;
                        }
                        row++;
                    }
                }
            }
        }
        resetMergeFlags();
        return moved;
    }

    /**
     * Implémentation du mouvement vers la gauche
     * @return true si le mouvement a modifié le plateau, false sinon
     */
    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 1; j < SIZE; j++) {
                if (tiles[i][j].getValue() != 0) {
                    int col = j;
                    // Déplace la tuile tant qu'il y a de l'espace ou une fusion possible
                    while (col > 0 && (tiles[i][col-1].getValue() == 0 ||
                            tiles[i][col-1].getValue() == tiles[i][col].getValue())) {
                        if (tiles[i][col-1].getValue() == 0) {
                            // Déplacement simple
                            tiles[i][col-1].setValue(tiles[i][col].getValue());
                            tiles[i][col].setValue(0);
                            moved = true;
                        } else if (tiles[i][col-1].getValue() == tiles[i][col].getValue() &&
                                !tiles[i][col-1].isMerged() && !tiles[i][col].isMerged()) {
                            // Fusion de tuiles
                            tiles[i][col-1].setValue(tiles[i][col].getValue() * 2);
                            tiles[i][col-1].setMerged(true);
                            tiles[i][col].setValue(0);
                            score += tiles[i][col-1].getValue();
                            moved = true;
                        }
                        col--;
                    }
                }
            }
        }
        resetMergeFlags();
        return moved;
    }

    /**
     * Implémentation du mouvement vers la droite
     * @return true si le mouvement a modifié le plateau, false sinon
     */
    private boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = SIZE - 2; j >= 0; j--) {
                if (tiles[i][j].getValue() != 0) {
                    int col = j;
                    // Déplace la tuile tant qu'il y a de l'espace ou une fusion possible
                    while (col < SIZE - 1 && (tiles[i][col+1].getValue() == 0 ||
                            tiles[i][col+1].getValue() == tiles[i][col].getValue())) {
                        if (tiles[i][col+1].getValue() == 0) {
                            // Déplacement simple
                            tiles[i][col+1].setValue(tiles[i][col].getValue());
                            tiles[i][col].setValue(0);
                            moved = true;
                        } else if (tiles[i][col+1].getValue() == tiles[i][col].getValue() &&
                                !tiles[i][col+1].isMerged() && !tiles[i][col].isMerged()) {
                            // Fusion de tuiles
                            tiles[i][col+1].setValue(tiles[i][col].getValue() * 2);
                            tiles[i][col+1].setMerged(true);
                            tiles[i][col].setValue(0);
                            score += tiles[i][col+1].getValue();
                            moved = true;
                        }
                        col++;
                    }
                }
            }
        }
        resetMergeFlags();
        return moved;
    }

    /**
     * Ajoute une nouvelle tuile (2 ou 4) sur une case vide
     */
    private void addNewTile() {
        List<Point> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].getValue() == 0) {
                    emptyCells.add(new Point(i, j));
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            Point randomCell = emptyCells.get(new Random().nextInt(emptyCells.size()));
            tiles[randomCell.x][randomCell.y].setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    /**
     * Vérifie si le jeu est gagné ou perdu
     */
    private void checkGameStatus() {
        // Réinitialise les états
        won = false;
        gameOver = false;

        // Vérifie si une tuile 2048 existe
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].getValue() == 2048) {
                    won = true;
                    break;
                }
            }
            if (won) break;
        }

        // Vérifie s'il reste des mouvements possibles
        if (!hasEmptyCell() && !hasPossibleMoves()) {
            gameOver = true;
        }
    }

    /**
     * Vérifie s'il reste des cases vides
     * @return true s'il y a au moins une case vide
     */
    private boolean hasEmptyCell() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie s'il reste des fusions possibles
     * @return true s'il y a au moins une fusion possible
     */
    private boolean hasPossibleMoves() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i < SIZE - 1 && tiles[i][j].getValue() == tiles[i + 1][j].getValue()) {
                    return true;
                }
                if (j < SIZE - 1 && tiles[i][j].getValue() == tiles[i][j + 1].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Réinitialise les drapeaux de fusion après chaque mouvement
     */
    private void resetMergeFlags() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setMerged(false);
            }
        }
    }

    public Tile getTile(int x, int y){
        return tiles[x][y];
    }
    public void restoreState(GameState state) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setValue(state.getBoard()[i][j].getValue());
            }
        }
        score = state.getScore();
        checkGameStatus();
    }

    /**
     * Sauvegarde l'état actuel du plateau
     * @return État actuel du plateau
     */
    public GameState saveState() {
        Board boardCopy = boardCopy();
        return new GameState(boardCopy, score);
    }

    private Board boardCopy(){
        Board copy = new Board();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy.getTile(i, j).setValue(tiles[i][j].getValue());
            }
        }
        return copy;
    }
    // Getters
    public Tile[][] getTiles() { return tiles; }
    public int getTileValue(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return tiles[row][col].getValue();
        }
        return 0;
    }
    public boolean resetGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setValue(0);
            }
        }
        score = 0;
        addNewTile();
        addNewTile();
        checkGameStatus();
        gameOver = false;
        won = false;
        return true;
    }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public boolean isWon() { return won; }
    public int getSize() { return SIZE; }
}