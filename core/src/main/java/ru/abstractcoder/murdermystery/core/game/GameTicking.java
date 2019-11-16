package ru.abstractcoder.murdermystery.core.game;

import ru.abstractcoder.benioapi.util.ticking.Ticking;
import ru.abstractcoder.murdermystery.core.game.side.GameSide;
import ru.abstractcoder.murdermystery.core.game.time.GameTime;

public class GameTicking implements Ticking {

    private final GameEngine gameEngine;

    public GameTicking(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public boolean doTick() {
        GameTime gameTime = gameEngine.getGameTime();
        int timeLeft = gameTime.decrement();

        if (timeLeft == 0) {
            var playerResolver = gameEngine.getPlayerResolver();
            var survivors = playerResolver.getSurvivors();

            if (survivors.size() == 0) {
                gameEngine.endGame(GameSide.MURDER, playerResolver.getMurder());
            } else {
                gameEngine.endGame(GameSide.SURVIVORS, null);
            }
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