package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface BukkitListener extends MurderListener, Listener {

    @Override
    default void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    default void unregister() {
        HandlerList.unregisterAll(this);
    }

}
