package ru.abstractcoder.murdermystery.core.game.role.stored;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public interface StoredTemplate {

    ItemStack getItem(int slot);

    void populateInventory(PlayerInventory inventory);

}
