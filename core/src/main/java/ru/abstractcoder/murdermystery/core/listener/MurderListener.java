package ru.abstractcoder.murdermystery.core.listener;

import org.bukkit.plugin.Plugin;

public interface MurderListener {

    void register(Plugin plugin);

    void unregister();

}