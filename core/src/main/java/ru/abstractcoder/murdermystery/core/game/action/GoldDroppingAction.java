package ru.abstractcoder.murdermystery.core.game.action;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.util.LocationUtils;
import ru.abstractcoder.murdermystery.core.game.GameEngine;

public class GoldDroppingAction implements GameAction {

    private static final ItemStack GOLD_ITEM = new ItemStack(Material.GOLD_INGOT);

    private final GameEngine gameEngine;

    public GoldDroppingAction(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        gameEngine.getArena().getSpawnPoints().forEach(spawnPoint -> {
            Location loc = LocationUtils.getSurfaceRandomNearLocation(spawnPoint, 7);
            loc.getWorld().dropItemNaturally(loc, GOLD_ITEM);
        });
    }

}