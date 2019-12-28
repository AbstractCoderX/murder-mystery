package ru.abstractcoder.murdermystery.core.game.skin.container;

import net.minecraft.server.EntityPlayer;
import org.bukkit.Bukkit;
import ru.abstractcoder.murdermystery.core.event.SkinViewUpdatedEvent;

import java.util.UUID;

public interface SkinContainable {

    UUID getUniqueId();

    EntityPlayer getNmsHandle();

    default void notifySkinUpdated() {
        Bukkit.getPluginManager().callEvent(new SkinViewUpdatedEvent(this));
    }

    SkinContainer getSkinContainer();

    void setSkinContainer(SkinContainer skinContainer);


}