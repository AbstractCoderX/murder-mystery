package ru.abstractcoder.murdermystery.core.cosmetic;

import org.bukkit.entity.Player;
import ru.abstractcoder.benioapi.gui.template.issuer.GuiAndCommandUserMixin;
import ru.abstractcoder.benioapi.item.ItemData;

public interface PremiumCosmetic extends Cosmetic {

    boolean isAvailableFor(Player player);

    default boolean isAvailableFor(GuiAndCommandUserMixin player) {
        return isAvailableFor(player.getHandle());
    }

    ItemData getIconData();

}