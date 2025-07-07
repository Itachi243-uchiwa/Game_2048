package be.esi.prj.game_2048.model.observer;

public interface GameStatusObserver {
    void onGameStatusChanged(boolean isWon, boolean isGameOver);
}