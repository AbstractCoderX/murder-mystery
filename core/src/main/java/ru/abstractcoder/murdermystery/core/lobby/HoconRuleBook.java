package ru.abstractcoder.murdermystery.core.lobby;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.abstractcoder.benioapi.nms.wrapped.CraftPlayer;
import ru.abstractcoder.benioapi.util.ColorUtils;

import java.util.List;

public class HoconRuleBook implements RuleBook {

    private final ItemStack bookItem;

    public HoconRuleBook(HoconConfig bookConfig) {
        List<String> pages = bookConfig.getHandle().getStringList("pages");
        pages.replaceAll(s -> s.replace("\r", ""));
        bookItem = ItemBuilder.fromMaterial(Material.WRITTEN_BOOK)
                .withItemMeta(BookMeta.class)
                .customModifying(bookMeta -> bookMeta.setPages(ColorUtils.color(pages)))
                .and().build();
    }

    @Override
    public void openFor(Player player) {
        CraftPlayer.wrap(player).getHandle().forceOpenBook(bookItem);
    }

}