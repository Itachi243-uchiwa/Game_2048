package be.esi.prj.game_2048.model.observer;

/**
 * Interface pour les sujets observables du pattern Observer
 */
public interface Observable {
    void addBoardObserver(BoardObserver observer);
    void removeBoardObserver(BoardObserver observer);
    void notifyBoardObservers();
    
    void addGameStatusObserver(GameStatusObserver observer);
    void removeGameStatusObserver(GameStatusObserver observer);
    void notifyGameStatusObservers();
}