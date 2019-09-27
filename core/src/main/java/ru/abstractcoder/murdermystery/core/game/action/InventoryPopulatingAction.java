package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.murdermystery.core.game.GameEngine;

public class InventoryPopulatingAction implements GameAction {

    private final GameEngine gameEngine;

    public InventoryPopulatingAction(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        gameEngine.getPlayerResolver().getAll().forEach(gamePlayer -> {
            gamePlayer.getRole().getEquipper().equip(gamePlayer.getHandle());
        });
    }

}
