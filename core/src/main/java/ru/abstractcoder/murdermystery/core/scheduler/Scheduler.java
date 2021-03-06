package ru.abstractcoder.murdermystery.core.scheduler;

import dagger.Reusable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;

@Reusable
public class Scheduler {

    private final Plugin plugin;

    @Inject
    public Scheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runSyncLater(long delayTicks, Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delayTicks);
    }

    public BukkitTask runAsynsLater(long delayTicks, Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delayTicks);
    }

}