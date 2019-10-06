package ru.abstractcoder.murdermystery.core.game.action;

import ru.abstractcoder.murdermystery.core.game.player.GamePlayerResolver;

import javax.inject.Inject;

public class InventoryPopulatingAction implements GameAction {

    private final GamePlayerResolver playerResolver;

    @Inject
    public InventoryPopulatingAction(GamePlayerResolver playerResolver) {
        this.playerResolver = playerResolver;
    }

    @Override
    public void execute() {
        playerResolver.getAll().forEach(gamePlayer -> {
            gamePlayer.getRole().getEquipper().equip(gamePlayer.getHandle());
        });
    }

}
