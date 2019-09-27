package ru.abstractcoder.murdermystery.core.game.action;

public interface GameAction {

    void execute();

    default GameAction andThen(GameAction other) {
        return () -> {
            this.execute();
            other.execute();
        };
    }

}