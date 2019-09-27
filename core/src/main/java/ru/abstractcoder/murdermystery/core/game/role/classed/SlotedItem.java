package ru.abstractcoder.murdermystery.core.game.role.classed;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.inventory.ItemStack;
import ru.abstractcoder.benioapi.item.ItemData;

public class SlotedItem {

    private final int slot;
    private final ItemStack itemStack;

    @JsonCreator
    public SlotedItem(int slot, ItemData itemData) {
        this.slot = slot;
        this.itemStack = itemData.toItemStack();
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}