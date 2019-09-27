package ru.abstractcoder.murdermystery.core.game.role.stored;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.abstractcoder.benioapi.item.ItemData;

import java.util.Map;

public abstract class AbstractStoredTemplate implements StoredTemplate {

    private Int2ObjectMap<ItemStack> itemsMap;

    protected AbstractStoredTemplate(Map<Integer, ItemData> itemDataMap) {
        if (itemDataMap != null) {
            itemsMap = new Int2ObjectOpenHashMap<>(itemDataMap.size());
            itemDataMap.forEach((slot, itemData) -> itemsMap.put((int) slot, itemData.toItemStack()));
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        Preconditions.checkState(itemsMap != null, "Items not stored for this template");
        return itemsMap.get(slot);
    }

    @Override
    public void populateInventory(PlayerInventory inventory) {
        if (itemsMap == null) {
            return;
        }
        itemsMap.int2ObjectEntrySet()
                .forEach(entry -> inventory.setItem(entry.getIntKey(), entry.getValue()));
    }

}
