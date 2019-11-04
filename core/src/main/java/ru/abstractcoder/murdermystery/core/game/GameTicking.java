package ru.abstractcoder.murdermystery.core.game;

import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.game.time.GameTime;

public class GameTicking implements Ticking {

    private GameEngine gameEngine;

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public boolean doTick() {
        GameTime gameTime = gameEngine.getGameTime();
        int timeLeft = gameTime.decrement();

        if (timeLeft == 0) {
            gameEngine.endGame();
            return true;
        }

        gameEngine.getActionService().handleTimeTicked(timeLeft);

        return false;
    }

    @Override
    public int getPeriod() {
        return 20;
    }

}