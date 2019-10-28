package ru.abstractcoder.murdermystery.core.slotbar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.abstractcoder.benioapi.item.ItemData;
import ru.abstractcoder.murdermystery.core.slotbar.click.ClickHandler;

public class SlotBarItem {

    private final ItemStack icon;
    private final int slot;
    private final ClickHandler clickHandler;

    @JsonCreator
    public SlotBarItem(
            @JsonProperty("icon") ItemData iconData,
            int slot,
            @JsonProperty("click") ClickHandler clickHandler) {
        this.icon = iconData.toItemStack();
        this.slot = slot;
        this.clickHandler = clickHandler;
    }

    public void addToInventory(Inventory inventory) {
        inventory.setItem(slot, icon);
    }

    public int getSlot() {
        return slot;
    }

    @Nullable
    public ClickHandler getClickHandler() {
        return clickHandler;
    }

}