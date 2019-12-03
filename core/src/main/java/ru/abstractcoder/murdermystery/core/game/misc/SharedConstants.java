package ru.abstractcoder.murdermystery.core.game.misc;

import com.destroystokyo.paper.Title;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class SharedConstants {

    public static final int WEAPON_SLOT = 1;
    public static final int ARROW_SLOT = 7;
    public static final int GOLD_SLOT = 8;

    public static final ItemStack ARROW_ITEM = new ItemStack(Material.ARROW);

    public static final Title EMPTY_TITLE = Title.builder().title("").build();

}
