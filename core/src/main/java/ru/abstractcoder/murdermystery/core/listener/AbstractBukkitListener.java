package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.plugin.Plugin;

public abstract class AbstractBukkitListener implements org.bukkit.event.Listener, RegistrableListener {

    protected final Plugin plugin;

    protected AbstractBukkitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}