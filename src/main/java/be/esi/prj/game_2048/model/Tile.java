package be.esi.prj.game_2048.model;

/**
 * Représente une tuile du jeu 2048
 * Contient sa valeur et un flag indiquant si elle a été fusionnée durant le mouvement actuel
 */
public class Tile {
    private int value;
    private boolean merged;

    public Tile(int value) {
        this.value = value;
        this.merged = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}