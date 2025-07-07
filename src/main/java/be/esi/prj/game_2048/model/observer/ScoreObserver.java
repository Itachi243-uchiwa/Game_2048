package be.esi.prj.game_2048.model.observer;

/**
 * Interface pour observer les changements de score
 * Implémente le pattern Observer
 */
public interface ScoreObserver {
    /**
     * Méthode appelée lorsque le score change
     * @param newScore Le nouveau score
     */
    void onScoreChanged(int newScore);
}