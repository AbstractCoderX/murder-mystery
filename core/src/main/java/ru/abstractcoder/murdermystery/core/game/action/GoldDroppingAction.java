package ru.abstractcoder.murdermystery.core.game.action;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.util.LocationUtils;
import ru.abstractcoder.murdermystery.core.game.arena.Arena;

import javax.inject.Inject;

public class GoldDroppingAction implements GameAction {

    private static final ItemStack GOLD_ITEM = new ItemStack(Material.GOLD_INGOT);

    private final Arena arena;

    @Inject
    public GoldDroppingAction(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void execute() {
        arena.getSpawnPoints().forEach(spawnPoint -> {
            Location loc = LocationUtils.getSurfaceRandomNearLocation(spawnPoint, 7);
            loc.getWorld().dropItemNaturally(loc, GOLD_ITEM);
        });
    }

}