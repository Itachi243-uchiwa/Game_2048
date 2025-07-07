package be.esi.prj.game_2048.model.command;

public interface Command {
    boolean execute();
    void undo();
}