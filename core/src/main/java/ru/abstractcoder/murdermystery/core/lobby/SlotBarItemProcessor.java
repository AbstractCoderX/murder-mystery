package ru.abstractcoder.murdermystery.core.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import ru.abstractcoder.murdermystery.core.lobby.slotbar.SlotBarItemResolver;

public class SlotBarItemProcessor {

    private final SlotBarItemResolver slotBarItemResolver;

    public SlotBarItemProcessor(SlotBarItemResolver slotBarItemResolver) {
        this.slotBarItemResolver = slotBarItemResolver;
    }

    public void populatePlayerInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        slotBarItemResolver.getAllItems()
                .forEach(slotBarItem -> slotBarItem.addToInventory(inventory));
    }

    public void clearAll() {
        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());
    }

}